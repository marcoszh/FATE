package com.webank.ai.fate.board.utils;

/**
 * @Description TODO
 * @Author kaideng
 **/
public class Dict {

    static public String ID = "id";
    static public String NAME = "name";
    static public String DESC = "desc";
    static public String JOBID="job_id";
    static public String RETCODE="retcode";
    static public String DATA ="data";
    static public String JOB="job";
    static public String DATASET="dataset";
    static public String COMPONENT_NAME="component_name";


    static public String METRIC_NAMESPACE="metric_namespace";
    static public String METRIC_NAME="metric_name";
    static public String STATUS = "status";







    static public String CREATE_TIME = "create_time";
    static public String SSH_CONFIG_FILE = "ssh_config_file";
    static public String LOG_LINE_NUM = "lineNum";
    static public String LOG_CONTENT ="content";

    static public String URL_COPONENT_METRIC_DATA="/v1/tracking/component/metric_data";
    static public String URL_COPONENT_METRIC ="/v1/tracking/component/metrics";
    static public String URL_COPONENT_PARAMETERS ="/v1/tracking/component/parameters";
    static public String URL_DAG_DEPENDENCY="/v1/pipeline/dag/dependency";
    static public String URL_OUTPUT_MODEL="/v1/tracking/component/output/model";
    static public String URL_OUTPUT_DATA= "/v1/tracking/component/output/data";
    static public String URL_JOB_DATAVIEW="/v1/tracking/job/data_view";
    static public String URL_JOB_STOP="/v1/pipeline/job/stop";

}
