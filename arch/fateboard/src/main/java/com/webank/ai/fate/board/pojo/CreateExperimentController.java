package com.webank.ai.fate.board.pojo;

import com.google.common.collect.Maps;
import com.webank.ai.fate.board.services.ExperimentService;
import com.webank.ai.fate.board.utils.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author kaideng
 **/
@Controller
@RequestMapping(value = "/cgi-bin/create")
public class CreateExperimentController {
    @Autowired
    ExperimentService experimentService;

    public Map create(@RequestParam(value = "pid", required = true) Integer pid,
                      @RequestParam(value = "name", required = true) String name,
                      @RequestParam(value = "desc", required = true) String desc) {
        Map result = Maps.newHashMap();
        int eid = experimentService.create(pid, name, desc);
        Experiment experiment = experimentService.getById(eid);
        result.put(Dict.ID, eid);
        result.put(Dict.CREATE_TIME, experiment.getfCreateTime());
        return result;

    }

    public void save(@RequestParam(value = "eid", required = true) Integer eid,
                     @RequestParam(value = "dataset", required = true) String dataset,
                     @RequestParam(value = "partner_dataset", required = false) String partnerDataset,
                     @RequestParam(value = "dag_config", required = true) String dagConfig,
                     @RequestParam(value = "config", required = true) String config

    ) {
        this.experimentService.save(eid, dataset, partnerDataset, dagConfig, config);
    }

    public Experiment getById(@RequestParam(value = "eid", required = true) Integer id) {
        Experiment experiment = experimentService.getById(id);
        return experiment;
    }

    public List<Experiment> getByProjectId(@RequestParam(value = "pid", required = true) Integer pid) {
        ExperimentExample experimentExample = new ExperimentExample();
        experimentExample.createCriteria().andFPidEqualTo(pid);
        return this.experimentService.list(experimentExample);
    }


}
