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

import math
import uuid

import numpy as np

from arch.api.eggroll import parallelize, table
from federatedml.ftl.eggroll_computation.util import eggroll_compute_vAvg_XY, eggroll_compute_hSum_XY, \
    eggroll_encrypt, eggroll_decrypt, eggroll_compute_XY, eggroll_compute_X_plus_Y
from federatedml.secureprotol.fate_paillier import PaillierEncryptedNumber


def prepare_table(matrix, batch_size=1, max_partition=20):
    """
    create table populated with input matrix
    :param matrix: 2D matrix
    :param batch_size: batch size for sample space
    :param max_partition max partition allowed
    :return:
    """
    m_length = len(matrix)
    n_batches = math.ceil(m_length / batch_size)
    partition = min(n_batches, max_partition)
    X = parallelize(matrix, partition=partition)
    return X


def create_empty_table(table_name, namespace, partition=1):
    return table(name=table_name, namespace=namespace, partition=partition)


def distribute_compute_sum_XY(X, Y):
    batch = 1
    XT = prepare_table(X, batch)
    YT = prepare_table(Y, batch)
    res = eggroll_compute_vAvg_XY(XT, YT, 1)

    destroy_table(XT)
    destroy_table(YT)
    return res


def distribute_compute_avg_XY(X, Y):
    length = len(X)
    batch = 1
    XT = prepare_table(X, batch)
    YT = prepare_table(Y, batch)
    res = eggroll_compute_vAvg_XY(XT, YT, length)

    destroy_table(XT)
    destroy_table(YT)
    return res


def distribute_compute_XY(X, Y):
    batch = 1
    XT = prepare_table(X, batch)
    YT = prepare_table(Y, batch)

    val = eggroll_compute_XY(XT, YT)

    result = []
    for i in range(len(val)):
        result.append(val[i])
    res = np.array(result)

    destroy_table(XT)
    destroy_table(YT)
    return res


def distribute_compute_XY_plus_Z(X, Y, Z):
    batch = 1
    XT = prepare_table(X, batch)
    YT = prepare_table(Y, batch)
    ZT = prepare_table(Z, batch)

    R = XT.join(YT, lambda x, y: y * x).join(ZT, lambda x, y: x + y)
    val = R.collect()
    val = dict(val)

    result = []
    for i in range(len(val)):
        result.append(val[i])
    res = np.array(result)

    destroy_table(XT)
    destroy_table(YT)
    destroy_table(ZT)
    return res


def distribute_compute_X_plus_Y(X, Y):
    batch = 1
    XT = prepare_table(X, batch)
    YT = prepare_table(Y, batch)

    val = eggroll_compute_X_plus_Y(XT, YT)

    result = []
    for i in range(len(val)):
        result.append(val[i])
    res = np.array(result)

    destroy_table(XT)
    destroy_table(YT)
    return res


def _convert_3d_to_2d_matrix(matrix):
    dim1, dim2, dim3 = matrix.shape
    ddim1 = dim1 * dim2
    matrix = matrix.reshape((ddim1, dim3))
    return matrix


def distribute_decrypt_matrix(private_key, matrix):
    _shape = matrix.shape
    if len(_shape) == 3:
        matrix = _convert_3d_to_2d_matrix(matrix)
    elif len(_shape) == 1:
        matrix = np.expand_dims(matrix, axis=1)

    X = prepare_table(matrix, batch_size=1)
    val = eggroll_decrypt(private_key, X)

    result = []
    last_index = len(val) - 1
    for i in range(last_index):
        result.append(val[i])

    if len(result) == 0:
        result = val[last_index]
    elif len(result[0]) == len(val[last_index]):
        result.append(val[last_index])
        result = np.array(result)
        result = result.reshape((result.shape[0] * result.shape[1], result.shape[-1]))
    else:
        result = np.array(result)
        result = result.reshape((result.shape[0] * result.shape[1], result.shape[-1]))
        result = np.vstack((result, val[last_index]))

    if len(_shape) == 3:
        result = result.reshape(_shape)
    elif len(_shape) == 1:
        result = np.squeeze(result, axis=1)

    destroy_table(X)
    return result


