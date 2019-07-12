#!/usr/bin/env python
# -*- coding: utf-8 -*-
################################################################################
#
# Copyright (c) 2018, WeBank Inc. All Rights Reserved
#
################################################################################
# =============================================================================
# TransferVariable Class
# =============================================================================
from arch.api.utils import log_utils

LOGGER = log_utils.getLogger()

class Variable(object):
    def __init__(self, name, auth):
        self.name = name
        self.auth = auth

class BaseTransferVariable(object):
    def __init__(self, flowid=0):
        self.flowid = flowid
        self.define_transfer_variable()

    def set_flowid(self, flowid):
        self.flowid = flowid

    def set_taskid(self, taskid):
        self.taskid = taskid

    def generate_transferid(self, transfer_var, *suffix):
        if transfer_var.name.split(".", -1)[-1] not in self.__dict__:
            raise ValueError("transfer variable not in class, please check if!!!")

        transferid = transfer_var.name + "." + str(self.flowid) + "." + str(self.taskid)
        if suffix:
            transferid += "." + ".".join(map(str, suffix))
        LOGGER.debug("transferid is :{}, taskid is : {}".format(transferid, self.taskid))
        return transferid

    def define_transfer_variable(self):
        pass
