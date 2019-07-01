package com.webank.ai.fate.board.pojo;

public class JobWithBLOBs extends Job {
    private String fDescription;

    private String fRoles;

    private String fDagConfig;

    private String fRunParameterConfig;

    public String getfDescription() {
        return fDescription;
    }

    public void setfDescription(String fDescription) {
        this.fDescription = fDescription == null ? null : fDescription.trim();
    }

    public String getfRoles() {
        return fRoles;
    }

    public void setfRoles(String fRoles) {
        this.fRoles = fRoles == null ? null : fRoles.trim();
    }

    public String getfDagConfig() {
        return fDagConfig;
    }

    public void setfDagConfig(String fDagConfig) {
        this.fDagConfig = fDagConfig == null ? null : fDagConfig.trim();
    }

    public String getfRunParameterConfig() {
        return fRunParameterConfig;
    }

    public void setfRunParameterConfig(String fRunParameterConfig) {
        this.fRunParameterConfig = fRunParameterConfig == null ? null : fRunParameterConfig.trim();
    }
}