def restore_shape(component, shape, sign, batch_size=16, bit_width=8, pad_zero=3):
    num_ele = np.prod(shape)
    num_ele_w_pad = batch_size * len(component)

    un_batched_nums = np.zeros(num_ele_w_pad, dtype=int)

    for i in range(batch_size):
        filter = (pow(2, bit_width+pad_zero) - 1) << ((bit_width + pad_zero) * i)
        # filtered_nums = [x & filter for x in component]
        for j in len(component):
            un_batched_nums[batch_size*j + batch_size-1-i] = (filter & component[j].ciphertext(be_secure=False)) >> ((bit_width + pad_zero) * i)

    un_batched_nums = un_batched_nums[:num_ele]
    pk = component[0].public_key
    un_batched_nums_en = [PaillierEncryptedNumber(pk, x, 0) for x in un_batched_nums]
    re = np.reshape(un_batched_nums_en, shape)
    re = np.multiply(re, sign)
    return re


def distribute_decrypt_matrix_batch(private_key, matrix, batch_size=16, parallelism=8, bit_width=8, pad_zero=3):
    _shape = matrix.shape
    if len(_shape) == 3:
        matrix = _convert_3d_to_2d_matrix(matrix)
    elif len(_shape) == 1:
        matrix = np.expand_dims(matrix, axis=1)


    # X = prepare_table(matrix, batch_size=1)
    # val = eggroll_decrypt(private_key, X)
    # matrix = matrix.astype(np.int)

    A = np.reshape(matrix, (1, -1))
    A = np.squeeze(A)
    A = [x.ciphertext(be_secure=False) if x.exponent == 0 else \
             (x.decrease_exponent_to(0).ciphertext(be_secure=False) if x.exponent > 0 \
                  else x.increase_exponent_to(0).ciphertext(be_secure=False)) for x in A]

    A_len = len(A)
    # pad array at the end so tha the array is the size of
    A = A if (A_len % batch_size) == 0 \
        else np.pad(A, (0, batch_size - (A_len % batch_size)), 'constant', constant_values=(0, 0))

    idx_range = int(len(A) / batch_size)
    idx_base = list(range(idx_range))
    batched_nums = np.zeros(idx_range, dtype=int)
    for i in range(batch_size):
        idx_filter = [i + x * batch_size for x in idx_base]
        # print(idx_filter)
        filted_num = A[idx_filter]
        # print(filted_num)
        batched_nums += filted_num << ((bit_width + pad_zero) * i)
        # print(batched_nums)

    batched_nums = np.expand_dims(batched_nums, axis=1)

    X = prepare_table(batched_nums, 1)
    val = eggroll_decrypt(private_key, X)

    result = []
    last_index = len(val) - 1
    for i in range(last_index):
        result.append(val[i])

    if len(result) == 0:
        result = val[last_index]
    elif len(result[0]) == len(val[last_index]):
        result.append(val[last_index])
        result = np.array(result)
        result = result.reshape((result.shape[0] * result.shape[1], result.shape[-1]))
    else:
        result = np.array(result)
        result = result.reshape((result.shape[0] * result.shape[1], result.shape[-1]))
        result = np.vstack((result, val[last_index]))

    result = np.squeeze(result)
    result = restore_shape(result, _shape)

    # if len(_shape) == 3:
    #     result = result.reshape(_shape)
    # elif len(_shape) == 1:
    #     result = np.squeeze(result, axis=1)

    result = (result - 0.5) * 2

    destroy_table(X)
    return result


def distribute_encrypt_matrix(public_key, matrix):
    _shape = matrix.shape
    if len(_shape) == 3:
        matrix = _convert_3d_to_2d_matrix(matrix)
    elif len(_shape) == 1:
        matrix = np.expand_dims(matrix, axis=1)

    X = prepare_table(matrix, 1)
    val = eggroll_encrypt(public_key, X)

    result = []
    last_index = len(val) - 1
    for i in range(last_index):
        result.append(val[i])

    if len(result) == 0:
        result = val[last_index]
    elif len(result[0]) == len(val[last_index]):
        result.append(val[last_index])
        result = np.array(result)
        result = result.reshape((result.shape[0] * result.shape[1], result.shape[-1]))
    else:
        result = np.array(result)
        result = result.reshape((result.shape[0] * result.shape[1], result.shape[-1]))
        result = np.vstack((result, val[last_index]))

    if len(_shape) == 3:
        result = result.reshape(_shape)
    elif len(_shape) == 1:
        result = np.squeeze(result, axis=1)

    destroy_table(X)
    return result

