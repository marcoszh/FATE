package com.webank.ai.fate.board.controller;

import com.webank.ai.fate.board.global.ErrorCode;
import com.webank.ai.fate.board.global.ResponseResult;
import com.webank.ai.fate.board.pojo.Project;
import com.webank.ai.fate.board.services.ProjectManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/project")
public class ProjectManagerController {
    @Autowired
    ProjectManagerService projectManagerService;

    /**
     * 创建project
     *
     * @param name
     * @param type
     * @param description
     * @return
     */
    @RequestMapping(value = "/create")
    public ResponseResult createProject(@RequestParam(value = "name", required = true) String name,
                                        @RequestParam(value = "type", required = true) String type,
                                        @RequestParam(value = "description", required = true) String description) {
        Integer fPid = projectManagerService.createProject(name, type, description);

        return new ResponseResult<Integer>(ErrorCode.SUCCESS, fPid);
    }

    /**
     * 查询所有project
     *
     * @return
     */
    @RequestMapping(value = "/query")
    public ResponseResult queryProject() {
        List<Project> projects = projectManagerService.queryProject();

        return new ResponseResult<List<Project>>(ErrorCode.SUCCESS, projects);
    }

    /**
     * 更新project
     *
     * @param pid
     * @param name
     * @param type
     * @param description
     * @return
     */
    public ResponseResult updateProject(@RequestParam(value = "pid", required = true) Integer pid,
                                        @RequestParam(value = "name", required = true) String name,
                                        @RequestParam(value = "type", required = true) String type,
                                        @RequestParam(value = "description", required = true) String description
    ) {

        projectManagerService.updateProject(pid, name, type, description);
        return new ResponseResult(ErrorCode.SUCCESS);

    }

    public ResponseResult deleteProject(@RequestParam(value = "pid", required = true) Integer pid) {
        projectManagerService.deleteProject(pid);
        return new ResponseResult(ErrorCode.SUCCESS);

    }
}
