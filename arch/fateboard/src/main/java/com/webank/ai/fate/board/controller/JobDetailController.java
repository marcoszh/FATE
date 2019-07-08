package com.webank.ai.fate.board.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.webank.ai.fate.board.global.ErrorCode;
import com.webank.ai.fate.board.global.ResponseResult;
import com.webank.ai.fate.board.services.TaskManagerService;
import com.webank.ai.fate.board.utils.Dict;
import com.webank.ai.fate.board.utils.HttpClientPool;
import com.webank.ai.fate.board.utils.ReadJson;
import com.webank.ai.fate.board.utils.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;




@Controller
@RequestMapping(value = "/v1")
public class JobDetailController {

    private final Logger logger = LoggerFactory.getLogger(JobDetailController.class);

    @Autowired
    HttpClientPool httpClientPool;
    @Autowired
    TaskManagerService taskManagerService;

    @Value("${fate.url}")
    String fateUrl;


    /**
     * get running parameters
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/tracking/component/metrics", method = RequestMethod.POST)
    public ResponseResult getMetaInfo(@RequestBody String param) {
        JSONObject jsonObject = JSON.parseObject(param);
        String jobId = jsonObject.getString(Dict.JOBID);
        String componentName = jsonObject.getString(Dict.COMPONENT_NAME);

        Preconditions.checkArgument(StringUtils.isNoneEmpty(jobId,componentName));
        String result = httpClientPool.post(fateUrl + Dict.URL_COPONENT_METRIC, param);


        return  ResponseUtil.buildResponse(result,Dict.DATA);

//        JSONObject resultObject = JSON.parseObject(result);
//
//        Integer retcode = resultObject.getInteger(Dict.RETCODE);
//
//        JSONObject data = resultObject.getJSONObject(Dict.DATA);
//
//        String  msg  = data.getString(Dict.REMOTE_RETURN_MSG);
//
//        return new ResponseResult<>(retcode,msg, data);

    }

    ;

    /**
     * get index parameters
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/tracking/component/metric_data", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult getMetricInfo(@RequestBody String param) {

        JSONObject jsonObject = JSON.parseObject(param);
        String jobId = jsonObject.getString(Dict.JOBID);
        String componentName = jsonObject.getString(Dict.COMPONENT_NAME);
        String metricNamespace = jsonObject.getString(Dict.METRIC_NAMESPACE);
        String metricName = jsonObject.getString(Dict.METRIC_NAME);
        Preconditions.checkArgument(StringUtils.isNoneEmpty(jobId,componentName,metricName,metricNamespace));

        String result = httpClientPool.post(fateUrl + Dict.URL_COPONENT_METRIC_DATA, param);

        return  ResponseUtil.buildResponse(result,null);


//        JSONObject resultObject = JSON.parseObject(result);
//
//        Integer retcode = resultObject.getInteger(Dict.RETCODE);
//
//        return new ResponseResult<>(retcode, resultObject);
    }

    /**
     * get graph/table parameters
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/tracking/component/parameters", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult getDetailInfo(@RequestBody String param) {


        JSONObject jsonObject = JSON.parseObject(param);
        String jobId = jsonObject.getString(Dict.JOBID);
        String componentName = jsonObject.getString(Dict.COMPONENT_NAME);
        Preconditions.checkArgument(StringUtils.isNoneEmpty(jobId,componentName));

        String result = httpClientPool.post(fateUrl + Dict.URL_COPONENT_PARAMETERS, param);

        return  ResponseUtil.buildResponse(result,Dict.DATA);

//        JSONObject resultObject = JSON.parseObject(result);
//        Integer retcode = resultObject.getInteger(Dict.RETCODE);
//
////        if (retcode == null) {
////            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "parameter not exist!");
////        }
////        if (retcode == 0) {
//
//            Object data = resultObject.get(Dict.DATA);
//
//            return new ResponseResult<>(retcode, data);
//
////        } else {
////            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "errorcode: " + retcode);
////        }

    }


    /**
     * get dag dependencies
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/pipeline/dag/dependencies", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult getDagDependencies(@RequestBody String param) {


        JSONObject jsonObject = JSON.parseObject(param);

        String jobId = jsonObject.getString(Dict.JOBID);
        Preconditions.checkArgument(StringUtils.isNotEmpty(jobId));
//        if (jobId == null || "".equals(jobId)) {
//            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters！");
//        }

        String result = httpClientPool.post(fateUrl + Dict.URL_DAG_DEPENDENCY, param);

        JSONObject resultObject = JSON.parseObject(result);
        Integer retcode = resultObject.getInteger(Dict.RETCODE);

        if (retcode == 0) {

            JSONObject data = resultObject.getJSONObject(Dict.DATA);

            JSONArray components_list = data.getJSONArray(Dict.COMPONENT_LIST);
            ArrayList<Map> componentList = new ArrayList<>();

            for (Object o : components_list) {
                HashMap<String, String> component = new HashMap<>();
                component.put(Dict.COMPONENT_NAME, (String) o);

                String taskStatus = taskManagerService.findTaskStatus(jobId, (String) o);
                component.put(Dict.STATUS, taskStatus);
                componentList.add(component);
            }
            data.put(Dict.COMPONENT_LIST, componentList);


            return new ResponseResult<>(ErrorCode.SUCCESS, data);

        } else {
            return new ResponseResult<>(retcode, resultObject);
        }



    }

    /**
     * model output
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/tracking/component/output/model", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult getModel(@RequestBody String param) {


        JSONObject jsonObject = JSON.parseObject(param);
        String jobId = jsonObject.getString(Dict.JOBID);
        String componentName = jsonObject.getString(Dict.COMPONENT_NAME);

        Preconditions.checkArgument(StringUtils.isNoneEmpty(jobId,componentName));
//        if (job_id == null || "".equals(job_id) || component_name == null || "".equals(component_name)) {
//            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters！");
//        }

        String result = httpClientPool.post(fateUrl + Dict.URL_OUTPUT_MODEL, param);



//        if (result == null || "".equals(result)) {
//            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
//        }

        return  ResponseUtil.buildResponse(result,null);


//        JSONObject resultObject = JSON.parseObject(result);
//        Integer retcode = resultObject.getInteger(Dict.RETCODE);
//        return new ResponseResult<>(retcode, resultObject);

//        } else {
//            return new ResponseResult<>(ErrorCode.SYSTEM_ERROR, "errorcode: " + retcode);
//        }

    }

    /**
     * data output
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/tracking/component/output/data", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult getData(@RequestBody String param) {
        JSONObject jsonObject = JSON.parseObject(param);
        String jobId = jsonObject.getString(Dict.JOBID);
        String componentName = jsonObject.getString(Dict.COMPONENT_NAME);
        Preconditions.checkArgument(StringUtils.isNoneEmpty(jobId,componentName));
//        if (StringUtils.isEmpty(jobId)) || String) {
//            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters！");
//        }

        String result = httpClientPool.post(fateUrl + Dict.URL_OUTPUT_DATA, param);
//        if (result == null || "".equals(result)) {
//            return new ResponseResult<>(ErrorCode.SYSTEM_ERROR, "Network Error!");
//        }
        return  ResponseUtil.buildResponse(result,null);


//        JSONObject resultObject = JSON.parseObject(result);
//        Integer retcode = resultObject.getInteger(Dict.RETCODE);
//        return new ResponseResult<>(retcode, resultObject);

    }


}
