package com.webank.ai.fate.board.pojo;

import java.util.Date;

public class ExperimentLib {
    private Integer fId;

    private Integer fEid;

    private Integer fDid;

    private Date fCreateTime;

    private Date fUpdateTime;

    public Integer getfId() {
        return fId;
    }

    public void setfId(Integer fId) {
        this.fId = fId;
    }

    public Integer getfEid() {
        return fEid;
    }

    public void setfEid(Integer fEid) {
        this.fEid = fEid;
    }

    public Integer getfDid() {
        return fDid;
    }

    public void setfDid(Integer fDid) {
        this.fDid = fDid;
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
}