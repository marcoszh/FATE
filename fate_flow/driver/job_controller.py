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
from fate_flow.utils.job_utils import generate_job_id, save_job_conf, query_tasks, get_job_dsl_parser, run_subprocess
from fate_flow.utils import job_utils
from arch.api.utils import file_utils, log_utils
from fate_flow.utils.api_utils import federated_api
from arch.api.utils.core import current_timestamp, json_dumps
from arch.api import federation
from fate_flow.settings import IP, logger, MAX_CONCURRENT_JOB_RUN
import importlib
from fate_flow.db.db_models import Task, Job
from fate_flow.manager.tracking import Tracking
from fate_flow.manager.queue_manager import JOB_QUEUE
from fate_flow.storage.fate_storage import FateStorage
from fate_flow.entity.metric import Metric
from fate_flow.settings import API_VERSION
from concurrent.futures import ThreadPoolExecutor
import os
import argparse
import json
import time


class JobController(object):
    task_executor_pool = None

    @staticmethod
    def init():
        JobController.task_executor_pool = ThreadPoolExecutor(max_workers=MAX_CONCURRENT_JOB_RUN)
        # JobController.task_executor_pool._work_queue.maxsize = MAX_CONCURRENT_JOB_RUN

    @staticmethod
    def submit_job(job_data):
        job_id = generate_job_id()
        logger.info('submit job, job_id {}, body {}'.format(job_id, job_data))
        job_runtime_conf = job_data.get('job_runtime_conf', {})
        job_dsl = job_data.get('job_dsl', {})
        job_dsl_path, job_runtime_conf_path = save_job_conf(job_id=job_id,
                                                            job_dsl=job_dsl,
                                                            job_runtime_conf=job_runtime_conf)
        job_initiator = job_runtime_conf.get('initiator', None)
        job = Job()
        job.f_job_id = job_id
        job.f_role = job_initiator.get('role', '')
        job.f_party_id = job_initiator.get('party_id', '')
        job.f_roles = json_dumps(job_runtime_conf.get('role', {}))
        job.f_initiator_party_id = job_initiator.get('party_id', '')
        job.f_is_initiator = 1
        job.f_dsl = json_dumps(job_dsl)
        job.f_runtime_conf = json_dumps(job_runtime_conf)
        job.f_run_ip = IP
        job.f_status = 'waiting'
        job.f_create_time = current_timestamp()
        job_tracker = Tracking(job_id=job_id, role=job_initiator.get('role', ''),
                               party_id=job_initiator.get('party_id', ''))
        job_tracker.save_job_info(job.f_role, job.f_party_id, job.to_json(), create=True)
        job_tracker.log_job_view({'role': job_runtime_conf.get('role', {}), 'initiator': job_initiator})
        JOB_QUEUE.put_event({
            'job_id': job_id,
            "job_dsl_path": job_dsl_path,
            "job_runtime_conf_path": job_runtime_conf_path
        }
        )
        logger.info('submit job successfully, job id is {}'.format(job.f_job_id))
        return job_id, job_dsl_path, job_runtime_conf_path

    @staticmethod
    def run_job(job_id, job_dsl_path, job_runtime_conf_path):
        dsl = get_job_dsl_parser(job_id=job_id, job_dsl_path=job_dsl_path, job_runtime_conf_path=job_runtime_conf_path)
        job_config = file_utils.load_json_conf(job_runtime_conf_path)
        job_initiator = job_config.get('initiator', None)
        job_args = dsl.get_args_input()
        if not job_initiator:
            return False
        FateStorage.init_storage(job_id=job_id)
        job = Job()
        job.f_job_id = job_id
        job.f_start_time = current_timestamp()
        job.f_role = job_initiator.get('role', '')
        job.f_party_id = job_initiator.get('party_id', '')
        job.f_status = 'running'
        job_tracker = Tracking(job_id=job_id, role=job.f_role, party_id=job.f_party_id)
        job_tracker.save_job_info(role=job.f_role, party_id=job.f_party_id, job_info=job.to_json())

        component_names = []
        task_ids = []
        top_level_task_status = set()
        component_count = len(dsl.get_dependency()['component_list'])
        components = dsl.get_next_components(None)
        logger.info('get job {} next components {} by {}'.format(job.f_job_id, components, None))
        for component in components:
            try:
                run_status = JobController.run_component(job_id, job_args, job_initiator, job, job_tracker, component_names, task_ids, dsl, component)
            except Exception as e:
                logger.info(e)
                run_status = False
            top_level_task_status.add(run_status)
            if not run_status:
                break
        if len(top_level_task_status) == 2:
            job.f_status = 'partial'
        elif True in top_level_task_status:
            job.f_status = 'success'
        else:
            job.f_status = 'failed'
        job.f_end_time = current_timestamp()
        job.f_elapsed = job.f_end_time - job.f_start_time
        job.f_progress = 100
        job.f_update_time = current_timestamp()
        job_tracker.save_job_info(role=job.f_role, party_id=job.f_party_id, job_info=job.to_json())
        logger.info('job {} {}'.format(job.f_job_id, job.f_status))

    @staticmethod
    def run_component(job_id, job_args, job_initiator, job, job_tracker, component_names, task_ids, dsl, component):
        parameters = component.get_role_parameters()
        component_name = component.get_name()
        component_names.append(component_name)
        module_name = component.get_module()
        task_id = '{}_{}'.format(job_id, component_name)
        task_ids.append(task_id)
        job.f_current_steps = json_dumps(component_names)
        job.f_current_tasks = json_dumps(task_ids)
        job.f_update_time = current_timestamp()
        job_tracker.save_job_info(role=job.f_role, party_id=job.f_party_id, job_info=job.to_json())
        logger.info('run job {} task {}'.format(job_id, component_name))
        task_success = False
        for role, partys_parameters in parameters.items():
            for party_index in range(len(partys_parameters)):
                party_parameters = partys_parameters[party_index]
                if role in job_args:
                    party_job_args = job_args[role][party_index]['args']
                else:
                    party_job_args = {}
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
                              src_party_id=job_initiator['party_id'],
                              dest_party_id=dest_party_id,
                              json_body={'job_initiator': job_initiator,
                                         'job_args': party_job_args,
                                         'parameters': party_parameters,
                                         'module_name': module_name,
                                         'input': component.get_input(),
                                         'output': component.get_output()})
        while True:
            tasks = query_tasks(job_id=job_id, task_id=task_id)
            if tasks and tasks[0].f_status in ['failed', 'success']:
                if tasks[0].f_status == 'success':
                    task_success = True
                break
            time.sleep(2)
        if task_success:
            components = dsl.get_next_components(component_name)
            for component in components:
                try:
                    run_status = JobController.run_component(job_id, job_args, job_initiator, job, job_tracker, component_names, task_ids, dsl, component)
                except Exception as e:
                    logger.info(e)
                    run_status = False
                if not run_status:
                    return False
            return True
        else:
            return False

    @staticmethod
    def start_task(job_id, component_name, task_id, role, party_id, task_config):
        print('start task subprocess success {} {} {} {} {}'.format(job_id, component_name, task_id, role, party_id,
                                                                    task_config))
        task_dir = os.path.join(job_utils.get_job_directory(job_id=job_id), role, party_id, component_name)
        if not os.path.exists(task_dir):
            os.makedirs(task_dir)
        task_config_path = os.path.join(task_dir, 'task_config.json')
        with open(task_config_path, 'w') as fw:
            json.dump(task_config, fw)
        process_cmd = [
            'python3', __file__,
            '-j', job_id,
            '-n', component_name,
            '-t', task_id,
            '-r', role,
            '-p', party_id,
            '-c', task_config_path
        ]
        p = run_subprocess(job_dir=task_dir, job_role=role, process_cmd=process_cmd)

    @staticmethod
    def run_task():
        parser = argparse.ArgumentParser()
        parser.add_argument('-j', '--job_id', required=True, type=str, help="Specify a config json file path")
        parser.add_argument('-n', '--component_name', required=True, type=str, help="Specify a config json file path")
        parser.add_argument('-t', '--task_id', required=True, type=str, help="Specify a config json file path")
        parser.add_argument('-r', '--role', required=True, type=str, help="Specify a config json file path")
        parser.add_argument('-p', '--party_id', required=True, type=str, help="Specify a config json file path")
        parser.add_argument('-c', '--config', required=True, type=str, help="Specify a config json file path")
        args = parser.parse_args()
        logger.info('start run task')
        logger.info(args)
        # init function args
        job_id = args.job_id
        component_name = args.component_name
        task_id = args.task_id
        role = args.role
        party_id = int(args.party_id)
        task_config = file_utils.load_json_conf(args.config)
        logger.info('task info {} {} {} {} {}'.format(job_id, component_name, task_id, role, party_id))
        request_url_without_host = task_config['request_url_without_host']
        job_initiator = task_config.get('job_initiator', None)
        job_args = task_config.get('job_args', {})
        task_input_dsl = task_config.get('input', {})
        task_output_dsl = task_config.get('output', {})
        parameters = task_config.get('parameters', {})
        module_name = task_config.get('module_name', '')

        # init environment
        FateStorage.init_storage(job_id=job_id)
        federation.init(job_id=job_id, runtime_conf=parameters)
        log_utils.LoggerFactory.setDirectory(os.path.join(file_utils.get_project_base_directory(), 'logs', job_id))

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
            task.f_run_pid = os.getpid()
            run_class_paths = parameters.get('CodePath').split('/')
            run_class_package = '.'.join(run_class_paths[:-2]) + '.' + run_class_paths[-2].rstrip('.py')
            run_class_name = run_class_paths[-1]
            task_run_args = JobController.get_task_run_args(job_id=job_id, role=role, party_id=party_id,
                                                            job_args=job_args, input_dsl=task_input_dsl)
            run_object = getattr(importlib.import_module(run_class_package), run_class_name)()
            run_object.set_tracker(tracker=tracker)
            task.f_status = 'running'
            tracker.save_task(role=role, party_id=party_id, task_info=task.to_json(), create=True)

            run_object.run(parameters, task_run_args)
            if task_output_dsl:
                if task_output_dsl.get('data', {}):
                    output_data = run_object.save_data()
                    tracker.save_output_data_table(output_data, task_output_dsl.get('data')[0])
                if task_output_dsl.get('model', {}):
                    output_model = run_object.export_model()
                    tracker.save_output_model(output_model, module_name)
            task.f_status = 'success'
        except Exception as e:
            logger.exception(e)
            task.f_status = 'failed'
        finally:
            task.f_end_time = current_timestamp()
            task.f_elapsed = task.f_end_time - task.f_start_time
            task.f_update_time = current_timestamp()
            tracker.save_task(role=role, party_id=party_id, task_info=task.to_json(), create=True)
            federated_api(job_id=job_id,
                          method='POST',
                          url=request_url_without_host.replace('run', 'status'),
                          src_party_id=task.f_party_id,
                          dest_party_id=job_initiator.get('party_id', None),
                          json_body=task.to_json())
        logger.info('finish task {} {} {} {} {}'.format(job_id, component_name, task_id, role, party_id))

    @staticmethod
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
                            if job_args.get('data', {}).get(search_data_name).get('namespace', '') and job_args.get(
                                    'data', {}).get(search_data_name).get('name', ''):

                                data_table = FateStorage.table(
                                    namespace=job_args['data'][search_data_name]['namespace'],
                                    name=job_args['data'][search_data_name]['name'])
                            else:
                                data_table = None
                        else:
                            data_table = Tracking(job_id=job_id, role=role, party_id=party_id,
                                                  component_name=search_component_name).get_output_data_table(
                                data_name=search_data_name)
                        args_from_component = this_type_args[search_component_name] = this_type_args.get(
                            search_component_name, {})
                        args_from_component[data_type] = data_table
            elif input_type == 'model':
                this_type_args = task_run_args[input_type] = task_run_args.get(input_type, {})
                for model_key in input_detail:
                    model_key_items = model_key.split('.')
                    search_component_name, search_model_name = model_key_items[0], model_key_items[1]
                    models = Tracking(job_id=job_id, role=role, party_id=party_id, component_name=search_component_name,
                                      model_id='jarvis_test').get_output_model()
                    this_type_args[search_component_name] = models
        return task_run_args

    @staticmethod
    def task_status(job_id, component_name, task_id, role, party_id, task_info):
        tracker = Tracking(job_id=job_id, role=role, party_id=party_id, component_name=component_name, task_id=task_id)
        tracker.save_task(role=role, party_id=party_id, task_info=task_info)


if __name__ == '__main__':
    JobController.run_task()
