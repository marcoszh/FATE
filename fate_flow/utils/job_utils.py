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
from arch.api.utils import file_utils
from arch.api.utils.core import json_loads
import subprocess
import os
import uuid
from fate_flow.settings import stat_logger
from fate_flow.db.db_models import DB, Job, Task
from fate_flow.manager.queue_manager import JOB_QUEUE
import errno
from arch.api import eggroll
import datetime
import json
import threading
from fate_flow.driver.dsl_parser import DSLParser


class IdCounter:
    _lock = threading.RLock()

    def __init__(self, initial_value=0):
        self._value = initial_value

    def incr(self, delta=1):
        '''
        Increment the counter with locking
        '''
        with IdCounter._lock:
            self._value += delta
            return self._value


id_counter = IdCounter()


def generate_job_id():
    return '_'.join([datetime.datetime.now().strftime("%Y%m%d%H%M%S%f"), str(id_counter.incr())])


def get_job_directory(job_id):
    return os.path.join(file_utils.get_project_base_directory(), 'jobs', job_id)


def get_job_log_directory(job_id):
    return os.path.join(file_utils.get_project_base_directory(), 'logs', job_id)


def new_runtime_conf(job_dir, method, module, role, party_id):
    if role:
        conf_path_dir = os.path.join(job_dir, method, module, role, str(party_id))
    else:
        conf_path_dir = os.path.join(job_dir, method, module, str(party_id))
    os.makedirs(conf_path_dir, exist_ok=True)
    return os.path.join(conf_path_dir, 'runtime_conf.json')


def save_job_conf(job_id, job_dsl, job_runtime_conf):
    job_dsl_path, job_runtime_conf_path = get_job_conf_path(job_id=job_id)
    os.makedirs(os.path.dirname(job_dsl_path), exist_ok=True)
    for data, conf_path in [(job_dsl, job_dsl_path), (job_runtime_conf, job_runtime_conf_path)]:
        with open(conf_path, 'w+') as f:
            f.truncate()
            f.write(json.dumps(data, indent=4))
            f.flush()
    return job_dsl_path, job_runtime_conf_path


def get_job_conf_path(job_id):
    job_dir = get_job_directory(job_id)
    job_dsl_path = os.path.join(job_dir, 'job_dsl.json')
    job_runtime_conf_path = os.path.join(job_dir, 'job_runtime_conf.json')
    return job_dsl_path, job_runtime_conf_path


@DB.connection_context()
def get_job_dsl_parser_by_job_id(job_id):
    jobs = Job.select(Job.f_dsl, Job.f_runtime_conf).where(Job.f_job_id == job_id)
    if jobs:
        job_dsl_path, job_runtime_conf_path = get_job_conf_path(job_id=job_id)
        job_dsl_parser = get_job_dsl_parser(job_dsl_path=job_dsl_path, job_runtime_conf_path=job_runtime_conf_path)
        return job_dsl_parser
    else:
        return None


def get_job_dsl_parser(job_dsl_path, job_runtime_conf_path):
    dsl_parser = DSLParser()
    default_runtime_conf_path = os.path.join(file_utils.get_project_base_directory(),
                                             *['federatedml', 'conf', 'default_runtime_conf'])
    setting_conf_path = os.path.join(file_utils.get_project_base_directory(), *['federatedml', 'conf', 'setting_conf'])
    dsl_parser.run(dsl_json_path=job_dsl_path,
                   runtime_conf=job_runtime_conf_path,
                   default_runtime_conf_prefix=default_runtime_conf_path,
                   setting_conf_prefix=setting_conf_path)
    return dsl_parser


@DB.connection_context()
def get_job_runtime_conf(job_id, role, party_id):
    jobs = Job.select(Job.f_runtime_conf).where(Job.f_job_id == job_id, Job.f_role == role, Job.f_party_id == party_id)
    if jobs:
        job = jobs[0]
        return json_loads(job.f_runtime_conf)
    else:
        return {}


@DB.connection_context()
def set_job_failed(job_id, role, party_id):
    sql = Job.update(f_status='failed').where(Job.f_job_id == job_id,
                                              Job.f_role == role,
                                              Job.f_party_id == party_id,
                                              Job.f_status != 'success')
    return sql.execute() > 0


@DB.connection_context()
def query_job_by_id(job_id):
    jobs = Job.select().where(Job.f_job_id == job_id)
    return [job for job in jobs]


@DB.connection_context()
def job_queue_size():
    return JOB_QUEUE.qsize()


@DB.connection_context()
def show_job_queue():
    # TODO
    pass


@DB.connection_context()
def running_job_amount():
    return Job.select().where(Job.f_status == "running").distinct().count()


def clean_job(job_id):
    try:
        stat_logger.info('ready clean job {}'.format(job_id))
        eggroll.cleanup('*', namespace=job_id, persistent=False)
        stat_logger.info('send clean job {}'.format(job_id))
    except Exception as e:
        stat_logger.exception(e)


@DB.connection_context()
def query_tasks(job_id, task_id, role=None, party_id=None):
    if role and party_id:
        tasks = Task.select().where(Task.f_job_id == job_id, Task.f_task_id == task_id, Task.f_role == role,
                                    Task.f_party_id == party_id)
    else:
        tasks = Task.select().where(Task.f_job_id == job_id, Task.f_task_id == task_id)
    return tasks


def gen_status_id():
    return uuid.uuid1().hex


def check_job_process(pid):
    if pid < 0:
        return False
    if pid == 0:
        raise ValueError('invalid PID 0')
    try:
        os.kill(pid, 0)
    except OSError as err:
        if err.errno == errno.ESRCH:
            # ESRCH == No such process
            return False
        elif err.errno == errno.EPERM:
            # EPERM clearly means there's a process to deny access to
            return True
        else:
            # According to "man 2 kill" possible error values are
            # (EINVAL, EPERM, ESRCH)
            raise
    else:
        return True


def run_subprocess(config_dir, process_cmd, log_dir=None):
    stat_logger.info('Starting process command: {}'.format(process_cmd))
    stat_logger.info(' '.join(process_cmd))

    os.makedirs(config_dir, exist_ok=True)
    if log_dir:
        os.makedirs(log_dir, exist_ok=True)
    std_log = open(os.path.join(log_dir if log_dir else config_dir, 'std.log'), 'w')
    pid_path = os.path.join(config_dir, 'pid')

    if os.name == 'nt':
        startupinfo = subprocess.STARTUPINFO()
        startupinfo.dwFlags |= subprocess.STARTF_USESHOWWINDOW
        startupinfo.wShowWindow = subprocess.SW_HIDE
    else:
        startupinfo = None
    p = subprocess.Popen(process_cmd,
                         stdout=std_log,
                         stderr=std_log,
                         startupinfo=startupinfo
                         )
    with open(pid_path, 'w') as f:
        f.truncate()
        f.write(str(p.pid) + "\n")
        f.flush()
    return p


def gen_all_party_key(all_party):
    """
    Join all party as party key
    :param all_party:
        "role": {
            "guest": [9999],
            "host": [10000],
            "arbiter": [10000]
         }
    :return:
    """
    if not all_party:
        all_party_key = 'all'
    elif isinstance(all_party, dict):
        sorted_role_name = sorted(all_party.keys())
        all_party_key = '#'.join([
            ('%s-%s' % (
                role_name,
                '_'.join([str(p) for p in sorted(set(all_party[role_name]))]))
             )
            for role_name in sorted_role_name])
    else:
        all_party_key = None
    return all_party_key
