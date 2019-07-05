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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        logger.info("start job query!");

        List<Job> jobs = jobManagerService.queryJobStatus();

        logger.info("results for job query：" + jobs);

        if (jobs.size() == 0) {
            return new ResponseResult<>(ErrorCode.SUCCESS, "There is no job on running or waiting!");
        }
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
        logger.info("input parameters for killing job：" + param);


        JSONObject jsonObject = JSON.parseObject(param);
        Object job_id = jsonObject.get("job_id");
        if ((job_id == null) || "".equals(job_id)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters!");

        }

        String result =  httpClientPool.post(fateUrl+"/v1/pipeline/job/stop",param);

       // String result = "{    \"retcode\": 0,    \"retmsg\": \"OK\"}";

        logger.info("result for killing job：" + result);
        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
        }

        JSONObject resultObject = JSON.parseObject(result);
        Integer retcode = resultObject.getInteger("retcode");
        if (retcode == 0) {

            return new ResponseResult<>(ErrorCode.SUCCESS);

        } else {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "errorcode: " + retcode);
        }

    }

    /**
     * query dataset according to job_id
     *
     * @return
     */
    @RequestMapping(value = "/tracking/job/data_view", method = RequestMethod.POST)
    public ResponseResult queryJobDataset(@RequestBody String param) {

        logger.info("parameters for querying dataset：" + param);

        JSONObject jsonObject = JSON.parseObject(param);
        Object job_id = jsonObject.get("job_id");
        if ((job_id == null) || "".equals(job_id)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters!");
        }
        String result = httpClientPool.post(fateUrl + Dict.URL_JOB_DATAVIEW, param);

        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
        }

        JSONObject resultObject = JSON.parseObject(result);

        Integer retcode = resultObject.getInteger("retcode");
        if (retcode == 0) {
            Object data = resultObject.get("data");

            return new ResponseResult<>(ErrorCode.SUCCESS, data);

        } else {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "errorcode: " + retcode);
        }
    }

    /**
     * query job according to jobId
     *
     * @return
     */
    @RequestMapping(value = "/query/{jobId}", method = RequestMethod.GET)
    public ResponseResult queryJobById(@PathVariable("jobId") String jobId) {

        logger.info("jobId：" + jobId);

        HashMap<String, Object> resultMap = new HashMap<>();

        JobWithBLOBs jobWithBLOBs = jobManagerService.queryJobByFJobId(jobId);
        logger.info("jobWithBLOBs：" + jobWithBLOBs);

        if (jobWithBLOBs == null) {
            return new ResponseResult<String>(ErrorCode.PARAM_ERROR, "Job not exist!");
        }

        Map  params = Maps.newHashMap();
        params.put("job_id",jobId);
        String result = httpClientPool.post(fateUrl + Dict.URL_JOB_DATAVIEW, JSON.toJSONString(params));

        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.SUCCESS, resultMap);
        }

        JSONObject data = JSON.parseObject(result).getJSONObject("data");

        logger.info("data：" + data);

        resultMap.put("job", jobWithBLOBs);
        resultMap.put("dataset", data);

        logger.info("stringObjectHashMap：" + resultMap);

        return new ResponseResult<>(ErrorCode.SUCCESS, resultMap);
    }


    /**
     * query all jobs
     *
     * @return
     */
    @RequestMapping(value = "/query/totalrecord", method = RequestMethod.GET)
    public ResponseResult queryTotalRecord() {
        logger.info("Start querying totalRecord!");

        long count = jobManagerService.count();
        return new ResponseResult<>(ErrorCode.SUCCESS, count);
    }

    @RequestMapping(value = "/query/all/{totalRecord}/{pageNum}/{pageSize}", method = RequestMethod.GET)

    public ResponseResult queryJob(@PathVariable(value = "totalRecord") long totalRecord, @PathVariable(value = "pageNum") long pageNum, @PathVariable(value = "pageSize") long pageSize) {

        logger.info("Start querying jobs:totalRecord={}, pageNum={},pageSize={}.",totalRecord, pageNum, pageSize);

        PageBean<Map> listPageBean = new PageBean<>(pageNum, pageSize, totalRecord);
        System.out.println(listPageBean);

        long startIndex = listPageBean.getStartIndex();
        List<JobWithBLOBs> jobWithBLOBsList = jobManagerService.queryJobByPage(startIndex, pageSize);

        ArrayList<Map> jobList = new ArrayList<>();

        Map<JobWithBLOBs, Future> jobDataMap = new HashMap<JobWithBLOBs, Future>(16);

        for (JobWithBLOBs jobWithBLOBs : jobWithBLOBsList) {

            Future feature = executorService.submit(new Callable<JSONObject>() {

                @Override
                public JSONObject call() throws Exception {
                    String jobId = jobWithBLOBs.getfJobId();
                    String result = httpClientPool.post(fateUrl + "/tracking/job/data_view", jobId);
                    logger.info("http result for data_view:" + result);

                    JSONObject data = JSON.parseObject(result).getJSONObject("data");
                    return data;
                }
            });
            jobDataMap.put(jobWithBLOBs, feature);


        }

        jobDataMap.forEach((k, v) -> {
            try {
                HashMap<String, Object> stringObjectHashMap = new HashMap<>();
                stringObjectHashMap.put("job", k);
                jobList.add(stringObjectHashMap);
                stringObjectHashMap.put("dataset", v.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        });


        logger.info("jobList：" + jobList);
        listPageBean.setList(jobList);
        System.out.println("111111111111");
        logger.info("11111111111111"+listPageBean.toString());

        return new ResponseResult<>(ErrorCode.SUCCESS, listPageBean);
    }


    /**
     *
     * @return
     */
    @RequestMapping(value = "/query/all", method = RequestMethod.GET)
    public ResponseResult queryJob() {

        logger.info("Start querying all jobs!");
        ArrayList<Map> jobList = new ArrayList<>();

        List<JobWithBLOBs> jobWithBLOBsList = jobManagerService.queryJob();
        logger.info("jobWithBLOBsList：" + jobWithBLOBsList);

        if (jobWithBLOBsList.size() == 0) {
            return new ResponseResult<String>(ErrorCode.SUCCESS, "Job not exist!");
        }


        Map<JobWithBLOBs,Future>  jobDataMap = new HashMap<JobWithBLOBs,Future>(16);




        for (JobWithBLOBs jobWithBLOBs : jobWithBLOBsList) {

            Future feature = executorService.submit(new Callable<JSONObject>() {

                @Override
                public JSONObject  call() throws Exception {
                    String jobId = jobWithBLOBs.getfJobId();
                    Map  params =Maps.newHashMap();
                    params.put("job_id",jobId);
                    String result = httpClientPool.post(fateUrl +Dict.URL_JOB_DATAVIEW, JSON.toJSONString(params));
                    JSONObject data = JSON.parseObject(result).getJSONObject("data");
                    return data;
                }
            });
            jobDataMap.put(jobWithBLOBs,feature);
        }

        jobDataMap.forEach((k,v)->{
            try {
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("job", k);
            jobList.add(stringObjectHashMap);
            stringObjectHashMap.put("dataset", v.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        });

        logger.info("jobList：" + jobList);

        return new ResponseResult<>(ErrorCode.SUCCESS, jobList);
    }
}