def distribute_encrypt_matrix_batch(public_key, matrix, batch_size=16, parallelism=8, bit_width=8, pad_zero=3, r_max=1, r_min=0):
    _shape = matrix.shape
    _signs = np.sign(matrix)
    matrix = np.multiply(matrix, _signs)
    if len(_shape) == 3:
        matrix = _convert_3d_to_2d_matrix(matrix)
    elif len(_shape) == 1:
        matrix = np.expand_dims(matrix, axis=1)

    A = np.reshape(matrix, (1, -1))
    A = np.squeeze(A)

    A = (np.clip(A,r_max,r_min))



    A_len = len(A)
    # pad array at the end so tha the array is the size of
    A = A if (A_len % batch_size) == 0 \
        else np.pad(A, (0, batch_size - (A_len % batch_size)), 'constant', constant_values=(0, 0))

    # quantization step
    # A = (A * 255.0 + 0.5).astype(np.int)
    A = ((A - r_min) * (pow(2, bit_width) - 1.0) / (r_max - r_min)).astype(np.int)

    idx_range = int(len(A) / batch_size)
    idx_base = list(range(idx_range))
    batched_nums = np.zeros(idx_range, dtype=int)
    for i in range(batch_size):
        idx_filter = [i + x * batch_size for x in idx_base]
        # print(idx_filter)
        filted_num = A[idx_filter]
        # print(filted_num)
        batched_nums *= pow(2, bit_width + pad_zero)
        # batched_nums += (filted_num << ((bit_width + pad_zero) * i))
        batched_nums += filted_num
        # print(batched_nums)

    # batched_nums = batched_nums.tolist()
    # batched_nums = np.reshape(batched_nums, (parallelism, -1))
    batched_nums = np.expand_dims(batched_nums, axis=1)

    X = prepare_table(batched_nums, 1)
    val = eggroll_encrypt(public_key, X)

    result = []
    last_index = len(val) - 1
    for i in range(last_index):
        result.append(val[i])

    if len(result) == 0:
        result = val[last_index]
    elif len(result[0]) == len(val[last_index]):
        result.append(val[last_index])
        result = np.array(result)
        result = result.reshape((result.shape[0] * result.shape[1], result.shape[-1]))
    else:
        result = np.array(result)
        result = result.reshape((result.shape[0] * result.shape[1], result.shape[-1]))
        result = np.vstack((result, val[last_index]))

    # if len(_shape) == 3:
    #     result = result.reshape(_shape)
    # elif len(_shape) == 1:
    #     result = np.squeeze(result, axis=1)
    result = np.squeeze(result)


    destroy_table(X)
    return result, _shape, _signs


# def encrypt_matmul_2(X, Y, partition=20):
#
#     XT = create_empty_table(str(uuid.uuid1()), str(uuid.uuid1()), partition=partition)
#     YT = create_empty_table(str(uuid.uuid1()), str(uuid.uuid1()), partition=partition)
#
#     def data_generator(X, Y, is_X=True):
#         for m in range(len(X)):
#             for k in range(Y.shape[1]):
#                 key = str(m) + "_" + str(k)
#                 if is_X:
#                     yield (key, X[m])
#                 else:
#                     yield (key, Y[:, k])
#
#     XT_generator = data_generator(X, Y, is_X=True)
#     YT_generator = data_generator(X, Y, is_X=False)
#     XT.put_all(XT_generator)
#     YT.put_all(YT_generator)
#
#     dictionary = distribute_compute_hSum_XY(XT, YT)
#
#     res = [[0 for _ in range(Y.shape[1])] for _ in range(len(X))]
#     for m in range(len(X)):
#         row_list = []
#         for k in range(Y.shape[1]):
#             key = str(m) + "_" + str(k)
#             row_list.append(dictionary[key])
#         res[m] = row_list
#
#     return np.array(res)


