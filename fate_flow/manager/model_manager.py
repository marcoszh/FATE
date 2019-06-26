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

from arch.api.proto.model_meta_pb2 import ModelMeta
from arch.api.proto.model_param_pb2 import ModelParam
from arch.api.proto.data_transform_server_pb2 import DataTransformServer
from arch.api.utils.core import json_loads, json_dumps, bytes_to_string, string_to_bytes
from arch.api.utils.format_transform import camel_to_pascal
from arch.api import eggroll
from arch.api import RuntimeInstance
from arch.api import WorkMode
from fate_flow.manager import version_control
import datetime
from arch.api.proto.data_io_meta_pb2 import DataIOMeta
from arch.api.proto.data_io_param_pb2 import DataIOParam
from arch.api.proto.data_io_meta_pb2 import ImputerMeta
from arch.api.proto.data_io_param_pb2 import ImputerParam
from arch.api.proto.data_io_meta_pb2 import OutlierMeta
from arch.api.proto.data_io_param_pb2 import OutlierParam


def save_model(model_key, model_buffers, model_version, model_id, version_log=None):
    data_table = eggroll.table(name=model_version, namespace=model_id, partition=get_model_table_partition_count(),
                               create_if_missing=True, error_if_exist=False)
    for buffer_name in model_buffers:
        model_buffers[buffer_name] = bytes_to_string(model_buffers[buffer_name].SerializeToString())
    data_table.put(model_key, json_dumps(model_buffers, byte=True), use_serialize=False)
    version_log = "[AUTO] save model at %s." % datetime.datetime.now() if not version_log else version_log
    version_control.save_version(name=model_version, namespace=model_id, version_log=version_log)


def read_model(model_key, model_version, model_id):
    data_table = eggroll.table(name=model_version, namespace=model_id, partition=get_model_table_partition_count(),
                               create_if_missing=False, error_if_exist=False)
    if data_table:
        model_buffers = data_table.get(model_key, use_serialize=False)
        if model_buffers:
            model_buffers = json_loads(model_buffers)
            for buffer_name in model_buffers:
                print(buffer_name)
                buffer_object = buffer_name()
                buffer_object.ParseFromString(string_to_bytes(model_buffers[buffer_name]))
                model_buffers[buffer_name] = buffer_object
            return model_buffers
        else:
            return {}
    else:
        return {}


def get_model_table_partition_count():
    # todo: max size limit?
    return 4 if RuntimeInstance.MODE == WorkMode.CLUSTER else 1


def test_model(role):
    with open("%s_runtime_conf.json" % role) as conf_fr:
        runtime_conf = json_loads(conf_fr.read())

    model_table_name = runtime_conf.get("WorkFlowParam").get("model_table")
    model_table_namespace = runtime_conf.get("WorkFlowParam").get("model_namespace")
    print(model_table_name, model_table_namespace)
    model_meta_save = ModelMeta()
    model_meta_save.name = "HeteroLR%s" % (camel_to_pascal(role))
    save_model("model_meta", model_meta_save, model_version=model_table_name, model_id=model_table_namespace)

    model_meta_read = ModelMeta()
    read_model("model_meta", model_meta_read, model_version=model_table_name, model_id=model_table_namespace)
    print(model_meta_read)

    model_param_save = ModelParam()
    model_param_save.weight["k1"] = 1
    model_param_save.weight["k2"] = 2
    save_model("model_param", model_param_save, model_version=model_table_name, model_id=model_table_namespace)

    # read
    model_param_read = ModelParam()
    read_model("model_param", model_param_read, model_version=model_table_name, model_id=model_table_namespace)
    print(model_param_read)

    data_transform = DataTransformServer()
    data_transform.missing_replace_method = "xxxx"
    save_model("data_transform", data_transform, model_version=model_table_name, model_id=model_table_namespace)

if __name__ == '__main__':
    import uuid
    job_id = str(uuid.uuid1().hex)
    eggroll.init(job_id=job_id, mode=0)

    test_model("guest")
    test_model("host")

    print(job_id)
