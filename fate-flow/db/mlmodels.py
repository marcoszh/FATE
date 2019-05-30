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
from arch.api.utils import log_utils
from peewee import CharField, IntegerField, BigIntegerField, DateTimeField, TextField, CompositeKey
from arch.task_manager.db.models import DB, DataBaseModel
import inspect
import sys

LOGGER = log_utils.getLogger()


@DB.connection_context()
def init_tables():
    members = inspect.getmembers(sys.modules[__name__], inspect.isclass)
    table_objs = []
    for name, obj in members:
        if obj != DataBaseModel and issubclass(obj, DataBaseModel):
            table_objs.append(obj)
    DB.create_tables(table_objs)


class Job(DataBaseModel):
    f_job_id = CharField(max_length=100)
    f_name = CharField(max_length=500, index=True)
    f_description = TextField(null=True)
    f_tag = CharField(max_length=50, null=True, index=True)
    f_role = CharField(max_length=50, index=True)
    f_party_id = CharField(max_length=50, index=True)
    f_roles = TextField()
    f_initiator_party_id = CharField(max_length=50, index=True)
    f_dsl = TextField()
    f_config = TextField()
    f_status = CharField(max_length=50, default='ready')  # waiting/ready/start/running/success/failed/partial/setFailed
    f_current_steps = CharField(max_length=500, null=True)  # record component id in DSL
    f_current_tasks = CharField(max_length=500, null=True)  # record task id
    f_progress = IntegerField(default=0)
    f_create_time = BigIntegerField(default=0)
    f_update_time = BigIntegerField(default=0)
    f_start_time = BigIntegerField(default=0)
    f_end_time = BigIntegerField(default=0)
    f_elapsed = BigIntegerField(default=-1)

    class Meta:
        db_table = "t_job"
        primary_key = CompositeKey('f_job_id', 'f_role', 'f_party_id')  # Compatible with standalone version


class Task(DataBaseModel):
    f_task_id = CharField(max_length=100, primary_key=True)
    f_job_id = CharField(max_length=100)
    f_component_name = TextField()
    f_operator = CharField(max_length=100)
    f_run_ip = CharField(max_length=100)
    f_run_pid = IntegerField()
    f_status = CharField(max_length=50, default='ready')  # waiting/ready/start/running/success/failed/partial/setFailed
    f_create_time = BigIntegerField(default=0)
    f_update_time = BigIntegerField(default=0)
    f_start_time = BigIntegerField(default=0)
    f_end_time = BigIntegerField(default=0)
    f_elapsed = BigIntegerField(default=-1)

    class Meta:
        db_table = "t_task"


class MachineLearningModelMeta(DataBaseModel):
    f_id = BigIntegerField(primary_key=True)
    f_role = CharField(max_length=50, index=True)
    f_party_id = CharField(max_length=50, index=True)
    f_roles = TextField()
    f_job_id = CharField(max_length=100)
    f_model_id = CharField(max_length=500, index=True)
    f_model_version = CharField(max_length=500, index=True)
    f_size = BigIntegerField(default=0)
    f_create_time = BigIntegerField(default=0)
    f_update_time = BigIntegerField(default=0)
    f_description = TextField(null=True)
    f_tag = CharField(max_length=50, null=True, index=True)

    class Meta:
        db_table = "t_machine_learning_model_meta"


init_tables()
