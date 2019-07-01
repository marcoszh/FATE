#
#  Copyright 2019 The FATE Authors. All Rights Reserved.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#
from fate_flow.utils.job_utils import generate_job_id, save_job_conf, query_tasks, get_job_dsl_parser
from arch.api.utils import file_utils
from fate_flow.utils.api_utils import get_json_result, federated_api
from arch.api.utils.core import current_timestamp, json_dumps
from flask import Flask, request
from fate_flow.settings import IP, logger
import importlib
from fate_flow.db.db_models import Task, Job
from fate_flow.manager.tracking import Tracking
from fate_flow.manager.queue_manager import JOB_QUEUE
from fate_flow.storage.fate_storage import FateStorage
from fate_flow.entity.metric import Metric
from fate_flow.settings import API_VERSION

manager = Flask(__name__)


@manager.errorhandler(500)
def internal_server_error(e):
    logger.exception(e)
    return get_json_result(status=100, msg=str(e))


@manager.route('/submit', methods=['POST'])
def submit_job():
    job_data = request.json
    job_id = generate_job_id()
    logger.info('generated job_id {}, body {}'.format(job_id, job_data))
    job_config = job_data.get('job_runtime_conf', {})
    job_dsl = job_data.get('job_dsl', {})
    job_dsl_path, job_runtime_conf_path = save_job_conf(job_id=job_id,
                                                      job_dsl=job_dsl,
                                                      job_runtime_conf=job_config)
    job_initiator = job_config.get('initiator', None)
    job = Job()
    job.f_job_id = job_id
    job.f_role = job_initiator.get('role', '')
    job.f_party_id = job_initiator.get('party_id', '')
    job.f_roles = json_dumps(job_config.get('role', {}))
    job.f_initiator_party_id = job_initiator.get('party_id', '')
    job.f_is_initiator = 1
    job.f_dsl = json_dumps(job_dsl)
    job.f_runtime_conf = json_dumps(job_config)
    job.f_run_ip = IP
    job.f_status = 'waiting'
    job.f_create_time = current_timestamp()
    Tracking(job_id=job_id, role=job_initiator.get('role', ''),
             party_id=job_initiator.get('party_id', '')).save_job_info(job.f_role, job.f_party_id, job.to_json(),
                                                                       create=True)
    JOB_QUEUE.put_event({
        'job_id': job_id,
        "job_dsl_path": job_dsl_path,
        "job_runtime_conf_path": job_runtime_conf_path
    }
    )
    return get_json_result(job_id=job_id, data={'job_dsl_path': job_dsl_path,
                                                'job_runtime_conf_path': job_runtime_conf_path})


def run_job(job_id, job_dsl_path, job_runtime_conf_path):
    dsl = get_job_dsl_parser(job_id=job_id, job_dsl_path=job_dsl_path, job_runtime_conf_path=job_runtime_conf_path)
    job_config = file_utils.load_json_conf(job_runtime_conf_path)
    job_initiator = job_config.get('initiator', None)
    job_args = dsl.get_args_input()
    if not job_initiator:
        return False
    FateStorage.init_storage(job_id=job_id)
    job = Job()
    job.f_create_time = current_timestamp()
    loops = 0
    component_name = None
    while True:
        components = dsl.get_next_components(component_name)
        if not components:
            break
        loops += 1
        task_ids = []

        for component in components:
            parameters = component.get_role_parameters()
            component_name = component.get_name()
            module_name = component.get_module()
            task_id = '{}_{}_{}'.format(job_id, component_name, loops)
            task_ids.append(task_id)
            for role, partys_parameters in parameters.items():
                for party_index in range(len(partys_parameters)):
                    party_parameters = partys_parameters[party_index]
                    party_job_args = job_args[role][party_index]['args']
                    dest_party_id = party_parameters.get('local', {}).get('party_id')
                    federated_api(job_id=job_id,
                                  method='POST',
                                  url='/{}/job/{}/{}/{}/{}/{}/run'.format(
                                      API_VERSION,
                                      job_id,
                                      component_name,
                                      task_id,
                                      role,
                                      dest_party_id),
                                  dest_party_id=dest_party_id,
                                  json_body={'job_initiator': job_initiator,
                                             'job_args': party_job_args,
                                             'parameters': party_parameters,
                                             'module_name': module_name,
                                             'input': component.get_input(),
                                             'output': component.get_output()})
        all_task_status = set()
        for task_id in task_ids:
            tasks = query_tasks(job_id=job_id, task_id=task_id)
            all_task_status.update(set([task.f_status for task in tasks]))
        if len(all_task_status) == 1 and 'success' in all_task_status:
            continue
        else:
            break