def distribute_encrypt_matmul_3(X, Y, partition=20):
    assert X.shape[0] == Y.shape[0]

    XT = create_empty_table(str(uuid.uuid1()), str(uuid.uuid1()), partition=partition)
    YT = create_empty_table(str(uuid.uuid1()), str(uuid.uuid1()), partition=partition)

    def data_generator(X, Y, is_X=True):
        for i in range(X.shape[0]):
            for m in range(X.shape[1]):
                for k in range(Y.shape[-1]):
                    key = str(i) + "_" + str(m) + "_" + str(k)
                    if is_X:
                        yield (key, X[i, m, :])
                    else:
                        yield (key, Y[i, :, k])

    XT_generator = data_generator(X, Y, is_X=True)
    YT_generator = data_generator(X, Y, is_X=False)
    XT.put_all(XT_generator)
    YT.put_all(YT_generator)

    dictionary = eggroll_compute_hSum_XY(XT, YT)

    res = [[[0 for _ in range(Y.shape[-1])] for _ in range(X.shape[1])] for _ in range(X.shape[0])]
    for i in range(X.shape[0]):
        second_dim_list = []
        for m in range(X.shape[1]):
            third_dim_list = []
            for k in range(Y.shape[-1]):
                key = str(i) + "_" + str(m) + "_" + str(k)
                third_dim_list.append(dictionary[key])
            second_dim_list.append(third_dim_list)
        res[i] = second_dim_list

    result = np.array(res)

    destroy_table(XT)
    destroy_table(YT)
    return result


def distribute_encrypt_matmul_2_ob(X, Y, partition=20):
    # batch = 1
    # XT = prepare_table(X, batch, 1)
    # YT = prepare_table(Y, batch, 2)
    XT = create_empty_table(str(uuid.uuid1()), str(uuid.uuid1()), partition=partition)
    YT = create_empty_table(str(uuid.uuid1()), str(uuid.uuid1()), partition=partition)

    # print("encrypt_matmul_2_ob XT", XT)
    # print("encrypt_matmul_2_ob YT", YT)
    for m in range(len(X)):
        for k in range(Y.shape[1]):
            key = str(m) + "_" + str(k)
            XT.put(key, X[m])
            YT.put(key, Y[:, k])

    dictionary = eggroll_compute_hSum_XY(XT, YT)

    res = [[0 for _ in range(Y.shape[1])] for _ in range(len(X))]
    for m in range(len(X)):
        row_list = []
        for k in range(Y.shape[1]):
            key = str(m) + "_" + str(k)
            row_list.append(dictionary[key])
        res[m] = row_list

    result = np.array(res)

    destroy_table(XT)
    destroy_table(YT)
    return result


def distribute_encrypt_matmul_3_ob(X, Y, partition=20):
    assert X.shape[0] == Y.shape[0]

    XT = create_empty_table(str(uuid.uuid1()), str(uuid.uuid1()), partition=partition)
    YT = create_empty_table(str(uuid.uuid1()), str(uuid.uuid1()), partition=partition)

    for i in range(X.shape[0]):
        for m in range(X.shape[1]):
            for k in range(Y.shape[-1]):
                key = str(i) + "_" + str(m) + "_" + str(k)
                XT.put(key, X[i, m, :])
                YT.put(key, Y[i, :, k])

    dictionary = eggroll_compute_hSum_XY(XT, YT)

    res = [[[0 for _ in range(Y.shape[-1])] for _ in range(X.shape[1])] for _ in range(X.shape[0])]
    for i in range(X.shape[0]):
        second_dim_list = []
        for m in range(X.shape[1]):
            third_dim_list = []
            for k in range(Y.shape[-1]):
                key = str(i) + "_" + str(m) + "_" + str(k)
                third_dim_list.append(dictionary[key])
            second_dim_list.append(third_dim_list)
        res[i] = second_dim_list

    result = np.array(res)

    destroy_table(XT)
    destroy_table(YT)
    return result


def destroy_table(table):
    table.destroy()
