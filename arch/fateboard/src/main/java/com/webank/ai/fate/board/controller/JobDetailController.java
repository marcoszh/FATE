package com.webank.ai.fate.board.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.webank.ai.fate.board.global.ErrorCode;
import com.webank.ai.fate.board.global.ResponseResult;
import com.webank.ai.fate.board.services.TaskManagerService;
import com.webank.ai.fate.board.utils.Dict;
import com.webank.ai.fate.board.utils.HttpClientPool;
import com.webank.ai.fate.board.utils.ReadJson;
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


/**
 * @Description TODO
 **/


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

        logger.info("input parameter for getMetaInfo：" + param);

        JSONObject jsonObject = JSON.parseObject(param);
        String job_id = jsonObject.getString("job_id");
        String component_name = jsonObject.getString("component_name");
        if (job_id == null || "".equals(job_id) || component_name == null || "".equals(component_name)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters！");

        }

        String result = httpClientPool.post(fateUrl + "/v1/tracking/component/metrics", param);

        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
        }

        JSONObject resultObject = JSON.parseObject(result);
        Integer retcode = resultObject.getInteger("retcode");

        if (retcode == null) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "parameter not exist!");
        }
        if (retcode == 0) {

            Object data = resultObject.get("data");

            return new ResponseResult<>(ErrorCode.SUCCESS, data);

        } else {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "errorcode: " + retcode);
        }


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
        logger.info("index parameters：" + param);

        JSONObject jsonObject = JSON.parseObject(param);
        String job_id = jsonObject.getString("job_id");
        String component_name = jsonObject.getString("component_name");
        String metric_namespace = jsonObject.getString("metric_namespace");
        String metric_name = jsonObject.getString("metric_name");

        if (job_id == null || "".equals(job_id) || component_name == null || "".equals(component_name)
                || metric_namespace == null || "".equals(metric_namespace) || metric_name == null || "".equals(metric_name)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters!");

        }

        String result = httpClientPool.post(fateUrl + Dict.URL_COPONENT_METRIC_DATA, param);

        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
        }

        JSONObject resultObject = JSON.parseObject(result);
        Integer retcode = resultObject.getInteger("retcode");

        if (retcode == null) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "parameter not exist!");
        }
        if (retcode == 0) {

            return new ResponseResult<>(ErrorCode.SUCCESS, resultObject);

        } else {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "errorcode: " + retcode);
        }
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
        logger.info("parameters：" + param);

        JSONObject jsonObject = JSON.parseObject(param);
        String job_id = jsonObject.getString("job_id");
        String component_name = jsonObject.getString("component_name");
        if (job_id == null || "".equals(job_id) || component_name == null || "".equals(component_name)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters！");
        }
        String result = httpClientPool.post(fateUrl + Dict.URL_COPONENT_PARAMETERS, param);
        logger.info("result: " + result);
        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
        }


        JSONObject resultObject = JSON.parseObject(result);
        Integer retcode = resultObject.getInteger("retcode");

        if (retcode == null) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "parameter not exist!");
        }
        if (retcode == 0) {

            Object data = resultObject.get("data");

            return new ResponseResult<>(ErrorCode.SUCCESS, data);

        } else {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "errorcode: " + retcode);
        }

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
        logger.info("dag dependencies：" + param);

        JSONObject jsonObject = JSON.parseObject(param);

        String job_id = jsonObject.getString("job_id");
        if (job_id == null || "".equals(job_id)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters！");

        }


        String result = httpClientPool.post(fateUrl + Dict.URL_DAG_DEPENDENCY, param);


        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
        }


        JSONObject resultObject = JSON.parseObject(result);
        Integer retcode = resultObject.getInteger("retcode");
        if (retcode == null) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "parameter not exist!");
        }


        if (retcode == 0) {

            JSONObject data = resultObject.getJSONObject("data");

            JSONArray components_list = data.getJSONArray("component_list");
            ArrayList<Map> componentList = new ArrayList<>();

            for (Object o : components_list) {
                HashMap<String, String> component = new HashMap<>();
                component.put("componentName", (String) o);

                String taskStatus = taskManagerService.findTaskStatus(job_id, (String) o);
                component.put("status", taskStatus);
                componentList.add(component);
            }
            data.remove("component_list");
            data.put("componentList", componentList);


            return new ResponseResult<>(ErrorCode.SUCCESS, data);

        } else {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "errorcode: " + retcode);
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
        logger.info("parameters for model output：" + param);

        JSONObject jsonObject = JSON.parseObject(param);
        String job_id = jsonObject.getString("job_id");
        String component_name = jsonObject.getString("component_name");
        if (job_id == null || "".equals(job_id) || component_name == null || "".equals(component_name)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters！");
        }

        String result = httpClientPool.post(fateUrl + Dict.URL_OUTPUT_MODEL, param);


        logger.info("result: " + result);
        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
        }

        JSONObject resultObject = JSON.parseObject(result);
//        Integer retcode = resultObject.getInteger("retcode");
//        if (retcode == null) {
//            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "parameter not exist!");
//        }
//        if (retcode == 0) {


            return new ResponseResult<>(ErrorCode.SUCCESS, resultObject);

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
        logger.info("parameters for data output：" + param);

        JSONObject jsonObject = JSON.parseObject(param);
        String job_id = jsonObject.getString("job_id");
        String component_name = jsonObject.getString("component_name");


        if (job_id == null || "".equals(job_id) || component_name == null || "".equals(component_name)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters！");
        }


        String result = httpClientPool.post(fateUrl + Dict.URL_OUTPUT_DATA, param);

        logger.info(result);
        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
        }


        JSONObject resultObject = JSON.parseObject(result);
        Integer retcode = resultObject.getInteger("retcode");
        if (retcode == null) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "parameter not exist!");
        }
        if (retcode == 0) {
            return new ResponseResult<>(ErrorCode.SUCCESS, resultObject);
        } else {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "errorcode: " + retcode);
        }


    }


}
