import request from '@/utils/request'

// 查询所有Job
export function getAllJobs(params) {
  return request({
    url: '/job/query/all',
    method: 'get',
    params
  })
}

// 查询所有JobStatus(比上一个接口返回少，上一个是后台复合接口，多返回partner，dataset等信息)
export function getAllJobsStatus(params) {
  return request({
    url: '/job/query/status',
    method: 'get',
    params
  })
}

// 根据jobid关闭job
export function killJob(jobId) {
  return request({
    url: '/job/v1/pipeline/job/stop',
    method: 'post',
    data: { 'job_id': jobId }
  })
}

// 根据jobid查询job详细信息
export function getJobDetails(jobId) {
  return request({
    url: `/job/query/${jobId}`,
    method: 'get'
  })
}

// 根据jobid查询DAG依赖(获取component_name，以及依赖关系绘制DAG图）
export function getDAGDpencencies(jobId) {
  return request({
    url: '/v1/pipeline/dag/dependencies',
    method: 'post',
    data: { job_id: jobId }
  })
}

// 根据jobid和componentid查询组件参数
export function getComponentPara(data) {
  return request({
    url: '/v1/tracking/component/parameters',
    method: 'post',
    data
  })
}

// 根据jobid和componentid查询模型输出
export function getModelOutput(data) {
  return request({
    url: '/v1/tracking/component/output/model',
    method: 'post',
    data
  })
}

// 查询日志
export function queryLog({ componentId, jobId, begin, end }) {
  return request({
    url: `/queryLogWithSize/${componentId}/${jobId}/${begin}/${end}  `,
    method: 'get'
  })
}

