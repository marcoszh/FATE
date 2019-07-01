package com.webank.ai.fate.board.services;

import com.webank.ai.fate.board.dao.ExperimentMapper;
import com.webank.ai.fate.board.pojo.Experiment;
import com.webank.ai.fate.board.pojo.ExperimentExample;
import com.webank.ai.fate.board.pojo.ExperimentWithBLOBs;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description TODO
 * @Author kaideng
 **/
@Component
public class ExperimentService {

    ExperimentMapper experimentMapper;

    /*
        create  experiment
     */
    public Integer create(Integer pid, String name, String desc) {

        ExperimentWithBLOBs experimentWithBLOBs = new ExperimentWithBLOBs();
        experimentWithBLOBs.setfEdesc(desc);
        experimentWithBLOBs.setfEname(name);
        experimentWithBLOBs.setfPid(pid);
        return experimentMapper.insertSelective(experimentWithBLOBs);
    }

    public void save(Integer eid, String dataset,
                     String partner_dataset,
                     String dag_config,
                     String config) {

        ExperimentWithBLOBs experimentWithBLOBs = new ExperimentWithBLOBs();

        experimentWithBLOBs.setfEid(eid);
        experimentWithBLOBs.setfDataset(dataset);
        experimentWithBLOBs.setfPartnerDataset(partner_dataset);
        experimentWithBLOBs.setfDagConfig(dag_config);
        experimentWithBLOBs.setfConfig(config);

        experimentMapper.updateByPrimaryKeySelective(experimentWithBLOBs);

    }

    public Experiment getById(Integer id) {

        return experimentMapper.selectByPrimaryKey(id);

    }

    public List<Experiment> list(ExperimentExample experimentExample) {

        return experimentMapper.selectByExample(experimentExample);
    }


}
