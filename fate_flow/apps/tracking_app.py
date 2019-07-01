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
from fate_flow.settings import logger
from flask import Flask, request
from fate_flow.manager.tracking import Tracking

manager = Flask(__name__)


@manager.errorhandler(500)
def internal_server_error(e):
    logger.exception(e)
    return get_json_result(status=100, msg=str(e))


@manager.route('/component/metrics', methods=['post'])
def component_metrics():
    request_data = request.json
    print(request_data)
    tracking = Tracking(job_id=request_data.get('job_id', ''), component_name=request_data.get('component_name', ''))
    metrics = tracking.get_metric_list()
    if metrics:
        return get_json_result(status=0, msg='success', data=metrics)
    else:
        return get_json_result(status=101, msg='error')


@manager.route('/component/metric_data', methods=['post'])
def component_metric_data():
    request_data = request.json
    tracking = Tracking(job_id=request_data.get('job_id', ''), component_name=request_data.get('component_name', ''))
    metric_data = tracking.read_metric_data(metric_namespace=request_data.get('metric_namespace', ''), metric_name=request_data.get('metric_name', ''))
    if metric_data:
        metric_data_list = [(metric.key, metric.value) for metric in metric_data]
        metric_data_list.sort(key=lambda x: x[0])
        return get_json_result(status=0, msg='success', data=metric_data_list)
    else:
        return get_json_result(status=101, msg='error')


@manager.route('/component/parameters', methods=['post'])
def component_parameters():
    request_data = request.json
    tracking = Tracking(job_id=request_data.get('job_id', ''), component_name=request_data.get('component_name', ''))
    metric_data = tracking.read_metric_data(metric_namespace=request_data.get('metric_namespace', ''), metric_name=request_data.get('metric_name', ''))
    if metric_data:
        metric_data_list = [(metric.key, metric.value) for metric in metric_data]
        metric_data_list.sort(key=lambda x: x[0])
        return get_json_result(status=0, msg='success', data=metric_data_list)
    else:
        return get_json_result(status=101, msg='error')
