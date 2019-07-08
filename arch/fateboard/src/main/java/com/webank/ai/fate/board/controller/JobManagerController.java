package com.webank.ai.fate.board.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.webank.ai.fate.board.global.ErrorCode;
import com.webank.ai.fate.board.global.ResponseResult;
import com.webank.ai.fate.board.pojo.Job;
import com.webank.ai.fate.board.pojo.JobWithBLOBs;
import com.webank.ai.fate.board.services.JobManagerService;
import com.webank.ai.fate.board.utils.Dict;
import com.webank.ai.fate.board.utils.HttpClientPool;
import com.webank.ai.fate.board.utils.PageBean;
import com.webank.ai.fate.board.utils.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/job")
public class JobManagerController {
    private final Logger logger = LoggerFactory.getLogger(JobManagerController.class);

    @Autowired
    JobManagerService jobManagerService;

    @Autowired
    HttpClientPool httpClientPool;

    @Value("${fate.url}")
    String fateUrl;


    ExecutorService executorService = Executors.newFixedThreadPool(20);

    /**
     * query status of jobs
     *
     * @return
     */
    @RequestMapping(value = "/query/status", method = RequestMethod.GET)
    public ResponseResult queryJobStatus() {
        List<Job> jobs = jobManagerService.queryJobStatus();
//        if (jobs.size() == 0) {
//            return new ResponseResult<>(ErrorCode.SUCCESS, "There is no job on running or waiting!");
//        }
        return new ResponseResult<>(ErrorCode.SUCCESS, jobs);
    }

    /**
     * kill job
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/v1/pipeline/job/stop", method = RequestMethod.POST)
    public ResponseResult stopJob(@RequestBody String param) {

        JSONObject jsonObject = JSON.parseObject(param);
        String jobId = jsonObject.getString(Dict.JOBID);
        if ( StringUtils.isEmpty(jobId)) {
//            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters!");
            return new ResponseResult<>(ErrorCode.INCOMING_PARAM_ERROR);

        }
        String result =  httpClientPool.post(fateUrl+Dict.URL_JOB_STOP,param);

        return  ResponseUtil.buildResponse(result,null);

    }

    /**
     * query dataset according to job_id
     *
     * @return
     */
    @RequestMapping(value = "/tracking/job/data_view", method = RequestMethod.POST)
    public ResponseResult queryJobDataset(@RequestBody String param) {

        JSONObject jsonObject = JSON.parseObject(param);
        String jobId = jsonObject.getString(Dict.JOBID);
        if (StringUtils.isEmpty(jobId)) {
//            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters!");
            ResponseResult<Object> objectResponseResult = new ResponseResult<>(ErrorCode.INCOMING_PARAM_ERROR);
        }
        String result = httpClientPool.post(fateUrl + Dict.URL_JOB_DATAVIEW, param);

//        if (result == null || "".equals(result)) {
//            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
//        }

        return  ResponseUtil.buildResponse(result,Dict.DATA);


//        JSONObject resultObject = JSON.parseObject(result);
//
//        Integer retcode = resultObject.getInteger(Dict.RETCODE);
//
//        Object data = resultObject.get(Dict.DATA);
//
//        return new ResponseResult<>(retcode, data);


    }

    /**
     * query job according to jobId
     *
     * @return
     */
    @RequestMapping(value = "/query/{jobId}", method = RequestMethod.GET)
    public ResponseResult queryJobById(@PathVariable("jobId") String jobId) {
        HashMap<String, Object> resultMap = new HashMap<>();
        JobWithBLOBs jobWithBLOBs = jobManagerService.queryJobByFJobId(jobId);
        if (jobWithBLOBs == null) {
//            return new ResponseResult<String>(ErrorCode.PARAM_ERROR, "Job not exist!");
            return new ResponseResult<>(ErrorCode.INCOMING_PARAM_ERROR);

        }
        Map  params = Maps.newHashMap();
        params.put(Dict.JOBID,jobId);
        String result = httpClientPool.post(fateUrl + Dict.URL_JOB_DATAVIEW, JSON.toJSONString(params));

//        if (result == null || "".equals(result)) {
//            return new ResponseResult<>(ErrorCode.SUCCESS, resultMap);
//        }
        JSONObject data = JSON.parseObject(result).getJSONObject(Dict.DATA);
        resultMap.put(Dict.JOB, jobWithBLOBs);
        resultMap.put(Dict.DATASET, data);
        return new ResponseResult<>(ErrorCode.SUCCESS, resultMap);
    }


