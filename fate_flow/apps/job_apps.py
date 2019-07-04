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
from fate_flow.utils.api_utils import get_json_result
from flask import Flask, request
from fate_flow.settings import logger
from fate_flow.driver.job_controller import JobController

manager = Flask(__name__)


@manager.errorhandler(500)
def internal_server_error(e):
    logger.exception(e)
    return get_json_result(retcode=100, retmsg=str(e))


@manager.route('/submit', methods=['POST'])
def submit_job():
    job_id, job_dsl_path, job_runtime_conf_path = JobController.submit_job(request.json)
    return get_json_result(job_id=job_id, data={'job_dsl_path': job_dsl_path,
                                                'job_runtime_conf_path': job_runtime_conf_path})


@manager.route('/<job_id>/<component_name>/<task_id>/<role>/<party_id>/run', methods=['POST'])
def run_task(job_id, component_name, task_id, role, party_id):
    task_data = request.json
    task_data['request_url_without_host'] = request.url.lstrip(request.host_url)
    JobController.start_task(job_id, component_name, task_id, role, party_id, request.json)
    return get_json_result(retcode=0, retmsg='success')


@manager.route('/<job_id>/<component_name>/<task_id>/<role>/<party_id>/status', methods=['POST'])
def task_status(job_id, component_name, task_id, role, party_id):
    JobController.task_status(job_id, component_name, task_id, role, party_id, request.json)
    return get_json_result(retcode=0, retmsg='success')
