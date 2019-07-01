#!/usr/bin/env python    
# -*- coding: utf-8 -*- 

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

# =============================================================================
# Transfer Variable Generator.py
# =============================================================================

import json
import os
import sys
from arch.api.utils import file_utils

header = ["#!/usr/bin/env python\n# -*- coding: utf-8 -*-\n",
          "################################################################################\n",
          "#\n# Copyright (c) 2018, WeBank Inc. All Rights Reserved\n#\n",
          "################################################################################\n",
          "# =============================================================================\n",
          "# TransferVariable Class\n# =============================================================================\n"]

base_class = 'class Variable(object):\n    def __init__(self, name, auth):\n        ' + \
             'self.name = name\n        self.auth = auth\n\nclass BaseTransferVariable(object):\n    ' + \
             'def __init__(self, flowid=0):\n        self.flowid = flowid\n        ' + \
             'self.define_transfer_variable()\n\n    def set_flowid(self, flowid):\n        ' + \
             'self.flowid = flowid\n\n    def generate_transferid(self, transfer_var, *suffix):\n        ' + \
             'if transfer_var.name.split(".", -1)[-1] not in self.__dict__:\n            ' + \
             'raise ValueError("transfer variable not in class, please check if!!!")\n\n        ' + \
             'transferid = transfer_var.name + "." + str(self.flowid)\n        if suffix:\n            ' + \
             'transferid += "." + ".".join(map(str, suffix))\n        return transferid\n\n' + \
             '    def define_transfer_variable(self):\n        pass\n'

import_var = "from federatedml.util.transfer_variable.base_transfer_variable import BaseTransferVariable, Variable"

class TransferVariableGenerator(object):
    def __init__(self):
        pass

    def write_base_class(self, writer):
        writer.write(base_class)

    def generate_base_class(self, out_path):
        fout = open(out_path, "w")
        for head in header:
            fout.write(head.strip() + "\n")

        fout.write("\n")
        fout.write("\n")

        self.write_base_class(fout)
        fout.close()

    def write_out_class(self, writer, class_name, transfer_var_dict, with_header = True):
        if with_header:
            global import_var
            writer.write("#!/usr/bin/env python" + "\n")
            writer.write("# -*- coding: utf-8 -*- " + "\n")
            writer.write(import_var + "\n")

        writer.write("\n\n")

        writer.write("class " + class_name + "(BaseTransferVariable):" + "\n")

        tag = '    '
        writer.write(tag + "def define_transfer_variable(self):" + "\n")

        for transfer_var, auth_dict in transfer_var_dict.items():
            writer.write(tag + tag)
            var_name = class_name + "." + transfer_var
            writer.write("self." + transfer_var + " = ")
            writer.write("Variable(name=" + '"' + var_name + '"' + ", ")
            writer.write("auth=" + "{'src': " + '"' + auth_dict["src"] + '"' + ", " + \
                         "'dst': " + str(auth_dict["dst"]) + "})")

            writer.write("\n")

        writer.write(tag + tag + "pass\n")
        writer.flush()

    def generate_all(self):
        base_dir = file_utils.get_project_base_directory()
        conf_dir = os.path.join(base_dir, "federatedml/transfer_variable_conf")
        merge_conf_path = os.path.join(conf_dir, "transfer_conf.json")
        trans_var_dir = os.path.join(base_dir, "federatedml/util/transfer_variable")
       
        merge_dict = {}
        with open(merge_conf_path, "w") as fin:
            pass

        base_class_path = os.path.join(trans_var_dir, "base_transfer_variable.py")
        self.generate_base_class(base_class_path)

        for conf in os.listdir(conf_dir):
            if not conf.endswith(".json"):
                continue

            if conf == "transfer_conf.json":
                continue
            
            with open(os.path.join(conf_dir, conf), "r") as fin:
                var_dict = json.loads(fin.read())
                merge_dict.update(var_dict)
       
            out_path = os.path.join(trans_var_dir, conf.split(".", -1)[0] + "_transfer_variable.py")
            fout = open(out_path, "w")
            with_header = True
            for class_name in var_dict:
                transfer_var_dict = var_dict[class_name]
                self.write_out_class(fout, class_name, transfer_var_dict, with_header)
                with_header = False

            fout.flush()
            fout.close()
        
        with open(merge_conf_path, "w") as fout:
            jsonDumpsIndentStr = json.dumps(merge_dict, indent=1);
            buffers = jsonDumpsIndentStr.split("\n", -1)
            for buf in buffers:
                fout.write(buf + "\n")

    def generate_transfer_var_class(self, transfer_var_conf_path, out_path):
        base_dir = file_utils.get_project_base_directory()
        merge_conf_path = os.path.join(base_dir, "federatedml/transfer_variable_conf/transfer_conf.json")

        merge_dict = {}
        if os.path.isfile(merge_conf_path):
            with open(merge_conf_path, "r") as fin:
                merge_dict = json.loads(fin.read())

        var_dict = {}
        with open(transfer_var_conf_path) as fin:
            var_dict = json.loads(fin.read())

        merge_dict.update(var_dict)

        with open(merge_conf_path, "w") as fout:
            jsonDumpsIndentStr = json.dumps(merge_dict, indent=1);
            buffers = jsonDumpsIndentStr.split("\n", -1)
            for buf in buffers:
                fout.write(buf + "\n")

        fout = open(out_path, "w")
        with_header = True
        for class_name in var_dict:
            transfer_var_dict = var_dict[class_name]
            self.write_out_class(fout, class_name, transfer_var_dict, with_header)
            with_header = False

        fout.flush()
        fout.close()


if __name__ == "__main__":

    conf_path = None
    out_path = None

    if len(sys.argv) == 2:
        out_path = sys.argv[1]
    elif len(sys.argv) == 3:
        conf_path = sys.argv[1]
        out_path = sys.argv[2]

    transfer_var_gen = TransferVariableGenerator()
    if conf_path is None and out_path is None:
        transfer_var_gen.generate_all()
    elif conf_path is None:
        transfer_var_gen.generate_base_class(out_path)
    else:
        transfer_var_gen.generate_transfer_var_class(conf_path, out_path)
