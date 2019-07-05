package com.webank.ai.fate.board.services;

import com.webank.ai.fate.board.dao.JobMapper;
import com.webank.ai.fate.board.pojo.Job;
import com.webank.ai.fate.board.pojo.JobExample;
import com.webank.ai.fate.board.pojo.JobWithBLOBs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service

public class JobManagerService {
    private final Logger logger = LoggerFactory.getLogger(JobManagerService.class);

    @Autowired
    JobMapper jobMapper;

    public long count() {

        JobExample jobExample = new JobExample();
        return jobMapper.countByExample(jobExample);
    }

    public List<JobWithBLOBs> queryJobByPage(long startIndex, long pageSize) {
        List<JobWithBLOBs> jobWithBLOBs = jobMapper.selectByPage(startIndex, pageSize);
        return jobWithBLOBs;
    }


    public List<Job> queryJobStatus() {

        // logger.info("Start query for JobStatus!");

        JobExample jobExample = new JobExample();

        JobExample.Criteria criteria = jobExample.createCriteria();

        ArrayList<String> stringArrayList = new ArrayList<String>();

        stringArrayList.add("waiting");

        stringArrayList.add("running");

        criteria.andFStatusIn(stringArrayList);

        jobExample.setOrderByClause("f_status, f_start_time desc");

        //logger.info("jobExample:" + jobExample);

        return jobMapper.selectByExample(jobExample);

    }


    public List<JobWithBLOBs> queryJob() {
        //logger.info("Start querying for job!");

        JobExample jobExample = new JobExample();

        jobExample.setOrderByClause("f_start_time desc");

        //logger.info("jobExample：" + jobExample);

        List<JobWithBLOBs> jobWithBLOBsList = jobMapper.selectByExampleWithBLOBs(jobExample);

        return jobWithBLOBsList;

    }


    public JobWithBLOBs queryJobByFJobId(String jobId) {

        //logger.info("jobId:" + jobId);

        JobExample jobExample = new JobExample();


        JobExample.Criteria criteria = jobExample.createCriteria();


        criteria.andFJobIdEqualTo(jobId);

        // logger.info("jobExample：" + jobExample);


        List<JobWithBLOBs> jobWithBLOBsList = jobMapper.selectByExampleWithBLOBs(jobExample);


        if (jobWithBLOBsList.size() != 0) {
            return jobWithBLOBsList.get(0);
        } else {
            return null;
        }
    }


}