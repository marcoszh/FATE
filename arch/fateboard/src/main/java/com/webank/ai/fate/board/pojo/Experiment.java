package com.webank.ai.fate.board.pojo;

import java.util.Date;

public class Experiment {
    private Integer fEid;

    private Integer fPid;

    private String fEname;

    private String fEdesc;

    private String fDataset;

    private String fPartnerDataset;

    private Date fCreateTime;

    private Date fUpdateTime;

    private Short fStatus;

    public Integer getfEid() {
        return fEid;
    }

    public void setfEid(Integer fEid) {
        this.fEid = fEid;
    }

    public Integer getfPid() {
        return fPid;
    }

    public void setfPid(Integer fPid) {
        this.fPid = fPid;
    }

    public String getfEname() {
        return fEname;
    }

    public void setfEname(String fEname) {
        this.fEname = fEname == null ? null : fEname.trim();
    }

    public String getfEdesc() {
        return fEdesc;
    }

    public void setfEdesc(String fEdesc) {
        this.fEdesc = fEdesc == null ? null : fEdesc.trim();
    }

    public String getfDataset() {
        return fDataset;
    }

    public void setfDataset(String fDataset) {
        this.fDataset = fDataset == null ? null : fDataset.trim();
    }

    public String getfPartnerDataset() {
        return fPartnerDataset;
    }

    public void setfPartnerDataset(String fPartnerDataset) {
        this.fPartnerDataset = fPartnerDataset == null ? null : fPartnerDataset.trim();
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

    public Short getfStatus() {
        return fStatus;
    }

    public void setfStatus(Short fStatus) {
        this.fStatus = fStatus;
    }
}