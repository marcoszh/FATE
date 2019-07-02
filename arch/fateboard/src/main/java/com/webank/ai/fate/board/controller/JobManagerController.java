package com.webank.ai.fate.board.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.webank.ai.fate.board.global.ErrorCode;
import com.webank.ai.fate.board.global.ResponseResult;
import com.webank.ai.fate.board.pojo.Job;
import com.webank.ai.fate.board.pojo.JobWithBLOBs;
import com.webank.ai.fate.board.services.JobManagerService;
import com.webank.ai.fate.board.utils.HttpClientPool;
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
        String result = httpClientPool.post(fateUrl + "/tracking/job/data_view", param);




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


        JobWithBLOBs jobWithBLOBs = jobManagerService.queryJobByFJobId(jobId);
        logger.info("jobWithBLOBs：" + jobWithBLOBs);

        if (jobWithBLOBs == null) {
            return new ResponseResult<String>(ErrorCode.PARAM_ERROR, "Job not exist!");
        }

        String result = httpClientPool.post(fateUrl + "/tracking/job/data_view", jobId);

        logger.info("result for dataset：" + result);

        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
        }

        JSONObject data = JSON.parseObject(result).getJSONObject("data");

        logger.info("data：" + data);


        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("job", jobWithBLOBs);
        stringObjectHashMap.put("dataset", data);

        logger.info("stringObjectHashMap：" + stringObjectHashMap);

        return new ResponseResult<>(ErrorCode.SUCCESS, stringObjectHashMap);
    }


    /**
     * query all jobs
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
                    String result = httpClientPool.post(fateUrl + "/tracking/job/data_view", jobId);
                    JSONObject data = JSON.parseObject(result).getJSONObject("data");
                    return data;
                }
            });
            jobDataMap.put(jobWithBLOBs,feature);

//            String jobId = jobWithBLOBs.getfJobId();
//            String result = httpClientPool.post(fateUrl + "/tracking/job/data_view", jobId);
//
//            logger.info("result for dataset：" + result);
//
//            if (result == null || "".equals(result)) {
//                return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
//            }



//            logger.info("data：" + data);
//            if (data == null) {
//                return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Data not exist!");
//            }





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