    /**
     * query all jobs
     *
     * @return
     */
    @RequestMapping(value = "/query/totalrecord", method = RequestMethod.GET)
    public ResponseResult queryTotalRecord() {
        long count = jobManagerService.count();
        return new ResponseResult<>(ErrorCode.SUCCESS, count);
    }

    @RequestMapping(value = "/query/all/{totalRecord}/{pageNum}/{pageSize}", method = RequestMethod.GET)

    public ResponseResult queryJob(@PathVariable(value = "totalRecord") long totalRecord, @PathVariable(value = "pageNum") long pageNum, @PathVariable(value = "pageSize") long pageSize) {

        PageBean<Map> listPageBean = new PageBean<>(pageNum, pageSize, totalRecord);

        long startIndex = listPageBean.getStartIndex();
        List<JobWithBLOBs> jobWithBLOBsList = jobManagerService.queryJobByPage(startIndex, pageSize);

        ArrayList<Map> jobList = new ArrayList<>();

        Map<JobWithBLOBs, Future> jobDataMap = new LinkedHashMap<>();

        for (JobWithBLOBs jobWithBLOBs : jobWithBLOBsList) {

            Future feature = executorService.submit(new Callable<JSONObject>() {

                @Override
                public JSONObject call() throws Exception {
                    Map param = Maps.newHashMap();
                    String jobId = jobWithBLOBs.getfJobId();
                    param.put(Dict.JOBID,jobId);
                    String result = httpClientPool.post(fateUrl + Dict.URL_JOB_DATAVIEW, JSON.toJSONString(param));
                    JSONObject data = JSON.parseObject(result).getJSONObject(Dict.DATA);
                    return data;
                }
            });
            jobDataMap.put(jobWithBLOBs, feature);
        }

        jobDataMap.forEach((k, v) -> {
            try {
                HashMap<String, Object> stringObjectHashMap = new HashMap<>();
                stringObjectHashMap.put(Dict.JOB, k);
                jobList.add(stringObjectHashMap);
                stringObjectHashMap.put(Dict.DATASET, v.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        });
        listPageBean.setList(jobList);

        return new ResponseResult<>(ErrorCode.SUCCESS, listPageBean);
    }


    /**
     *
     * @return
     */
    @RequestMapping(value = "/query/all", method = RequestMethod.GET)
    public ResponseResult queryJob() {


        ArrayList<Map> jobList = new ArrayList<>();

        List<JobWithBLOBs> jobWithBLOBsList = jobManagerService.queryJob();

        if (jobWithBLOBsList.size() == 0) {
//            return new ResponseResult<String>(ErrorCode.SUCCESS, "Job not exist!");
            return new ResponseResult<>(ErrorCode.INCOMING_PARAM_ERROR);
        }

        Map<JobWithBLOBs,Future>  jobDataMap = new HashMap<JobWithBLOBs,Future>(16);

        for (JobWithBLOBs jobWithBLOBs : jobWithBLOBsList) {

            Future feature = executorService.submit(new Callable<JSONObject>() {

                @Override
                public JSONObject  call() throws Exception {
                    String jobId = jobWithBLOBs.getfJobId();
                    Map  params =Maps.newHashMap();
                    params.put(Dict.JOBID,jobId);
                    String result = httpClientPool.post(fateUrl +Dict.URL_JOB_DATAVIEW, JSON.toJSONString(params));
                    JSONObject data = JSON.parseObject(result).getJSONObject(Dict.DATA);
                    return data;
                }
            });
            jobDataMap.put(jobWithBLOBs,feature);
        }

        jobDataMap.forEach((k,v)->{
            try {
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put(Dict.JOB, k);
            jobList.add(stringObjectHashMap);
            stringObjectHashMap.put(Dict.DATASET, v.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        });

        return new ResponseResult<>(ErrorCode.SUCCESS, jobList);
    }
}
