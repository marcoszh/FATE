package com.webank.ai.fate.board.services;

import com.webank.ai.fate.board.dao.ProjectMapper;
import com.webank.ai.fate.board.pojo.Project;
import com.webank.ai.fate.board.pojo.ProjectExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProjectManagerService {
    @Autowired
    ProjectMapper projectMapper;

    public Integer createProject(String name, String type, String description) {
        Project project = new Project();
        project.setfName(name);
        project.setfType(type);
        project.setfDesc(description);
        Date date = new Date();
        project.setfCreateTime(date);
        projectMapper.insertSelective(project);
        return project.getfPid();
    }

    public List<Project> queryProject() {
        ProjectExample projectExample = new ProjectExample();

        return projectMapper.selectByExample(projectExample);
    }

    public void updateProject(Integer pid, String name, String type, String description) {
        Project project = new Project();
        project.setfPid(pid);
        project.setfName(name);
        project.setfType(type);
        project.setfDesc(description);
        Date date = new Date();
        project.setfUpdateTime(date);

        projectMapper.updateByPrimaryKey(project);

    }

    public void deleteProject(Integer pid) {
        projectMapper.deleteByPrimaryKey(pid);
    }
}
