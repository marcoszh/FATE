package com.webank.ai.fate.board.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.webank.ai.fate.board.global.ErrorCode;
import com.webank.ai.fate.board.global.ResponseResult;
import com.webank.ai.fate.board.services.TaskManagerService;
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

    @Value("${fate.url:dddddddd}")
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

        //假数据
        String result = null;
        if (component_name.contains("intersection")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"train\": [\n" +
                    "            \"intersection\"\n" +
                    "        ]\n" +
                    "    },\n" +

                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";

        }
        if (component_name.contains("boost") || component_name.contains("lr")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"TRAIN\": [\n" +
                    "            \"LOSS0\",\n" +
                    "            \"RECALL\"\n" +
                    "        ],\n" +
                    "        \"VALIDATION\": [\n" +
                    "            \"LOSS0\"\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }

        if (component_name.contains("dataio")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"DataIO\": [\n" +
                    "            \"missing_value_ratio\",\n" +
                    "            \"missing_value_list\",\n" +
                    "            \"outlier_value_ratio\",\n" +
                    "            \"outlier_value_list\"\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }
        if (component_name.contains("evaluation")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"train\": [\n" +
                    "            \"hetero_lr\",\n" +
                    "            \"hetero_lr_ks_tpr\",\n" +
                    "            \"hetero_lr_ks_fpr\",\n" +
                    "            \"hetero_lr_roc\"\n" +
                    "            \"hetero_lr_lift\"\n" +
                    "            \"hetero_lr_gain\"\n" +
                    "            \"hetero_lr_precision_recall\"\n" +
                    "            \"hetero_lr_accuracy\"\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }
       /* if(component_name.equals("scale")){
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"scale\": [\n" +
                    "            \"scaletest\",\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }*/
        if (component_name.contains("sample")) {
            Random random = new Random();
            int i = random.nextInt(10);

            if ((i > 5)) {
                result = "{\n" +
                        "    \"data\": {\n" +
                        "        \"random\": [\n" +
                        "            \"sample_count\"]\n" +
                        "    },\n" +
                        "    \"retcode\": 0,\n" +
                        "    \"retmsg\": \"OK\"\n" +
                        "}";
            } else {
                result = "{\n" +
                        "    \"data\": {\n" +
                        "        \"stratified\": [\n" +
                        "            \"sample_count\"]\n" +
                        "    },\n" +
                        "    \"retcode\": 0,\n" +
                        "    \"retmsg\": \"OK\"\n" +
                        "}";
            }
        }
            /*result = "{\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\",\n" +
                    "    \"data\":null\n" +
                    "}";*/


//        String result =  httpClientPool.post(fateUrl+"/v1/tracking/component/metrics",param);

        logger.info("result: " + result);

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

//        String result =  httpClientPool.post(fateUrl+" /v1/tracking/component/metric_data",param);
        //假数据
        String result = null;

        if (metric_name.equals("intersection")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"intersection_ratio\": 0.5,\n" +
                    "        \"intersection_count\": 30000\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"module_name\": \"field\",\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"ok\"\n" +
                    "}";

        }

        if (metric_name.contains("LOSS")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"1\": \"12.619519306167089\",\n" +
                    "        \"10\": \"0.37002806145702466\",\n" +
                    "        \"11\": \"0.3666836724512943\",\n" +
                    "        \"12\": \"0.3636923745574634\",\n" +
                    "        \"13\": \"0.36098255949001756\",\n" +
                    "        \"14\": \"0.35850780636124974\",\n" +
                    "        \"15\": \"0.35623307237622576\",\n" +
                    "        \"16\": \"0.3541322654947004\",\n" +
                    "        \"17\": \"0.3521847108316513\",\n" +
                    "        \"18\": \"0.35037369962658227\",\n" +
                    "        \"19\": \"0.34868529119618996\",\n" +
                    "        \"2\": \"42.84409198249153\",\n" +
                    "        \"20\": \"0.34710764127968863\",\n" +
                    "        \"3\": \"5.7818485491613805\",\n" +
                    "        \"4\": \"0.8931268616432674\",\n" +
                    "        \"5\": \"0.4419725176662893\",\n" +
                    "        \"6\": \"0.39430649347146746\",\n" +
                    "        \"7\": \"0.38385683832758316\",\n" +
                    "        \"8\": \"0.37827563215167875\",\n" +
                    "        \"9\": \"0.37382005419175035\"\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"BEST\": 0.4,\n" +
                    "        \"metric_type\": \"LOSS\",\n" +
                    "        \"unit_name\": \"iter num\"\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"ok\"\n" +
                    "}";
        }
        if (metric_name.equals("hetero_lr_accuracy") && metric_namespace.equals("train")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"0.01\": 0.02,\n" +
                    "        \"0.02\": 0.03,\n" +
                    "        \"0.03\": 0.04,\n" +
                    "        \"0.6\": 0.044\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"metric_type\": \"Accuracy\",\n" +
                    "        \"unit_name\": \"threshold\",\n" +
                    "        \"curve_name\": \"hetero_lr\"\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }
        if (metric_name.equals("hetero_lr_precision_recall") && metric_namespace.equals("train")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"0.01\": 0.02,\n" +
                    "        \"0.02\": 0.03,\n" +
                    "        \"0.03\": 0.04,\n" +
                    "        \"0.6\": 0.044\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"metric_type\": \"Precision-Recall\",\n" +
                    "        \"unit_name\": \"Recall\",\n" +
                    "        \"ordinate_name\": \"Precision\",\n" +
                    "        \"curve_name\": \"hetero_lr\",\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }
        if (metric_name.equals("hetero_lr_gain") && metric_namespace.equals("train")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"0.01\": 0.02,\n" +
                    "        \"0.02\": 0.03,\n" +
                    "        \"0.03\": 0.04,\n" +
                    "        \"0.6\": 0.044\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"metric_type\": \"GAIN\",\n" +
                    "        \"unit_name\": \"threshold\",\n" +
                    "        \"curve_name\": \"hetero_lr\"\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }
        if (metric_name.equals("hetero_lr_lift") && metric_namespace.equals("train")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"0.01\": 0.02,\n" +
                    "        \"0.02\": 0.03,\n" +
                    "        \"0.03\": 0.04,\n" +
                    "        \"0.6\": 0.044\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"metric_type\": \"LEFT\",\n" +
                    "        \"unit_name\": \"threshold\",\n" +
                    "        \"curve_name\": \"hetero_lr\"\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }
        if (metric_name.equals("hetero_lr_roc") && metric_namespace.equals("train")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"0.01\": 0.02,\n" +
                    "        \"0.02\": 0.03,\n" +
                    "        \"0.03\": 0.04,\n" +
                    "        \"0.6\": 0.044\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"metric_type\": \"ROC\",\n" +
                    "        \"unit_name\": \"fpr\",\n" +
                    "        \"ordinate_name\": \"tpr\",\n" +
                    "        \"curve_name\": \"hetero_lr\"\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }


        if (metric_name.equals("hetero_lr_ks_fpr") && metric_namespace.equals("train")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"0.01\": 0.02,\n" +
                    "        \"0.02\": 0.03,\n" +
                    "        \"0.03\": 0.04,\n" +
                    "        \"0.6\": 0.044\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"curve_name\": \"hetero_lr_fpr\",\n" +
                    "        \"metric_type\": \"KS\",\n" +
                    "        \"unit_name\": \"threshold\"\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }
        if (metric_name.equals("hetero_lr_ks_tpr") && metric_namespace.equals("train")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"0.01\": 0.02,\n" +
                    "        \"0.02\": 0.03,\n" +
                    "        \"0.03\": 0.04,\n" +
                    "        \"0.6\": 0.044\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"curve_name\": \"hetero_lr_tpr\",\n" +
                    "        \"metric_type\": \"KS\",\n" +
                    "        \"unit_name\": \"threshold\"\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }
        if (metric_name.equals("hetero_lr") && metric_namespace.equals("train")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"auc\": 0.8\n" +
                    "    },\n" +
                    "    \"metric_type\": \"auc\",\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }
        if (metric_name.equals("sample_count") && metric_namespace.equals("random")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"count\": 10000\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"module_name\": \"field\",\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"ok\"\n" +
                    "}";
        }
        if (metric_name.equals("sample_count") && metric_namespace.equals("stratified"))
            result = result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"0\": 1000\n" +
                    "        \"1\": 2000\n" +
                    "        \"2\": 5000\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"module_name\": \"stratified\",\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"ok\"\n" +
                    "}";


        if (metric_name.equals("missing_value_ratio")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"0\": 0.5,\n" +
                    "        \"1\": 0.2,\n" +
                    "        \"2\": 0.3\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"module_name\": \"field\",\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"ok\"\n" +
                    "}";

        }
        if (metric_name.equals("missing_value_list")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"na\": 1,\n" +
                    "        \"none\": 0,\n" +
                    "        \"null\": 2\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"module_name\": \"field\",\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"ok\"\n" +
                    "}";
        }
        if (metric_name.equals("outlier_value_ratio")) {

            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"0\": 0.5,\n" +
                    "        \"1\": 0.2,\n" +
                    "        \"2\": 0.3\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"module_name\": \"field\",\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"ok\"\n" +
                    "}";
        }
        if (metric_name.equals("outlier_value_list")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"na\": 1,\n" +
                    "        \"none\": 0,\n" +
                    "        \"null\": 2\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"module_name\": \"field\",\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"ok\"\n" +
                    "}";
        }

         /*   result = " {\n" +
                    "    \"data\": {\n" +
                    "        \"1\": \"12.619519306167089\",\n" +
                    "        \"10\": \"0.37002806145702466\",\n" +
                    "        \"11\": \"0.3666836724512943\",\n" +
                    "        \"12\": \"0.3636923745574634\",\n" +
                    "        \"13\": \"0.36098255949001756\",\n" +
                    "        \"14\": \"0.35850780636124974\",\n" +
                    "        \"15\": \"0.35623307237622576\",\n" +
                    "        \"16\": \"0.3541322654947004\",\n" +
                    "        \"17\": \"0.3521847108316513\",\n" +
                    "        \"18\": \"0.35037369962658227\",\n" +
                    "        \"19\": \"0.34868529119618996\",\n" +
                    "        \"2\": \"42.84409198249153\",\n" +
                    "        \"20\": \"0.34710764127968863\",\n" +
                    "        \"3\": \"5.7818485491613805\",\n" +
                    "        \"4\": \"0.8931268616432674\",\n" +
                    "        \"5\": \"0.4419725176662893\",\n" +
                    "        \"6\": \"0.39430649347146746\",\n" +
                    "        \"7\": \"0.38385683832758316\",\n" +
                    "        \"8\": \"0.37827563215167875\",\n" +
                    "        \"9\": \"0.37382005419175035\"\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"BEST\": 0.4,\n" +
                    "        \"metric_type\": \"OTHERS\",\n" +
                    "        \"unit_name\": \"iter num\"\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"ok\"\n" +
                    "}";*/


        logger.info("result: " + result);
        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
        }

        JSONObject resultObject = JSON.parseObject(result);
        Integer retcode = resultObject.getInteger("retcode");
        if (retcode == 0) {

            return new ResponseResult<>(ErrorCode.SUCCESS, resultObject);

        } else {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "errorcode: " + retcode);
        }
    }

    /**
     * get parameters
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


//        String result =  httpClientPool.post(fateUrl+"/v1/tracking/component/parameters",param);
        String result = "{\n" +
                "    \"retcode\": 0,\n" +
                "    \"retmsg\": \"OK\",\n" +
                "    \"data\": {\n" +
                "        \"max_iter\": 10,\n" +
                "        \"batch_size\": 100,\n" +
                "        \"learning_rate\": 0.01\n" +
                "    }\n" +
                "}\n";

        logger.info("result: " + result);
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

//        String result =  httpClientPool.post(fateUrl+"/v1/pipeline/dag/dependencies",param);
//        String result = "{\n" +
//                "    \"retcode\": 0,\n" +
//                "    \"retmsg\": \"OK\",\n" +
//                "    \"data\": {\n" +
//                "        \"dependencies\": {\n" +
//                "            \"feature_scale_0\":[\"secure_boost_0\"],\n" +
//                "            \"sample_0\":[\"feature_scale_0\"],\n" +
//                "            \"homo_lr_0\":[\"sample_0\", \"feature_scale_0\"],\n" +
//                "            \"evaluation_0\":[\"homo_lr_0\"]\n" +
//                "        },\n" +
//                "       \"components_list\":[\"secure_boost_0\",\"feature_scale_0\",\"sample_0\",\"homo_lr_0\", \"evaluation_0\"]\n" +
//                "    }\n" +
//                "}\n";
        String result = "{\n" +
                "    \"retcode\": 0,\n" +
                "    \"retmsg\": \"OK\",\n" +
                "    \"data\": {\n" +
                "        \"dependencies\": {\n" +
                "            \"dataio_0\":[\"secure_boost_0\"],\n" +
                "            \"federated_sample_0\":[\"dataio_0\"],\n" +
                "            \"intersection_0\":[\"federated_sample\"],\n" +
                "            \"scale_0\":[\"intersection_0\"],\n" +
                "            \"hetero_feature_binning_0\":[\"scale_0\"],\n" +
                "            \"feature_scale_0\":[\"hetero_feature_binning_0\"],\n" +
                "            \"hetero_feature_selection_0\":[\"feature_scale_0\"],\n" +
                "            \"one_hot_0\":[\"hetero_feature_selection_0\"],\n" +
                "            \"homo_lr_0\":[\"one_hot_0\"],\n" +
                "            \"evaluation_0\":[\"homo_lr_0\"]\n" +
                "        },\n" +
                "       \"components_list\":[\"secure_boost_0\",\"dataio_0\",\"federated_sample_0\"," +
                "\"intersection_0\",\"scale_0\",\"hetero_feature_binning_0\",\"feature_scale_0\",\"hetero_feature_selection_0\"," +
                "\"one_hot_0\", \"homo_lr_0\",\"evaluation_0\"]\n" +
                "    }\n" +
                "}\n";

        logger.info("result: " + result);

        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
        }


        JSONObject resultObject = JSON.parseObject(result);
        Integer retcode = resultObject.getInteger("retcode");


        if (retcode == 0) {

            JSONObject data = resultObject.getJSONObject("data");

            JSONArray components_list = data.getJSONArray("components_list");
            ArrayList<Map> componentList = new ArrayList<>();
            for (Object o : components_list) {
                HashMap<String, String> component = new HashMap<>();
                component.put("componentName", (String) o);

                String taskStatus = taskManagerService.findTaskStatus(job_id, (String) o);
                component.put("status", taskStatus);
                componentList.add(component);
            }
            data.remove("components_list");
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

//        String result =  httpClientPool.post(fateUrl+"/v1/pipeline/dag/dependencies",param);
        //假数据
        String result = null;
        if (component_name.contains("boost")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"featureImportances\": [\n" +
                    "            {\n" +
                    "                \"fid\": 2,\n" +
                    "                \"importance\": 321.8889916459333,\n" +
                    "                \"sitename\": \"guest\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"fid\": 7,\n" +
                    "                \"importance\": 55.82974847483065,\n" +
                    "                \"sitename\": \"guest\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"fid\": 1,\n" +
                    "                \"importance\": 21.026937651633173,\n" +
                    "                \"sitename\": \"guest\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"fid\": 4,\n" +
                    "                \"importance\": 14.37449979694914,\n" +
                    "                \"sitename\": \"host.0\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"fid\": 1,\n" +
                    "                \"importance\": 12.217549424799014,\n" +
                    "                \"sitename\": \"host.0\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"fid\": 12,\n" +
                    "                \"importance\": 8.921498722713366,\n" +
                    "                \"sitename\": \"host.0\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"fid\": 7,\n" +
                    "                \"importance\": 6.136037881558039,\n" +
                    "                \"sitename\": \"host.0\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"fid\": 5,\n" +
                    "                \"importance\": 4.46207932913752,\n" +
                    "                \"sitename\": \"host.0\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"importance\": 3.0856409460928433,\n" +
                    "                \"sitename\": \"host.0\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"fid\": 2,\n" +
                    "                \"importance\": 2.721774193548388,\n" +
                    "                \"sitename\": \"host.0\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"fid\": 9,\n" +
                    "                \"importance\": 1.713508600333796,\n" +
                    "                \"sitename\": \"host.0\"\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"initScore\": [\n" +
                    "            0\n" +
                    "        ],\n" +
                    "        \"losses\": [\n" +
                    "            0.6045349992658311,\n" +
                    "            0.5317964726526309\n" +
                    "        ],\n" +
                    "        \"treeNum\": 2,\n" +
                    "        \"trees\": [\n" +
                    "            {\n" +
                    "                \"splitMaskdict\": {\n" +
                    "                    \"0\": 0.18844908000000016,\n" +
                    "                    \"1\": 0.29706784000000014,\n" +
                    "                    \"4\": 0.10092687999999989\n" +
                    "                },\n" +
                    "                \"tree\": [\n" +
                    "                    {\n" +
                    "                        \"fid\": 2,\n" +
                    "                        \"leftNodeid\": 1,\n" +
                    "                        \"rightNodeid\": 2,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 0.4781997187060479\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 7,\n" +
                    "                        \"id\": 1,\n" +
                    "                        \"leftNodeid\": 3,\n" +
                    "                        \"rightNodeid\": 4,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.56153050672182\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 1,\n" +
                    "                        \"id\": 2,\n" +
                    "                        \"leftNodeid\": 5,\n" +
                    "                        \"rightNodeid\": 6,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": -1.8161925601750546\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 12,\n" +
                    "                        \"id\": 3,\n" +
                    "                        \"leftNodeid\": 7,\n" +
                    "                        \"rightNodeid\": 8,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": 1.8757467144563917\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 1,\n" +
                    "                        \"id\": 4,\n" +
                    "                        \"leftNodeid\": 9,\n" +
                    "                        \"rightNodeid\": 10,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": -0.4545454545454546\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 1,\n" +
                    "                        \"id\": 5,\n" +
                    "                        \"leftNodeid\": 11,\n" +
                    "                        \"rightNodeid\": 12,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": 0.9090909090909091\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 4,\n" +
                    "                        \"id\": 6,\n" +
                    "                        \"leftNodeid\": 13,\n" +
                    "                        \"rightNodeid\": 14,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": -1.9450800915331807\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 7,\n" +
                    "                        \"leftNodeid\": 15,\n" +
                    "                        \"rightNodeid\": 16,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": 1.922141119221411\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 4,\n" +
                    "                        \"id\": 8,\n" +
                    "                        \"leftNodeid\": 17,\n" +
                    "                        \"rightNodeid\": 18,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": -0.5882352941176471\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 2,\n" +
                    "                        \"id\": 9,\n" +
                    "                        \"leftNodeid\": 19,\n" +
                    "                        \"rightNodeid\": 20,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": 0.9677419354838709\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 7,\n" +
                    "                        \"id\": 10,\n" +
                    "                        \"leftNodeid\": 21,\n" +
                    "                        \"rightNodeid\": 22,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": -1.6666666666666665\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 11,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": -1.4285714285714286\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 12,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.7647058823529411\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 13,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.4285714285714286\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 14,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": -1.9907407407407407\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 15,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.9458128078817734\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 16,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 17,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.4285714285714286\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 18,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": -1.6666666666666667\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 19,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.875\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 20,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 21,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.4285714285714286\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 22,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": -1.9402985074626866\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"splitMaskdict\": {\n" +
                    "                    \"0\": 0.18844908000000016,\n" +
                    "                    \"1\": 0.29706784000000014,\n" +
                    "                    \"4\": 0.10092687999999989\n" +
                    "                },\n" +
                    "                \"tree\": [\n" +
                    "                    {\n" +
                    "                        \"fid\": 2,\n" +
                    "                        \"leftNodeid\": 1,\n" +
                    "                        \"rightNodeid\": 2,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 0.4348962721726942\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 7,\n" +
                    "                        \"id\": 1,\n" +
                    "                        \"leftNodeid\": 3,\n" +
                    "                        \"rightNodeid\": 4,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.419133885198914\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 4,\n" +
                    "                        \"id\": 2,\n" +
                    "                        \"leftNodeid\": 5,\n" +
                    "                        \"rightNodeid\": 6,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": -1.6511684163420053\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 12,\n" +
                    "                        \"id\": 3,\n" +
                    "                        \"leftNodeid\": 7,\n" +
                    "                        \"rightNodeid\": 8,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": 1.704880330197874\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 1,\n" +
                    "                        \"id\": 4,\n" +
                    "                        \"leftNodeid\": 9,\n" +
                    "                        \"rightNodeid\": 10,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": -0.4099717081214952\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 5,\n" +
                    "                        \"id\": 5,\n" +
                    "                        \"leftNodeid\": 11,\n" +
                    "                        \"rightNodeid\": 12,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": 0.8476271289391625\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 1,\n" +
                    "                        \"id\": 6,\n" +
                    "                        \"leftNodeid\": 13,\n" +
                    "                        \"rightNodeid\": 14,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": -1.769691347271527\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 9,\n" +
                    "                        \"id\": 7,\n" +
                    "                        \"leftNodeid\": 15,\n" +
                    "                        \"rightNodeid\": 16,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": 1.7471974548010207\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 4,\n" +
                    "                        \"id\": 8,\n" +
                    "                        \"leftNodeid\": 17,\n" +
                    "                        \"rightNodeid\": 18,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": -0.5353452855316457\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 5,\n" +
                    "                        \"id\": 9,\n" +
                    "                        \"leftNodeid\": 19,\n" +
                    "                        \"rightNodeid\": 20,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": 0.8810058688243421\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"fid\": 7,\n" +
                    "                        \"id\": 10,\n" +
                    "                        \"leftNodeid\": 21,\n" +
                    "                        \"rightNodeid\": 22,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": -1.5152819357251675\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 11,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": -1.2959544810161243\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 12,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.6290811524064492\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 13,\n" +
                    "                        \"leftNodeid\": 23,\n" +
                    "                        \"rightNodeid\": 24,\n" +
                    "                        \"sitename\": \"host.0\",\n" +
                    "                        \"weight\": -0.013991193581546574\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 14,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": -1.8108826773632216\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 15,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": -0.08113869051242602\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 16,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.7700206892428898\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 17,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.331539890557368\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 18,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": -1.5369517697881\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 19,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": -0.029251281304972808\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 20,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.7408268930202675\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 21,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.331539890557368\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 22,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": -1.7686980219879578\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 23,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": 1.3100949664160633\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": 24,\n" +
                    "                        \"isLeaf\": true,\n" +
                    "                        \"leftNodeid\": -1,\n" +
                    "                        \"rightNodeid\": -1,\n" +
                    "                        \"sitename\": \"guest\",\n" +
                    "                        \"weight\": -1.331539890557368\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"module_name\": \"HeteroSecureBoost\"\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }

        if (component_name.contains("dataio")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"header\": [\n" +
                    "            \"fid0\",\n" +
                    "            \"fid1\",\n" +
                    "            \"fid2\"\n" +
                    "        ],\n" +
                    "        \"imputer_param\": {\n" +
                    "            \"missingReplaceValue\": {\n" +
                    "                \"fid0\": \"0\",\n" +
                    "                \"fid1\": \"0\",\n" +
                    "                \"fid2\": \"0\"\n" +
                    "            },\n" +
                    "            \"strategy\": \"mean\"\n" +
                    "        },\n" +
                    "        \"outlier_param\": {\n" +
                    "            \"outlierReplaceValue\": {\n" +
                    "                \"fid0\": \"0\",\n" +
                    "                \"fid1\": \"0\",\n" +
                    "                \"fid2\": \"0\"\n" +
                    "            },\n" +
                    "            \"strategy\": \"mean\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"module_name\": \"field\"\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }
        // TODO: 2019/6/20
        if (component_name.contains("federated")) {
            result = null;
        }


        if (component_name.contains("intersection")) {
            result = null;
        }

        if (component_name.contains("lr")) {
            result = "{\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"meta\": {\n" +
                    "        \"module_name\": \"HeteroLR\"\n" +
                    "    },\n" +
                    "    \"data\": {\n" +
                    "        \"converged\": true,\n" +
                    "        \"intercept\": 0.5096661669562351,\n" +
                    "        \"iters\": \"10\",\n" +
                    "        \"weight\": {\n" +
                    "            \"x0\": -0.29598219449355584,\n" +
                    "            \"x1\": -0.3384962994443867,\n" +
                    "            \"x2\": -0.13881183466484848,\n" +
                    "            \"x3\": 0.2052687223761664,\n" +
                    "            \"x4\": -0.14932114100548474,\n" +
                    "            \"x5\": -0.03247333049193252,\n" +
                    "            \"x6\": -0.3702834766819785,\n" +
                    "            \"x7\": -0.16033648325487504,\n" +
                    "            \"x8\": 0.07594446334901014,\n" +
                    "            \"x9\": -0.31760998253484846\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }
        if (component_name.contains("scale")) {
            Random random = new Random();
            int i = random.nextInt(10);
            if (i > 5) {
                result = "{\n" +
                        "    \"data\": {\n" +
                        "        \"method\": \"min_max_scale\",\n" +
                        "        \"min_max_scale_param\": {\n" +
                        "            \"x1\": [\n" +
                        "                0,\n" +
                        "                323,\n" +
                        "                0,\n" +
                        "                1\n" +
                        "            ],\n" +
                        "            \"x2\": [\n" +
                        "                0,\n" +
                        "                14.1,\n" +
                        "                0,\n" +
                        "                1\n" +
                        "            ],\n" +
                        "            \"x3\": [\n" +
                        "                -5,\n" +
                        "                32,\n" +
                        "                0,\n" +
                        "                1\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"meta\": {\n" +
                        "        \"module_name\": \"FeatureScale\"\n" +
                        "    },\n" +
                        "    \"retcode\": 0,\n" +
                        "    \"retmsg\": \"OK\"\n" +
                        "}";
            } else {
                result = "{\n" +
                        "    \"data\": {\n" +
                        "        \"standardScaleParam\": {\n" +
                        "            \"fid0\": {\n" +
                        "                \"mean\": -2.6362038655520638E-8,\n" +
                        "                \"scale\": 0.9999999963032254\n" +
                        "            },\n" +
                        "            \"fid1\": {\n" +
                        "                \"mean\": 1.7574692445338804E-9,\n" +
                        "                \"scale\": 0.9999999818942258\n" +
                        "            },\n" +
                        "            \"fid2\": {\n" +
                        "                \"mean\": 8.787346239839812E-9,\n" +
                        "                \"scale\": 1.0000000090027952\n" +
                        "            },\n" +
                        "            \"fid3\": {\n" +
                        "                \"mean\": -1.05448154812518E-8,\n" +
                        "                \"scale\": 0.9999999950534464\n" +
                        "            },\n" +
                        "            \"fid4\": {\n" +
                        "                \"mean\": 9.365677536380934E-18,\n" +
                        "                \"scale\": 0.9999999966699842\n" +
                        "            },\n" +
                        "            \"fid5\": {\n" +
                        "                \"mean\": 1.9332161701482223E-8,\n" +
                        "                \"scale\": 0.9999999807614925\n" +
                        "            },\n" +
                        "            \"fid6\": {\n" +
                        "                \"mean\": 8.787346222084047E-9,\n" +
                        "                \"scale\": 1.0000000248204188\n" +
                        "            },\n" +
                        "            \"fid7\": {\n" +
                        "                \"mean\": 5.272407710187447E-9,\n" +
                        "                \"scale\": 1.0000000065376404\n" +
                        "            },\n" +
                        "            \"fid8\": {\n" +
                        "                \"mean\": -2.987697712897894E-8,\n" +
                        "                \"scale\": 0.9999999995119494\n" +
                        "            },\n" +
                        "            \"fid9\": {\n" +
                        "                \"mean\": -1.2302284714859055E-8,\n" +
                        "                \"scale\": 0.9999999964968829\n" +
                        "            }\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"meta\": {\n" +
                        "        \"module_name\": \"FeatureScale\"\n" +
                        "    },\n" +
                        "    \"retcode\": 0,\n" +
                        "    \"retmsg\": \"OK\"\n" +
                        "}";
            }


        }
        if (component_name.contains("binning")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"binningResult\": {\n" +
                    "            \"binningResult\": {\n" +
                    "                \"fid8\": {\n" +
                    "                    \"binNums\": \"10\",\n" +
                    "                    \"eventCountArray\": [\n" +
                    "                        \"48\",\n" +
                    "                        \"48\",\n" +
                    "                        \"44\",\n" +
                    "                        \"43\",\n" +
                    "                        \"40\",\n" +
                    "                        \"36\",\n" +
                    "                        \"34\",\n" +
                    "                        \"32\",\n" +
                    "                        \"27\",\n" +
                    "                        \"5\"\n" +
                    "                    ],\n" +
                    "                    \"eventRateArray\": [\n" +
                    "                        0.13445378151260504,\n" +
                    "                        0.13445378151260504,\n" +
                    "                        0.12324929971988796,\n" +
                    "                        0.12044817927170869,\n" +
                    "                        0.11204481792717087,\n" +
                    "                        0.10084033613445378,\n" +
                    "                        0.09523809523809523,\n" +
                    "                        0.0896358543417367,\n" +
                    "                        0.07563025210084033,\n" +
                    "                        0.014005602240896359\n" +
                    "                    ],\n" +
                    "                    \"isWoeMonotonic\": true,\n" +
                    "                    \"iv\": 1.0284689754358698,\n" +
                    "                    \"ivArray\": [\n" +
                    "                        0.12289076849428592,\n" +
                    "                        0.10606117394652959,\n" +
                    "                        0.04323174562574946,\n" +
                    "                        0.032700310802828196,\n" +
                    "                        0.010656407294972203,\n" +
                    "                        0.000031834260073240545,\n" +
                    "                        0.001726574282752986,\n" +
                    "                        0.0077592842326783965,\n" +
                    "                        0.041273967727770054,\n" +
                    "                        0.6621369087682298\n" +
                    "                    ],\n" +
                    "                    \"nonEventCountArray\": [\n" +
                    "                        \"8\",\n" +
                    "                        \"9\",\n" +
                    "                        \"13\",\n" +
                    "                        \"14\",\n" +
                    "                        \"17\",\n" +
                    "                        \"21\",\n" +
                    "                        \"23\",\n" +
                    "                        \"25\",\n" +
                    "                        \"30\",\n" +
                    "                        \"52\"\n" +
                    "                    ],\n" +
                    "                    \"nonEventRateArray\": [\n" +
                    "                        0.03773584905660377,\n" +
                    "                        0.04245283018867924,\n" +
                    "                        0.06132075471698113,\n" +
                    "                        0.0660377358490566,\n" +
                    "                        0.08018867924528301,\n" +
                    "                        0.09905660377358491,\n" +
                    "                        0.10849056603773585,\n" +
                    "                        0.1179245283018868,\n" +
                    "                        0.14150943396226415,\n" +
                    "                        0.24528301886792453\n" +
                    "                    ],\n" +
                    "                    \"splitPoints\": [\n" +
                    "                        -1.0398379999999998,\n" +
                    "                        -0.753491,\n" +
                    "                        -0.544797,\n" +
                    "                        -0.334485,\n" +
                    "                        -0.127409,\n" +
                    "                        0.103933,\n" +
                    "                        0.327187,\n" +
                    "                        0.577943,\n" +
                    "                        1.1376950000000001\n" +
                    "                    ],\n" +
                    "                    \"woeArray\": [\n" +
                    "                        -1.2706099621204283,\n" +
                    "                        -1.152826926464045,\n" +
                    "                        -0.6980907693490979,\n" +
                    "                        -0.6009932789706773,\n" +
                    "                        -0.3345166029500937,\n" +
                    "                        -0.017846993625060308,\n" +
                    "                        0.13028319842061506,\n" +
                    "                        0.2742894291761008,\n" +
                    "                        0.626510022765453,\n" +
                    "                        2.8629553132549534\n" +
                    "                    ]\n" +
                    "                },\n" +
                    "                \"fid9\": {\n" +
                    "                    \"binNums\": \"10\",\n" +
                    "                    \"eventCountArray\": [\n" +
                    "                        \"39\",\n" +
                    "                        \"46\",\n" +
                    "                        \"50\",\n" +
                    "                        \"37\",\n" +
                    "                        \"40\",\n" +
                    "                        \"41\",\n" +
                    "                        \"36\",\n" +
                    "                        \"32\",\n" +
                    "                        \"19\",\n" +
                    "                        \"17\"\n" +
                    "                    ],\n" +
                    "                    \"eventRateArray\": [\n" +
                    "                        0.1092436974789916,\n" +
                    "                        0.12885154061624648,\n" +
                    "                        0.1400560224089636,\n" +
                    "                        0.10364145658263306,\n" +
                    "                        0.11204481792717087,\n" +
                    "                        0.11484593837535013,\n" +
                    "                        0.10084033613445378,\n" +
                    "                        0.0896358543417367,\n" +
                    "                        0.05322128851540616,\n" +
                    "                        0.047619047619047616\n" +
                    "                    ],\n" +
                    "                    \"iv\": 0.6167526912413686,\n" +
                    "                    \"ivArray\": [\n" +
                    "                        0.008983776625572152,\n" +
                    "                        0.07000687452561365,\n" +
                    "                        0.1546647652396469,\n" +
                    "                        0.0008747084841677925,\n" +
                    "                        0.010656407294972203,\n" +
                    "                        0.016530638382267877,\n" +
                    "                        0.000031834260073240545,\n" +
                    "                        0.0077592842326783965,\n" +
                    "                        0.15303051909218987,\n" +
                    "                        0.19421388310418644\n" +
                    "                    ],\n" +
                    "                    \"nonEventCountArray\": [\n" +
                    "                        \"17\",\n" +
                    "                        \"11\",\n" +
                    "                        \"7\",\n" +
                    "                        \"20\",\n" +
                    "                        \"17\",\n" +
                    "                        \"16\",\n" +
                    "                        \"21\",\n" +
                    "                        \"25\",\n" +
                    "                        \"38\",\n" +
                    "                        \"40\"\n" +
                    "                    ],\n" +
                    "                    \"nonEventRateArray\": [\n" +
                    "                        0.08018867924528301,\n" +
                    "                        0.05188679245283019,\n" +
                    "                        0.0330188679245283,\n" +
                    "                        0.09433962264150944,\n" +
                    "                        0.08018867924528301,\n" +
                    "                        0.07547169811320754,\n" +
                    "                        0.09905660377358491,\n" +
                    "                        0.1179245283018868,\n" +
                    "                        0.1792452830188679,\n" +
                    "                        0.18867924528301888\n" +
                    "                    ],\n" +
                    "                    \"splitPoints\": [\n" +
                    "                        -1.007782,\n" +
                    "                        -0.794985,\n" +
                    "                        -0.582743,\n" +
                    "                        -0.393221,\n" +
                    "                        -0.216444,\n" +
                    "                        -0.040776,\n" +
                    "                        0.28119,\n" +
                    "                        0.671317,\n" +
                    "                        1.2443170000000001\n" +
                    "                    ],\n" +
                    "                    \"woeArray\": [\n" +
                    "                        -0.3091987949658038,\n" +
                    "                        -0.9095966165830977,\n" +
                    "                        -1.444963349265206,\n" +
                    "                        -0.09403613198260684,\n" +
                    "                        -0.3345166029500937,\n" +
                    "                        -0.4198338373569,\n" +
                    "                        -0.017846993625060308,\n" +
                    "                        0.2742894291761008,\n" +
                    "                        1.2142966876675718,\n" +
                    "                        1.376815617165347\n" +
                    "                    ]\n" +
                    "                }\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"hostResults\": {\n" +
                    "            \"host\": {\n" +
                    "                \"binningResult\": {\n" +
                    "                    \"fid0\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"55\",\n" +
                    "                            \"56\",\n" +
                    "                            \"52\",\n" +
                    "                            \"53\",\n" +
                    "                            \"51\",\n" +
                    "                            \"42\",\n" +
                    "                            \"37\",\n" +
                    "                            \"10\",\n" +
                    "                            \"1\",\n" +
                    "                            \"0\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.15546218487394958,\n" +
                    "                            0.1568627450980392,\n" +
                    "                            0.14565826330532214,\n" +
                    "                            0.1484593837535014,\n" +
                    "                            0.14285714285714285,\n" +
                    "                            0.11764705882352941,\n" +
                    "                            0.10364145658263306,\n" +
                    "                            0.028011204481792718,\n" +
                    "                            0.0028011204481792717,\n" +
                    "                            0.0014005602240896359\n" +
                    "                        ],\n" +
                    "                        \"iv\": 4.78109237927036,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.6412565574506771,\n" +
                    "                            0.414430417581365,\n" +
                    "                            0.2591340754187956,\n" +
                    "                            0.26732748835346604,\n" +
                    "                            0.16088736804548914,\n" +
                    "                            0.02384334483974108,\n" +
                    "                            0.0008747084841677925,\n" +
                    "                            0.40068243538870474,\n" +
                    "                            1.1621573824496818,\n" +
                    "                            1.4504986012582712\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"0\",\n" +
                    "                            \"2\",\n" +
                    "                            \"4\",\n" +
                    "                            \"4\",\n" +
                    "                            \"7\",\n" +
                    "                            \"15\",\n" +
                    "                            \"20\",\n" +
                    "                            \"47\",\n" +
                    "                            \"55\",\n" +
                    "                            \"58\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.0023584905660377358,\n" +
                    "                            0.009433962264150943,\n" +
                    "                            0.018867924528301886,\n" +
                    "                            0.018867924528301886,\n" +
                    "                            0.0330188679245283,\n" +
                    "                            0.07075471698113207,\n" +
                    "                            0.09433962264150944,\n" +
                    "                            0.22169811320754718,\n" +
                    "                            0.25943396226415094,\n" +
                    "                            0.2759433962264151\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -4.1883806942047075,\n" +
                    "                            -2.8110550030675774,\n" +
                    "                            -2.0437998503539103,\n" +
                    "                            -2.0628480453246048,\n" +
                    "                            -1.4647659765613859,\n" +
                    "                            -0.5084699100735317,\n" +
                    "                            -0.09403613198260684,\n" +
                    "                            2.0687120158236394,\n" +
                    "                            4.528482692340098,\n" +
                    "                            5.283323441905383\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid1\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"54\",\n" +
                    "                            \"51\",\n" +
                    "                            \"48\",\n" +
                    "                            \"43\",\n" +
                    "                            \"42\",\n" +
                    "                            \"36\",\n" +
                    "                            \"23\",\n" +
                    "                            \"19\",\n" +
                    "                            \"17\",\n" +
                    "                            \"24\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.15126050420168066,\n" +
                    "                            0.14285714285714285,\n" +
                    "                            0.13445378151260504,\n" +
                    "                            0.12044817927170869,\n" +
                    "                            0.11764705882352941,\n" +
                    "                            0.10084033613445378,\n" +
                    "                            0.06442577030812324,\n" +
                    "                            0.05322128851540616,\n" +
                    "                            0.047619047619047616,\n" +
                    "                            0.06722689075630252\n" +
                    "                        ],\n" +
                    "                        \"iv\": 1.2623165975794457,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.3935243130700967,\n" +
                    "                            0.18545541211999292,\n" +
                    "                            0.12289076849428592,\n" +
                    "                            0.026436942187863913,\n" +
                    "                            0.02384334483974108,\n" +
                    "                            0.000031834260073240545,\n" +
                    "                            0.08750936597300174,\n" +
                    "                            0.14406765912233302,\n" +
                    "                            0.20430791579323032,\n" +
                    "                            0.07424904171882693\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"2\",\n" +
                    "                            \"6\",\n" +
                    "                            \"8\",\n" +
                    "                            \"15\",\n" +
                    "                            \"15\",\n" +
                    "                            \"21\",\n" +
                    "                            \"34\",\n" +
                    "                            \"37\",\n" +
                    "                            \"41\",\n" +
                    "                            \"33\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.009433962264150943,\n" +
                    "                            0.02830188679245283,\n" +
                    "                            0.03773584905660377,\n" +
                    "                            0.07075471698113207,\n" +
                    "                            0.07075471698113207,\n" +
                    "                            0.09905660377358491,\n" +
                    "                            0.16037735849056603,\n" +
                    "                            0.17452830188679244,\n" +
                    "                            0.19339622641509435,\n" +
                    "                            0.15566037735849056\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -2.7746873588967023,\n" +
                    "                            -1.6189166563886441,\n" +
                    "                            -1.2706099621204283,\n" +
                    "                            -0.5320004074837257,\n" +
                    "                            -0.5084699100735317,\n" +
                    "                            -0.017846993625060308,\n" +
                    "                            0.9120158157946383,\n" +
                    "                            1.1876284405854105,\n" +
                    "                            1.4015082297557184,\n" +
                    "                            0.8396032382261613\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid10\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"56\",\n" +
                    "                            \"50\",\n" +
                    "                            \"50\",\n" +
                    "                            \"48\",\n" +
                    "                            \"41\",\n" +
                    "                            \"49\",\n" +
                    "                            \"32\",\n" +
                    "                            \"23\",\n" +
                    "                            \"6\",\n" +
                    "                            \"2\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.15826330532212884,\n" +
                    "                            0.1400560224089636,\n" +
                    "                            0.1400560224089636,\n" +
                    "                            0.13445378151260504,\n" +
                    "                            0.11484593837535013,\n" +
                    "                            0.13725490196078433,\n" +
                    "                            0.0896358543417367,\n" +
                    "                            0.06442577030812324,\n" +
                    "                            0.01680672268907563,\n" +
                    "                            0.0056022408963585435\n" +
                    "                        ],\n" +
                    "                        \"iv\": 2.880466320403542,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.6557728047907063,\n" +
                    "                            0.1546647652396469,\n" +
                    "                            0.1546647652396469,\n" +
                    "                            0.10606117394652959,\n" +
                    "                            0.016530638382267877,\n" +
                    "                            0.12850191197481733,\n" +
                    "                            0.0077592842326783965,\n" +
                    "                            0.08750936597300174,\n" +
                    "                            0.595471795646068,\n" +
                    "                            0.9735298149781791\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"0\",\n" +
                    "                            \"7\",\n" +
                    "                            \"7\",\n" +
                    "                            \"9\",\n" +
                    "                            \"16\",\n" +
                    "                            \"8\",\n" +
                    "                            \"25\",\n" +
                    "                            \"34\",\n" +
                    "                            \"51\",\n" +
                    "                            \"55\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.0023584905660377358,\n" +
                    "                            0.0330188679245283,\n" +
                    "                            0.0330188679245283,\n" +
                    "                            0.04245283018867924,\n" +
                    "                            0.07547169811320754,\n" +
                    "                            0.03773584905660377,\n" +
                    "                            0.1179245283018868,\n" +
                    "                            0.16037735849056603,\n" +
                    "                            0.24056603773584906,\n" +
                    "                            0.25943396226415094\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -4.2062383116047135,\n" +
                    "                            -1.444963349265206,\n" +
                    "                            -1.444963349265206,\n" +
                    "                            -1.152826926464045,\n" +
                    "                            -0.4198338373569,\n" +
                    "                            -1.2912292493231643,\n" +
                    "                            0.2742894291761008,\n" +
                    "                            0.9120158157946383,\n" +
                    "                            2.6612156706038976,\n" +
                    "                            3.835335511780152\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid11\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"43\",\n" +
                    "                            \"40\",\n" +
                    "                            \"32\",\n" +
                    "                            \"31\",\n" +
                    "                            \"31\",\n" +
                    "                            \"35\",\n" +
                    "                            \"34\",\n" +
                    "                            \"34\",\n" +
                    "                            \"36\",\n" +
                    "                            \"41\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.12044817927170869,\n" +
                    "                            0.11204481792717087,\n" +
                    "                            0.0896358543417367,\n" +
                    "                            0.08683473389355742,\n" +
                    "                            0.08683473389355742,\n" +
                    "                            0.09803921568627451,\n" +
                    "                            0.09523809523809523,\n" +
                    "                            0.09523809523809523,\n" +
                    "                            0.10084033613445378,\n" +
                    "                            0.11484593837535013\n" +
                    "                        ],\n" +
                    "                        \"iv\": 0.10128425575114644,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.039916998292660086,\n" +
                    "                            0.010656407294972203,\n" +
                    "                            0.0077592842326783965,\n" +
                    "                            0.012362605810278772,\n" +
                    "                            0.00951466246479537,\n" +
                    "                            0.001058676447914538,\n" +
                    "                            0.001726574282752986,\n" +
                    "                            0.001726574282752986,\n" +
                    "                            0.000031834260073240545,\n" +
                    "                            0.016530638382267877\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"13\",\n" +
                    "                            \"17\",\n" +
                    "                            \"25\",\n" +
                    "                            \"26\",\n" +
                    "                            \"25\",\n" +
                    "                            \"23\",\n" +
                    "                            \"23\",\n" +
                    "                            \"23\",\n" +
                    "                            \"21\",\n" +
                    "                            \"16\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.06132075471698113,\n" +
                    "                            0.08018867924528301,\n" +
                    "                            0.1179245283018868,\n" +
                    "                            0.12264150943396226,\n" +
                    "                            0.1179245283018868,\n" +
                    "                            0.10849056603773585,\n" +
                    "                            0.10849056603773585,\n" +
                    "                            0.10849056603773585,\n" +
                    "                            0.09905660377358491,\n" +
                    "                            0.07547169811320754\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -0.6751012511243991,\n" +
                    "                            -0.3345166029500937,\n" +
                    "                            0.2742894291761008,\n" +
                    "                            0.3452588406439625,\n" +
                    "                            0.30603812749068116,\n" +
                    "                            0.10129566154736266,\n" +
                    "                            0.13028319842061506,\n" +
                    "                            0.13028319842061506,\n" +
                    "                            -0.017846993625060308,\n" +
                    "                            -0.4198338373569\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid12\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"56\",\n" +
                    "                            \"53\",\n" +
                    "                            \"52\",\n" +
                    "                            \"46\",\n" +
                    "                            \"44\",\n" +
                    "                            \"41\",\n" +
                    "                            \"30\",\n" +
                    "                            \"22\",\n" +
                    "                            \"13\",\n" +
                    "                            \"0\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.15826330532212884,\n" +
                    "                            0.1484593837535014,\n" +
                    "                            0.14565826330532214,\n" +
                    "                            0.12885154061624648,\n" +
                    "                            0.12324929971988796,\n" +
                    "                            0.11484593837535013,\n" +
                    "                            0.08403361344537816,\n" +
                    "                            0.06162464985994398,\n" +
                    "                            0.036414565826330535,\n" +
                    "                            0.0014005602240896359\n" +
                    "                        ],\n" +
                    "                        \"isWoeMonotonic\": true,\n" +
                    "                        \"iv\": 3.1138643359481923,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.6557728047907063,\n" +
                    "                            0.26732748835346604,\n" +
                    "                            0.22225362754119488,\n" +
                    "                            0.07000687452561365,\n" +
                    "                            0.04323174562574946,\n" +
                    "                            0.016530638382267877,\n" +
                    "                            0.01801400696268557,\n" +
                    "                            0.10196473504881415,\n" +
                    "                            0.2978374356102409,\n" +
                    "                            1.4209249791074536\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"0\",\n" +
                    "                            \"4\",\n" +
                    "                            \"5\",\n" +
                    "                            \"11\",\n" +
                    "                            \"13\",\n" +
                    "                            \"16\",\n" +
                    "                            \"27\",\n" +
                    "                            \"35\",\n" +
                    "                            \"44\",\n" +
                    "                            \"57\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.0023584905660377358,\n" +
                    "                            0.018867924528301886,\n" +
                    "                            0.02358490566037736,\n" +
                    "                            0.05188679245283019,\n" +
                    "                            0.06132075471698113,\n" +
                    "                            0.07547169811320754,\n" +
                    "                            0.12735849056603774,\n" +
                    "                            0.1650943396226415,\n" +
                    "                            0.20754716981132076,\n" +
                    "                            0.27122641509433965\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -4.2062383116047135,\n" +
                    "                            -2.0628480453246048,\n" +
                    "                            -1.8206562990397004,\n" +
                    "                            -0.9095966165830977,\n" +
                    "                            -0.6980907693490979,\n" +
                    "                            -0.4198338373569,\n" +
                    "                            0.4157889914498002,\n" +
                    "                            0.9854551152387244,\n" +
                    "                            1.740389783564351,\n" +
                    "                            5.266081635470877\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid13\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"56\",\n" +
                    "                            \"54\",\n" +
                    "                            \"54\",\n" +
                    "                            \"51\",\n" +
                    "                            \"46\",\n" +
                    "                            \"47\",\n" +
                    "                            \"33\",\n" +
                    "                            \"15\",\n" +
                    "                            \"1\",\n" +
                    "                            \"0\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.15826330532212884,\n" +
                    "                            0.15126050420168066,\n" +
                    "                            0.15126050420168066,\n" +
                    "                            0.14285714285714285,\n" +
                    "                            0.12885154061624648,\n" +
                    "                            0.13165266106442577,\n" +
                    "                            0.09243697478991597,\n" +
                    "                            0.04201680672268908,\n" +
                    "                            0.0028011204481792717,\n" +
                    "                            0.0014005602240896359\n" +
                    "                        ],\n" +
                    "                        \"iv\": 4.522485990874989,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.6557728047907063,\n" +
                    "                            0.3935243130700967,\n" +
                    "                            0.27558047390378815,\n" +
                    "                            0.18545541211999292,\n" +
                    "                            0.07000687452561365,\n" +
                    "                            0.0867142953898317,\n" +
                    "                            0.00421010728628049,\n" +
                    "                            0.242069447591931,\n" +
                    "                            1.1882272830892937,\n" +
                    "                            1.4209249791074536\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"0\",\n" +
                    "                            \"2\",\n" +
                    "                            \"4\",\n" +
                    "                            \"6\",\n" +
                    "                            \"11\",\n" +
                    "                            \"10\",\n" +
                    "                            \"24\",\n" +
                    "                            \"42\",\n" +
                    "                            \"56\",\n" +
                    "                            \"57\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.0023584905660377358,\n" +
                    "                            0.009433962264150943,\n" +
                    "                            0.018867924528301886,\n" +
                    "                            0.02830188679245283,\n" +
                    "                            0.05188679245283019,\n" +
                    "                            0.04716981132075472,\n" +
                    "                            0.11320754716981132,\n" +
                    "                            0.19811320754716982,\n" +
                    "                            0.2641509433962264,\n" +
                    "                            0.27122641509433965\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -4.2062383116047135,\n" +
                    "                            -2.7746873588967023,\n" +
                    "                            -2.081540178336757,\n" +
                    "                            -1.6189166563886441,\n" +
                    "                            -0.9095966165830977,\n" +
                    "                            -1.0264130016083863,\n" +
                    "                            0.20269577598909203,\n" +
                    "                            1.550768924288785,\n" +
                    "                            4.546501197842776,\n" +
                    "                            5.266081635470877\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid14\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"38\",\n" +
                    "                            \"33\",\n" +
                    "                            \"35\",\n" +
                    "                            \"31\",\n" +
                    "                            \"33\",\n" +
                    "                            \"34\",\n" +
                    "                            \"40\",\n" +
                    "                            \"32\",\n" +
                    "                            \"38\",\n" +
                    "                            \"43\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.10644257703081232,\n" +
                    "                            0.09243697478991597,\n" +
                    "                            0.09803921568627451,\n" +
                    "                            0.08683473389355742,\n" +
                    "                            0.09243697478991597,\n" +
                    "                            0.09523809523809523,\n" +
                    "                            0.11204481792717087,\n" +
                    "                            0.0896358543417367,\n" +
                    "                            0.10644257703081232,\n" +
                    "                            0.12044817927170869\n" +
                    "                        ],\n" +
                    "                        \"iv\": 0.08171309147387984,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.004868740795912333,\n" +
                    "                            0.00421010728628049,\n" +
                    "                            0.00032596390460088744,\n" +
                    "                            0.012362605810278772,\n" +
                    "                            0.00421010728628049,\n" +
                    "                            0.001726574282752986,\n" +
                    "                            0.010656407294972203,\n" +
                    "                            0.0077592842326783965,\n" +
                    "                            0.0028929897772950928,\n" +
                    "                            0.032700310802828196\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"18\",\n" +
                    "                            \"24\",\n" +
                    "                            \"22\",\n" +
                    "                            \"26\",\n" +
                    "                            \"24\",\n" +
                    "                            \"23\",\n" +
                    "                            \"17\",\n" +
                    "                            \"25\",\n" +
                    "                            \"19\",\n" +
                    "                            \"14\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.08490566037735849,\n" +
                    "                            0.11320754716981132,\n" +
                    "                            0.10377358490566038,\n" +
                    "                            0.12264150943396226,\n" +
                    "                            0.11320754716981132,\n" +
                    "                            0.10849056603773585,\n" +
                    "                            0.08018867924528301,\n" +
                    "                            0.1179245283018868,\n" +
                    "                            0.08962264150943396,\n" +
                    "                            0.0660377358490566\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -0.22606489472259447,\n" +
                    "                            0.20269577598909203,\n" +
                    "                            0.05684389897652889,\n" +
                    "                            0.3452588406439625,\n" +
                    "                            0.20269577598909203,\n" +
                    "                            0.13028319842061506,\n" +
                    "                            -0.3345166029500937,\n" +
                    "                            0.2742894291761008,\n" +
                    "                            -0.17199767345231876,\n" +
                    "                            -0.6009932789706773\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid15\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"54\",\n" +
                    "                            \"44\",\n" +
                    "                            \"49\",\n" +
                    "                            \"42\",\n" +
                    "                            \"39\",\n" +
                    "                            \"34\",\n" +
                    "                            \"21\",\n" +
                    "                            \"27\",\n" +
                    "                            \"24\",\n" +
                    "                            \"23\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.15126050420168066,\n" +
                    "                            0.12324929971988796,\n" +
                    "                            0.13725490196078433,\n" +
                    "                            0.11764705882352941,\n" +
                    "                            0.1092436974789916,\n" +
                    "                            0.09523809523809523,\n" +
                    "                            0.058823529411764705,\n" +
                    "                            0.07563025210084033,\n" +
                    "                            0.06722689075630252,\n" +
                    "                            0.06442577030812324\n" +
                    "                        ],\n" +
                    "                        \"iv\": 0.9176576972709517,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.3935243130700967,\n" +
                    "                            0.04323174562574946,\n" +
                    "                            0.12850191197481733,\n" +
                    "                            0.02384334483974108,\n" +
                    "                            0.006134168146950812,\n" +
                    "                            0.001726574282752986,\n" +
                    "                            0.11766326391124456,\n" +
                    "                            0.041273967727770054,\n" +
                    "                            0.07424904171882693,\n" +
                    "                            0.08750936597300174\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"2\",\n" +
                    "                            \"13\",\n" +
                    "                            \"8\",\n" +
                    "                            \"15\",\n" +
                    "                            \"18\",\n" +
                    "                            \"23\",\n" +
                    "                            \"36\",\n" +
                    "                            \"30\",\n" +
                    "                            \"33\",\n" +
                    "                            \"34\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.009433962264150943,\n" +
                    "                            0.06132075471698113,\n" +
                    "                            0.03773584905660377,\n" +
                    "                            0.07075471698113207,\n" +
                    "                            0.08490566037735849,\n" +
                    "                            0.10849056603773585,\n" +
                    "                            0.16981132075471697,\n" +
                    "                            0.14150943396226415,\n" +
                    "                            0.15566037735849056,\n" +
                    "                            0.16037735849056603\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -2.7746873588967023,\n" +
                    "                            -0.6980907693490979,\n" +
                    "                            -1.2912292493231643,\n" +
                    "                            -0.5084699100735317,\n" +
                    "                            -0.25204038112585514,\n" +
                    "                            0.13028319842061506,\n" +
                    "                            1.0601460078403135,\n" +
                    "                            0.626510022765453,\n" +
                    "                            0.8396032382261613,\n" +
                    "                            0.9120158157946383\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid16\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"56\",\n" +
                    "                            \"53\",\n" +
                    "                            \"51\",\n" +
                    "                            \"46\",\n" +
                    "                            \"35\",\n" +
                    "                            \"26\",\n" +
                    "                            \"23\",\n" +
                    "                            \"24\",\n" +
                    "                            \"19\",\n" +
                    "                            \"24\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.15826330532212884,\n" +
                    "                            0.1484593837535014,\n" +
                    "                            0.14285714285714285,\n" +
                    "                            0.12885154061624648,\n" +
                    "                            0.09803921568627451,\n" +
                    "                            0.07282913165266107,\n" +
                    "                            0.06442577030812324,\n" +
                    "                            0.06722689075630252,\n" +
                    "                            0.05322128851540616,\n" +
                    "                            0.06722689075630252\n" +
                    "                        ],\n" +
                    "                        \"iv\": 1.6428878690904525,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.6557728047907063,\n" +
                    "                            0.3156960341313482,\n" +
                    "                            0.16088736804548914,\n" +
                    "                            0.07000687452561365,\n" +
                    "                            0.00032596390460088744,\n" +
                    "                            0.051160855189848836,\n" +
                    "                            0.08750936597300174,\n" +
                    "                            0.07424904171882693,\n" +
                    "                            0.15303051909218987,\n" +
                    "                            0.07424904171882693\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"0\",\n" +
                    "                            \"3\",\n" +
                    "                            \"7\",\n" +
                    "                            \"11\",\n" +
                    "                            \"22\",\n" +
                    "                            \"31\",\n" +
                    "                            \"34\",\n" +
                    "                            \"33\",\n" +
                    "                            \"38\",\n" +
                    "                            \"33\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.0023584905660377358,\n" +
                    "                            0.014150943396226415,\n" +
                    "                            0.0330188679245283,\n" +
                    "                            0.05188679245283019,\n" +
                    "                            0.10377358490566038,\n" +
                    "                            0.14622641509433962,\n" +
                    "                            0.16037735849056603,\n" +
                    "                            0.15566037735849056,\n" +
                    "                            0.1792452830188679,\n" +
                    "                            0.15566037735849056\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -4.2062383116047135,\n" +
                    "                            -2.3505301177763855,\n" +
                    "                            -1.4647659765613859,\n" +
                    "                            -0.9095966165830977,\n" +
                    "                            0.05684389897652889,\n" +
                    "                            0.6970401735712907,\n" +
                    "                            0.9120158157946383,\n" +
                    "                            0.8396032382261613,\n" +
                    "                            1.2142966876675718,\n" +
                    "                            0.8396032382261613\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid17\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"54\",\n" +
                    "                            \"52\",\n" +
                    "                            \"52\",\n" +
                    "                            \"42\",\n" +
                    "                            \"39\",\n" +
                    "                            \"32\",\n" +
                    "                            \"28\",\n" +
                    "                            \"21\",\n" +
                    "                            \"18\",\n" +
                    "                            \"19\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.15126050420168066,\n" +
                    "                            0.14565826330532214,\n" +
                    "                            0.14565826330532214,\n" +
                    "                            0.11764705882352941,\n" +
                    "                            0.1092436974789916,\n" +
                    "                            0.0896358543417367,\n" +
                    "                            0.0784313725490196,\n" +
                    "                            0.058823529411764705,\n" +
                    "                            0.05042016806722689,\n" +
                    "                            0.05322128851540616\n" +
                    "                        ],\n" +
                    "                        \"iv\": 1.357688579041451,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.3935243130700967,\n" +
                    "                            0.22225362754119488,\n" +
                    "                            0.22225362754119488,\n" +
                    "                            0.02384334483974108,\n" +
                    "                            0.006134168146950812,\n" +
                    "                            0.00550322265506618,\n" +
                    "                            0.0324628155554776,\n" +
                    "                            0.1258341447019574,\n" +
                    "                            0.17284879589758176,\n" +
                    "                            0.15303051909218987\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"2\",\n" +
                    "                            \"5\",\n" +
                    "                            \"5\",\n" +
                    "                            \"15\",\n" +
                    "                            \"18\",\n" +
                    "                            \"24\",\n" +
                    "                            \"29\",\n" +
                    "                            \"37\",\n" +
                    "                            \"39\",\n" +
                    "                            \"38\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.009433962264150943,\n" +
                    "                            0.02358490566037736,\n" +
                    "                            0.02358490566037736,\n" +
                    "                            0.07075471698113207,\n" +
                    "                            0.08490566037735849,\n" +
                    "                            0.11320754716981132,\n" +
                    "                            0.13679245283018868,\n" +
                    "                            0.17452830188679244,\n" +
                    "                            0.18396226415094338,\n" +
                    "                            0.1792452830188679\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -2.7746873588967023,\n" +
                    "                            -1.8206562990397004,\n" +
                    "                            -1.8206562990397004,\n" +
                    "                            -0.5084699100735317,\n" +
                    "                            -0.25204038112585514,\n" +
                    "                            0.23346743465584568,\n" +
                    "                            0.5562408269188968,\n" +
                    "                            1.087544982028428,\n" +
                    "                            1.2943393953411082,\n" +
                    "                            1.2142966876675718\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid18\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"30\",\n" +
                    "                            \"32\",\n" +
                    "                            \"32\",\n" +
                    "                            \"34\",\n" +
                    "                            \"40\",\n" +
                    "                            \"38\",\n" +
                    "                            \"34\",\n" +
                    "                            \"39\",\n" +
                    "                            \"43\",\n" +
                    "                            \"35\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.08403361344537816,\n" +
                    "                            0.0896358543417367,\n" +
                    "                            0.0896358543417367,\n" +
                    "                            0.09523809523809523,\n" +
                    "                            0.11204481792717087,\n" +
                    "                            0.10644257703081232,\n" +
                    "                            0.09523809523809523,\n" +
                    "                            0.1092436974789916,\n" +
                    "                            0.12044817927170869,\n" +
                    "                            0.09803921568627451\n" +
                    "                        ],\n" +
                    "                        \"iv\": 0.08627722043526534,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.014595663477755365,\n" +
                    "                            0.0077592842326783965,\n" +
                    "                            0.0077592842326783965,\n" +
                    "                            0.001726574282752986,\n" +
                    "                            0.010656407294972203,\n" +
                    "                            0.0028929897772950928,\n" +
                    "                            0.001726574282752986,\n" +
                    "                            0.006134168146950812,\n" +
                    "                            0.032700310802828196,\n" +
                    "                            0.00032596390460088744\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"26\",\n" +
                    "                            \"25\",\n" +
                    "                            \"25\",\n" +
                    "                            \"23\",\n" +
                    "                            \"17\",\n" +
                    "                            \"19\",\n" +
                    "                            \"23\",\n" +
                    "                            \"18\",\n" +
                    "                            \"14\",\n" +
                    "                            \"22\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.12264150943396226,\n" +
                    "                            0.1179245283018868,\n" +
                    "                            0.1179245283018868,\n" +
                    "                            0.10849056603773585,\n" +
                    "                            0.08018867924528301,\n" +
                    "                            0.08962264150943396,\n" +
                    "                            0.10849056603773585,\n" +
                    "                            0.08490566037735849,\n" +
                    "                            0.0660377358490566,\n" +
                    "                            0.10377358490566038\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            0.3780486634669532,\n" +
                    "                            0.2742894291761008,\n" +
                    "                            0.2742894291761008,\n" +
                    "                            0.13028319842061506,\n" +
                    "                            -0.3345166029500937,\n" +
                    "                            -0.17199767345231876,\n" +
                    "                            0.13028319842061506,\n" +
                    "                            -0.25204038112585514,\n" +
                    "                            -0.6009932789706773,\n" +
                    "                            0.05684389897652889\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid19\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"45\",\n" +
                    "                            \"41\",\n" +
                    "                            \"45\",\n" +
                    "                            \"38\",\n" +
                    "                            \"37\",\n" +
                    "                            \"34\",\n" +
                    "                            \"27\",\n" +
                    "                            \"30\",\n" +
                    "                            \"26\",\n" +
                    "                            \"34\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.12605042016806722,\n" +
                    "                            0.11484593837535013,\n" +
                    "                            0.12605042016806722,\n" +
                    "                            0.10644257703081232,\n" +
                    "                            0.10364145658263306,\n" +
                    "                            0.09523809523809523,\n" +
                    "                            0.07563025210084033,\n" +
                    "                            0.08403361344537816,\n" +
                    "                            0.07282913165266107,\n" +
                    "                            0.09523809523809523\n" +
                    "                        ],\n" +
                    "                        \"iv\": 0.2556286895287599,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.06582894938782889,\n" +
                    "                            0.016530638382267877,\n" +
                    "                            0.055599425051389796,\n" +
                    "                            0.0028929897772950928,\n" +
                    "                            0.0008747084841677925,\n" +
                    "                            0.001726574282752986,\n" +
                    "                            0.041273967727770054,\n" +
                    "                            0.01801400696268557,\n" +
                    "                            0.051160855189848836,\n" +
                    "                            0.001726574282752986\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"11\",\n" +
                    "                            \"16\",\n" +
                    "                            \"12\",\n" +
                    "                            \"19\",\n" +
                    "                            \"20\",\n" +
                    "                            \"23\",\n" +
                    "                            \"30\",\n" +
                    "                            \"27\",\n" +
                    "                            \"31\",\n" +
                    "                            \"23\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.05188679245283019,\n" +
                    "                            0.07547169811320754,\n" +
                    "                            0.05660377358490566,\n" +
                    "                            0.08962264150943396,\n" +
                    "                            0.09433962264150944,\n" +
                    "                            0.10849056603773585,\n" +
                    "                            0.14150943396226415,\n" +
                    "                            0.12735849056603774,\n" +
                    "                            0.14622641509433962,\n" +
                    "                            0.10849056603773585\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -0.8876177098643225,\n" +
                    "                            -0.4198338373569,\n" +
                    "                            -0.8006063328746928,\n" +
                    "                            -0.17199767345231876,\n" +
                    "                            -0.09403613198260684,\n" +
                    "                            0.13028319842061506,\n" +
                    "                            0.626510022765453,\n" +
                    "                            0.4157889914498002,\n" +
                    "                            0.6970401735712907,\n" +
                    "                            0.13028319842061506\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid2\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"56\",\n" +
                    "                            \"56\",\n" +
                    "                            \"55\",\n" +
                    "                            \"52\",\n" +
                    "                            \"49\",\n" +
                    "                            \"45\",\n" +
                    "                            \"33\",\n" +
                    "                            \"10\",\n" +
                    "                            \"1\",\n" +
                    "                            \"0\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.15826330532212884,\n" +
                    "                            0.1568627450980392,\n" +
                    "                            0.15406162464985995,\n" +
                    "                            0.14565826330532214,\n" +
                    "                            0.13725490196078433,\n" +
                    "                            0.12605042016806722,\n" +
                    "                            0.09243697478991597,\n" +
                    "                            0.028011204481792718,\n" +
                    "                            0.0028011204481792717,\n" +
                    "                            0.0014005602240896359\n" +
                    "                        ],\n" +
                    "                        \"isWoeMonotonic\": true,\n" +
                    "                        \"iv\": 5.016776153661034,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.6557728047907063,\n" +
                    "                            0.5331495183192079,\n" +
                    "                            0.40395033960077936,\n" +
                    "                            0.22225362754119488,\n" +
                    "                            0.12850191197481733,\n" +
                    "                            0.055599425051389796,\n" +
                    "                            0.00421010728628049,\n" +
                    "                            0.40068243538870474,\n" +
                    "                            1.1621573824496818,\n" +
                    "                            1.4504986012582712\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"0\",\n" +
                    "                            \"1\",\n" +
                    "                            \"2\",\n" +
                    "                            \"5\",\n" +
                    "                            \"8\",\n" +
                    "                            \"12\",\n" +
                    "                            \"24\",\n" +
                    "                            \"47\",\n" +
                    "                            \"55\",\n" +
                    "                            \"58\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.0023584905660377358,\n" +
                    "                            0.0047169811320754715,\n" +
                    "                            0.009433962264150943,\n" +
                    "                            0.02358490566037736,\n" +
                    "                            0.03773584905660377,\n" +
                    "                            0.05660377358490566,\n" +
                    "                            0.11320754716981132,\n" +
                    "                            0.22169811320754718,\n" +
                    "                            0.25943396226415094,\n" +
                    "                            0.2759433962264151\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -4.2062383116047135,\n" +
                    "                            -3.504202183627523,\n" +
                    "                            -2.793036497564899,\n" +
                    "                            -1.8206562990397004,\n" +
                    "                            -1.2912292493231643,\n" +
                    "                            -0.8006063328746928,\n" +
                    "                            0.20269577598909203,\n" +
                    "                            2.0687120158236394,\n" +
                    "                            4.528482692340098,\n" +
                    "                            5.283323441905383\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid20\": {\n" +
                    "                        \"binNums\": \"3\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"105\",\n" +
                    "                            \"129\",\n" +
                    "                            \"123\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.29411764705882354,\n" +
                    "                            0.36134453781512604,\n" +
                    "                            0.3445378151260504\n" +
                    "                        ],\n" +
                    "                        \"isWoeMonotonic\": true,\n" +
                    "                        \"iv\": 0.016574576662535794,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.009408529836444508,\n" +
                    "                            0.00016031329039509356,\n" +
                    "                            0.007005733535696193\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"74\",\n" +
                    "                            \"75\",\n" +
                    "                            \"63\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.3490566037735849,\n" +
                    "                            0.35377358490566035,\n" +
                    "                            0.2971698113207547\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            0.17125425015427287,\n" +
                    "                            -0.02117478371773514,\n" +
                    "                            -0.14790012187325818\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid3\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"56\",\n" +
                    "                            \"54\",\n" +
                    "                            \"54\",\n" +
                    "                            \"54\",\n" +
                    "                            \"49\",\n" +
                    "                            \"42\",\n" +
                    "                            \"38\",\n" +
                    "                            \"9\",\n" +
                    "                            \"1\",\n" +
                    "                            \"0\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.15826330532212884,\n" +
                    "                            0.15126050420168066,\n" +
                    "                            0.15126050420168066,\n" +
                    "                            0.15126050420168066,\n" +
                    "                            0.13725490196078433,\n" +
                    "                            0.11764705882352941,\n" +
                    "                            0.10644257703081232,\n" +
                    "                            0.025210084033613446,\n" +
                    "                            0.0028011204481792717,\n" +
                    "                            0.0014005602240896359\n" +
                    "                        ],\n" +
                    "                        \"isWoeMonotonic\": true,\n" +
                    "                        \"iv\": 4.836362717865052,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.6557728047907063,\n" +
                    "                            0.32484302225612627,\n" +
                    "                            0.32484302225612627,\n" +
                    "                            0.32484302225612627,\n" +
                    "                            0.12850191197481733,\n" +
                    "                            0.02384334483974108,\n" +
                    "                            0.0028929897772950928,\n" +
                    "                            0.4416703375173664,\n" +
                    "                            1.1882272830892937,\n" +
                    "                            1.4209249791074536\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"0\",\n" +
                    "                            \"3\",\n" +
                    "                            \"3\",\n" +
                    "                            \"3\",\n" +
                    "                            \"8\",\n" +
                    "                            \"15\",\n" +
                    "                            \"19\",\n" +
                    "                            \"48\",\n" +
                    "                            \"56\",\n" +
                    "                            \"57\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.0023584905660377358,\n" +
                    "                            0.014150943396226415,\n" +
                    "                            0.014150943396226415,\n" +
                    "                            0.014150943396226415,\n" +
                    "                            0.03773584905660377,\n" +
                    "                            0.07075471698113207,\n" +
                    "                            0.08962264150943396,\n" +
                    "                            0.22641509433962265,\n" +
                    "                            0.2641509433962264,\n" +
                    "                            0.27122641509433965\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -4.2062383116047135,\n" +
                    "                            -2.369222250788538,\n" +
                    "                            -2.369222250788538,\n" +
                    "                            -2.369222250788538,\n" +
                    "                            -1.2912292493231643,\n" +
                    "                            -0.5084699100735317,\n" +
                    "                            -0.17199767345231876,\n" +
                    "                            2.1951259406792984,\n" +
                    "                            4.546501197842776,\n" +
                    "                            5.266081635470877\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid4\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"53\",\n" +
                    "                            \"48\",\n" +
                    "                            \"47\",\n" +
                    "                            \"36\",\n" +
                    "                            \"35\",\n" +
                    "                            \"34\",\n" +
                    "                            \"32\",\n" +
                    "                            \"27\",\n" +
                    "                            \"25\",\n" +
                    "                            \"20\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.1484593837535014,\n" +
                    "                            0.13445378151260504,\n" +
                    "                            0.13165266106442577,\n" +
                    "                            0.10084033613445378,\n" +
                    "                            0.09803921568627451,\n" +
                    "                            0.09523809523809523,\n" +
                    "                            0.0896358543417367,\n" +
                    "                            0.07563025210084033,\n" +
                    "                            0.0700280112044818,\n" +
                    "                            0.056022408963585436\n" +
                    "                        ],\n" +
                    "                        \"isWoeMonotonic\": true,\n" +
                    "                        \"iv\": 0.7576243375086991,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.3156960341313482,\n" +
                    "                            0.10606117394652959,\n" +
                    "                            0.0867142953898317,\n" +
                    "                            0.000031834260073240545,\n" +
                    "                            0.00032596390460088744,\n" +
                    "                            0.001726574282752986,\n" +
                    "                            0.0077592842326783965,\n" +
                    "                            0.03624523826270484,\n" +
                    "                            0.06840152793797866,\n" +
                    "                            0.13466241116020036\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"3\",\n" +
                    "                            \"9\",\n" +
                    "                            \"10\",\n" +
                    "                            \"21\",\n" +
                    "                            \"22\",\n" +
                    "                            \"23\",\n" +
                    "                            \"25\",\n" +
                    "                            \"29\",\n" +
                    "                            \"33\",\n" +
                    "                            \"37\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.014150943396226415,\n" +
                    "                            0.04245283018867924,\n" +
                    "                            0.04716981132075472,\n" +
                    "                            0.09905660377358491,\n" +
                    "                            0.10377358490566038,\n" +
                    "                            0.10849056603773585,\n" +
                    "                            0.1179245283018868,\n" +
                    "                            0.13679245283018868,\n" +
                    "                            0.15566037735849056,\n" +
                    "                            0.17452830188679244\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -2.3505301177763855,\n" +
                    "                            -1.152826926464045,\n" +
                    "                            -1.0264130016083863,\n" +
                    "                            -0.017846993625060308,\n" +
                    "                            0.05684389897652889,\n" +
                    "                            0.13028319842061506,\n" +
                    "                            0.2742894291761008,\n" +
                    "                            0.5926084710897717,\n" +
                    "                            0.7987812437059061,\n" +
                    "                            1.13633514619786\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid5\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"55\",\n" +
                    "                            \"52\",\n" +
                    "                            \"51\",\n" +
                    "                            \"50\",\n" +
                    "                            \"45\",\n" +
                    "                            \"37\",\n" +
                    "                            \"30\",\n" +
                    "                            \"20\",\n" +
                    "                            \"11\",\n" +
                    "                            \"6\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.15406162464985995,\n" +
                    "                            0.14565826330532214,\n" +
                    "                            0.14285714285714285,\n" +
                    "                            0.1400560224089636,\n" +
                    "                            0.12605042016806722,\n" +
                    "                            0.10364145658263306,\n" +
                    "                            0.08403361344537816,\n" +
                    "                            0.056022408963585436,\n" +
                    "                            0.03081232492997199,\n" +
                    "                            0.01680672268907563\n" +
                    "                        ],\n" +
                    "                        \"isWoeMonotonic\": true,\n" +
                    "                        \"iv\": 2.251021092110662,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.5206428586470736,\n" +
                    "                            0.22225362754119488,\n" +
                    "                            0.18545541211999292,\n" +
                    "                            0.1546647652396469,\n" +
                    "                            0.055599425051389796,\n" +
                    "                            0.0008747084841677925,\n" +
                    "                            0.01801400696268557,\n" +
                    "                            0.13466241116020036,\n" +
                    "                            0.36338208125824173,\n" +
                    "                            0.595471795646068\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"1\",\n" +
                    "                            \"5\",\n" +
                    "                            \"6\",\n" +
                    "                            \"7\",\n" +
                    "                            \"12\",\n" +
                    "                            \"20\",\n" +
                    "                            \"27\",\n" +
                    "                            \"37\",\n" +
                    "                            \"46\",\n" +
                    "                            \"51\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.0047169811320754715,\n" +
                    "                            0.02358490566037736,\n" +
                    "                            0.02830188679245283,\n" +
                    "                            0.0330188679245283,\n" +
                    "                            0.05660377358490566,\n" +
                    "                            0.09433962264150944,\n" +
                    "                            0.12735849056603774,\n" +
                    "                            0.17452830188679244,\n" +
                    "                            0.2169811320754717,\n" +
                    "                            0.24056603773584906\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            -3.4861836781248443,\n" +
                    "                            -1.8206562990397004,\n" +
                    "                            -1.6189166563886441,\n" +
                    "                            -1.444963349265206,\n" +
                    "                            -0.8006063328746928,\n" +
                    "                            -0.09403613198260684,\n" +
                    "                            0.4157889914498002,\n" +
                    "                            1.13633514619786,\n" +
                    "                            1.951895630798351,\n" +
                    "                            2.6612156706038976\n" +
                    "                        ]\n" +
                    "                    },\n" +
                    "                    \"fid9\": {\n" +
                    "                        \"binNums\": \"10\",\n" +
                    "                        \"eventCountArray\": [\n" +
                    "                            \"22\",\n" +
                    "                            \"33\",\n" +
                    "                            \"45\",\n" +
                    "                            \"41\",\n" +
                    "                            \"37\",\n" +
                    "                            \"34\",\n" +
                    "                            \"44\",\n" +
                    "                            \"37\",\n" +
                    "                            \"35\",\n" +
                    "                            \"29\"\n" +
                    "                        ],\n" +
                    "                        \"eventRateArray\": [\n" +
                    "                            0.06162464985994398,\n" +
                    "                            0.09243697478991597,\n" +
                    "                            0.12605042016806722,\n" +
                    "                            0.11484593837535013,\n" +
                    "                            0.10364145658263306,\n" +
                    "                            0.09523809523809523,\n" +
                    "                            0.12324929971988796,\n" +
                    "                            0.10364145658263306,\n" +
                    "                            0.09803921568627451,\n" +
                    "                            0.08123249299719888\n" +
                    "                        ],\n" +
                    "                        \"iv\": 0.24669894977826287,\n" +
                    "                        \"ivArray\": [\n" +
                    "                            0.09445376408096214,\n" +
                    "                            0.00421010728628049,\n" +
                    "                            0.055599425051389796,\n" +
                    "                            0.021356568895619286,\n" +
                    "                            0.0002074461026263366,\n" +
                    "                            0.001726574282752986,\n" +
                    "                            0.04323174562574946,\n" +
                    "                            0.0008747084841677925,\n" +
                    "                            0.00032596390460088744,\n" +
                    "                            0.02471264606411368\n" +
                    "                        ],\n" +
                    "                        \"nonEventCountArray\": [\n" +
                    "                            \"34\",\n" +
                    "                            \"24\",\n" +
                    "                            \"12\",\n" +
                    "                            \"15\",\n" +
                    "                            \"21\",\n" +
                    "                            \"23\",\n" +
                    "                            \"13\",\n" +
                    "                            \"20\",\n" +
                    "                            \"22\",\n" +
                    "                            \"28\"\n" +
                    "                        ],\n" +
                    "                        \"nonEventRateArray\": [\n" +
                    "                            0.16037735849056603,\n" +
                    "                            0.11320754716981132,\n" +
                    "                            0.05660377358490566,\n" +
                    "                            0.07075471698113207,\n" +
                    "                            0.09905660377358491,\n" +
                    "                            0.10849056603773585,\n" +
                    "                            0.06132075471698113,\n" +
                    "                            0.09433962264150944,\n" +
                    "                            0.10377358490566038,\n" +
                    "                            0.1320754716981132\n" +
                    "                        ],\n" +
                    "                        \"woeArray\": [\n" +
                    "                            0.9564675783654721,\n" +
                    "                            0.20269577598909203,\n" +
                    "                            -0.8006063328746928,\n" +
                    "                            -0.48437235849447113,\n" +
                    "                            -0.0452459678131748,\n" +
                    "                            0.13028319842061506,\n" +
                    "                            -0.6980907693490979,\n" +
                    "                            -0.09403613198260684,\n" +
                    "                            0.05684389897652889,\n" +
                    "                            0.4860581872963565\n" +
                    "                        ]\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"module_name\": \"HeteroFeatureBinning\"\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }
        if (component_name.contains("selection")) {
            result = "{\n" +
                    "    \"data\": {\n" +
                    "        \"finalLeftCols\": {\n" +
                    "            \"leftCols\": {\n" +
                    "                \"fid5\": true\n" +
                    "            },\n" +
                    "            \"originalCols\": [\n" +
                    "                \"fid0\",\n" +
                    "                \"fid1\",\n" +
                    "                \"fid2\",\n" +
                    "                \"fid3\",\n" +
                    "                \"fid4\",\n" +
                    "                \"fid5\",\n" +
                    "                \"fid6\",\n" +
                    "                \"fid7\",\n" +
                    "                \"fid8\",\n" +
                    "                \"fid9\"\n" +
                    "            ]\n" +
                    "        },\n" +
                    "        \"results\": [\n" +
                    "            {\n" +
                    "                \"featureValues\": {\n" +
                    "                    \"fid0\": 7.630322,\n" +
                    "                    \"fid1\": 2.044484,\n" +
                    "                    \"fid2\": 7.591713,\n" +
                    "                    \"fid3\": 5.7475570000000005,\n" +
                    "                    \"fid4\": 6.2922080000000005,\n" +
                    "                    \"fid5\": 8.898517,\n" +
                    "                    \"fid6\": 8.35306,\n" +
                    "                    \"fid7\": 8.355283,\n" +
                    "                    \"fid8\": 8.553449,\n" +
                    "                    \"fid9\": 8.824813\n" +
                    "                },\n" +
                    "                \"filterName\": \"unique_value\",\n" +
                    "                \"leftCols\": {\n" +
                    "                    \"leftCols\": {\n" +
                    "                        \"fid0\": true,\n" +
                    "                        \"fid1\": true,\n" +
                    "                        \"fid2\": true,\n" +
                    "                        \"fid3\": true,\n" +
                    "                        \"fid4\": true,\n" +
                    "                        \"fid5\": true,\n" +
                    "                        \"fid6\": true,\n" +
                    "                        \"fid7\": true,\n" +
                    "                        \"fid8\": true,\n" +
                    "                        \"fid9\": true\n" +
                    "                    },\n" +
                    "                    \"originalCols\": [\n" +
                    "                        \"fid0\",\n" +
                    "                        \"fid1\",\n" +
                    "                        \"fid2\",\n" +
                    "                        \"fid3\",\n" +
                    "                        \"fid4\",\n" +
                    "                        \"fid5\",\n" +
                    "                        \"fid6\",\n" +
                    "                        \"fid7\",\n" +
                    "                        \"fid8\",\n" +
                    "                        \"fid9\"\n" +
                    "                    ]\n" +
                    "                }\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"filterName\": \"iv_value_thres\",\n" +
                    "                \"leftCols\": {\n" +
                    "                    \"leftCols\": {\n" +
                    "                        \"fid0\": false,\n" +
                    "                        \"fid1\": false,\n" +
                    "                        \"fid2\": false,\n" +
                    "                        \"fid3\": false,\n" +
                    "                        \"fid4\": false,\n" +
                    "                        \"fid5\": true,\n" +
                    "                        \"fid6\": false,\n" +
                    "                        \"fid7\": false,\n" +
                    "                        \"fid8\": false,\n" +
                    "                        \"fid9\": false\n" +
                    "                    },\n" +
                    "                    \"originalCols\": [\n" +
                    "                        \"fid0\",\n" +
                    "                        \"fid1\",\n" +
                    "                        \"fid2\",\n" +
                    "                        \"fid3\",\n" +
                    "                        \"fid4\",\n" +
                    "                        \"fid5\",\n" +
                    "                        \"fid6\",\n" +
                    "                        \"fid7\",\n" +
                    "                        \"fid8\",\n" +
                    "                        \"fid9\"\n" +
                    "                    ]\n" +
                    "                }\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"featureValues\": {\n" +
                    "                    \"fid5\": 4189359.1159706516\n" +
                    "                },\n" +
                    "                \"filterName\": \"coefficient_of_variation_value_thres\",\n" +
                    "                \"leftCols\": {\n" +
                    "                    \"leftCols\": {\n" +
                    "                        \"fid5\": true\n" +
                    "                    },\n" +
                    "                    \"originalCols\": [\n" +
                    "                        \"fid5\"\n" +
                    "                    ]\n" +
                    "                }\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"featureValues\": {\n" +
                    "                    \"fid5\": 1.794564\n" +
                    "                },\n" +
                    "                \"filterName\": \"outlier_cols\",\n" +
                    "                \"leftCols\": {\n" +
                    "                    \"leftCols\": {\n" +
                    "                        \"fid5\": true\n" +
                    "                    },\n" +
                    "                    \"originalCols\": [\n" +
                    "                        \"fid5\"\n" +
                    "                    ]\n" +
                    "                }\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    \"meta\": {\n" +
                    "        \"module_name\": \"HeteroFeatureSelection\"\n" +
                    "    },\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";

        }
        // FIXME: 2019/6/21
        if (component_name.contains("evaluation")) {
            result = "{\n" +
                    "    \"data\": null,\n" +
                    "    \"retcode\": 0,\n" +
                    "    \"retmsg\": \"OK\"\n" +
                    "}";
        }


        logger.info("result: " + result);
        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
        }

        JSONObject resultObject = JSON.parseObject(result);
        Integer retcode = resultObject.getInteger("retcode");
        if (retcode == 0) {


            return new ResponseResult<>(ErrorCode.SUCCESS, resultObject);

        } else {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "errorcode: " + retcode);
        }

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


//        String result =  httpClientPool.post(fateUrl+"/v1/tracking/component/output/data",param);

//        String result = ReadJson.readJsonFile("/data/projects/fate/fateboard/data_output.txt");
        String result = ReadJson.readJsonFile("C:\\Users\\kaideng\\myboard\\src\\main\\resources\\data_output.txt");

        logger.info(result);
        if (result == null || "".equals(result)) {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Network Error!");
        }





//        String fileName = "C:\\Users\\kaideng\\Desktop\\" + job_id + component_name + ".txt";
//        File fileName = new File(/data/projects/fate/fateboard/data_output/" + job_id + component_name+".txt");
//
//
//        FileWriter fileWriter;
//        try {
//
//            fileWriter = new FileWriter(fileName);
//            fileWriter.write(result);
//            fileWriter.flush();
//            fileWriter.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("io异常", e);
//        }
//
//        //存储转换json参数
//        Integer columnNum = jsonObject.getInteger("columnNum");
//        Integer rowNum = jsonObject.getInteger("rowNum");
//        ArrayList<String> columns = new ArrayList<>();
//        ArrayList<List> rows = new ArrayList<>();
//        HashMap<String, Object> dataOutput = new HashMap<>();
//
//
//
//        String output = ReadJson.readJsonFile(fileName);
//        JSONObject jsonObject1 = JSON.parseObject(output);
//
//        JSONObject meta = jsonObject1.getJSONObject("meta");
//        JSONArray header = meta.getJSONArray("header");
//        JSONArray data = jsonObject1.getJSONArray("data");
//        int l =header.size();
//
//        if(header.size()>10){
//            l=   10;
//        }
//
//
//
//        for(int i=0;i<l;i++){
//            String columnValue = header.getString(columnNum + i);
//            columns.add(columnValue);
//        }
//
//       for(int i =0;i<10; i++){
//           JSONArray rowArray = data.getJSONArray(i);
//           ArrayList<String> row = new ArrayList<>();
//           for (int j =0; j<header.size();j++){
//               String rowValue = rowArray.getString(rowNum + j);
//               row.add(rowValue);
//           }
//           rows.add(row);
//       }
//       dataOutput.put("headers",columns);
//       dataOutput.put("data",rows);
//       return new ResponseResult(ErrorCode.SUCCESS,dataOutput);


        JSONObject resultObject = JSON.parseObject(result);
        Integer retcode = resultObject.getInteger("retcode");
        if (retcode == 0) {
            return new ResponseResult<>(ErrorCode.SUCCESS, resultObject);
        } else {
            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "errorcode: " + retcode);
        }


    }


}
