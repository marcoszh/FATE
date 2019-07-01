package com.webank.ai.fate.board.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Project {
    @JsonProperty(value = "pid")
    private Integer fPid;

    @JsonProperty(value = "name")
    private String fName;

    @JsonProperty(value = "description")
    private String fDesc;

    @JsonProperty(value = "type")
    private String fType;

    @JsonProperty(value = "status")
    private Short fStatus;

    @JsonProperty(value = "create_time")
    private Date fCreateTime;

    @JsonProperty(value = "update_time")
    private Date fUpdateTime;

    @JsonProperty(value = "content")
    private String fContent;

    public Integer getfPid() {
        return fPid;
    }

    public void setfPid(Integer fPid) {
        this.fPid = fPid;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName == null ? null : fName.trim();
    }

    public String getfDesc() {
        return fDesc;
    }

    public void setfDesc(String fDesc) {
        this.fDesc = fDesc == null ? null : fDesc.trim();
    }

    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType == null ? null : fType.trim();
    }

    public Short getfStatus() {
        return fStatus;
    }

    public void setfStatus(Short fStatus) {
        this.fStatus = fStatus;
    }

    public Date getfCreateTime() {
        return fCreateTime;
    }

    public void setfCreateTime(Date fCreateTime) {
        this.fCreateTime = fCreateTime;
    }

    public Date getfUpdateTime() {
        return fUpdateTime;
    }

    public void setfUpdateTime(Date fUpdateTime) {
        this.fUpdateTime = fUpdateTime;
    }

    public String getfContent() {
        return fContent;
    }

    public void setfContent(String fContent) {
        this.fContent = fContent == null ? null : fContent.trim();
    }
}