@manager.route('/<job_id>/<component_name>/<task_id>/<role>/<party_id>/run', methods=['POST'])
def run_task(job_id, component_name, task_id, role, party_id):
    request_data = request.json
    request_url_without_host = request.url.lstrip(request.host_url)
    job_initiator = request_data.get('job_initiator', None)
    job_args = request_data.get('job_args', {})
    task_input_dsl = request_data.get('input', {})
    task_output_dsl = request_data.get('output', {})
    parameters = request_data.get('parameters', None)
    module_name = request_data.get('module_name', '')
    task = Task()
    task.f_job_id = job_id
    task.f_component_name = component_name
    task.f_task_id = task_id
    task.f_role = role
    task.f_party_id = party_id
    task.f_create_time = current_timestamp()
    tracker = Tracking(job_id=job_id, role=role, party_id=party_id, component_name=component_name, task_id=task_id,
                       model_id='jarvis_test')
    try:
        task.f_start_time = current_timestamp()
        task.f_operator = 'python_operator'
        task.f_run_ip = IP
        task.f_run_pid = 1234
        run_class_paths = parameters.get('CodePath').split('/')
        run_class_package = '.'.join(run_class_paths[:-2]) + '.' + run_class_paths[-2].rstrip('.py')
        run_class_name = run_class_paths[-1]
        task_run_args = get_task_run_args(job_id=job_id, role=role, party_id=party_id, job_args=job_args, input_dsl=task_input_dsl)
        run_object = getattr(importlib.import_module(run_class_package), run_class_name)()
        run_object.set_tracker(tracker=tracker)
        run_object.run(parameters, task_run_args)
        output_model = run_object.export_model()
        output_data = run_object.save_data()
        if task_output_dsl:
            tracker.save_output_data_table(output_data, task_output_dsl.get('data')[0])
        tracker.save_output_model(output_model)
        tracker.save_output_model_meta({'module_name': module_name})
        task.f_status = 'success'
        tracker.log_metric_data('TRAIN', 'LOSS0', [Metric(key=1, value=0.1), Metric(key=2, value=0.2)])
    except Exception as e:
        logger.exception(e)
        task.f_status = 'failed'
    finally:
        task.f_end_time = current_timestamp()
        task.f_elapsed = task.f_end_time - task.f_start_time
        task.f_update_time = current_timestamp()
        task_info = task.to_json()
        tracker.save_task(role=role, party_id=party_id, task_info=task_info, create=True)
        federated_api(job_id=job_id,
                      method='POST',
                      url=request_url_without_host.replace('run', 'status'),
                      dest_party_id=job_initiator.get('party_id', None),
                      json_body=task_info)
    return get_json_result(status=0, msg='success')


def get_task_run_args(job_id, role, party_id, job_args, input_dsl):
    task_run_args = {}
    for input_type, input_detail in input_dsl.items():
        if input_type == 'data':
            this_type_args = task_run_args[input_type] = task_run_args.get(input_type, {})
            for data_type, data_list in input_detail.items():
                for data_key in data_list:
                    data_key_item = data_key.split('.')
                    search_component_name, search_data_name = data_key_item[0], data_key_item[1]
                    if search_component_name == 'args':
                        data_table = FateStorage.table(
                            namespace=job_args.get('data', {}).get(search_data_name).get('namespace', ''),
                            name=job_args.get('data', {}).get(search_data_name).get('name', ''))
                    else:
                        data_table = Tracking(job_id=job_id, role=role, party_id=party_id, component_name=search_component_name).get_output_data_table(
                            data_name=search_data_name)
                    args_from_component = this_type_args[search_component_name] = this_type_args.get(search_component_name, {})
                    args_from_component[data_type] = data_table
        elif input_type == 'model':
            this_type_args = task_run_args[input_type] = task_run_args.get(input_type, {})
            for model_key in input_detail:
                model_key_items = model_key.split('.')
                search_component_name, search_model_name = model_key_items[0], model_key_items[1]
                models = Tracking(job_id=job_id, role=role, party_id=party_id, component_name=search_component_name, model_id='jarvis_test').get_output_model()
                this_type_args[search_component_name] = models
    return task_run_args


@manager.route('/<job_id>/<component_name>/<task_id>/<role>/<party_id>/status', methods=['POST'])
def task_status(job_id, component_name, task_id, role, party_id):
    task_info = request.json
    tracker = Tracking(job_id=job_id, role=role, party_id=party_id, component_name=component_name, task_id=task_id)
    tracker.save_task(role=role, party_id=party_id, task_info=task_info)
    return get_json_result(status=0, msg='success')
