package com.webank.ai.fate.board.pojo;

public class DataSource {
    private Integer fId;

    private String fPartyId;

    private String fLibrary;

    private String fName;

    private String fDataType;

    private Integer fSize;

    private Integer fRowNum;

    private Integer fColumnNum;

    private Integer fUploadTime;

    public Integer getfId() {
        return fId;
    }

    public void setfId(Integer fId) {
        this.fId = fId;
    }

    public String getfPartyId() {
        return fPartyId;
    }

    public void setfPartyId(String fPartyId) {
        this.fPartyId = fPartyId == null ? null : fPartyId.trim();
    }

    public String getfLibrary() {
        return fLibrary;
    }

    public void setfLibrary(String fLibrary) {
        this.fLibrary = fLibrary == null ? null : fLibrary.trim();
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName == null ? null : fName.trim();
    }

    public String getfDataType() {
        return fDataType;
    }

    public void setfDataType(String fDataType) {
        this.fDataType = fDataType == null ? null : fDataType.trim();
    }

    public Integer getfSize() {
        return fSize;
    }

    public void setfSize(Integer fSize) {
        this.fSize = fSize;
    }

    public Integer getfRowNum() {
        return fRowNum;
    }

    public void setfRowNum(Integer fRowNum) {
        this.fRowNum = fRowNum;
    }

    public Integer getfColumnNum() {
        return fColumnNum;
    }

    public void setfColumnNum(Integer fColumnNum) {
        this.fColumnNum = fColumnNum;
    }

    public Integer getfUploadTime() {
        return fUploadTime;
    }

    public void setfUploadTime(Integer fUploadTime) {
        this.fUploadTime = fUploadTime;
    }
}