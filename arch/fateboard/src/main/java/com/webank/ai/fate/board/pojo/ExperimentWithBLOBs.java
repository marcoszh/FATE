package com.webank.ai.fate.board.pojo;

public class ExperimentWithBLOBs extends Experiment {
    private String fDagConfig;

    private String fConfig;

    public String getfDagConfig() {
        return fDagConfig;
    }

    public void setfDagConfig(String fDagConfig) {
        this.fDagConfig = fDagConfig == null ? null : fDagConfig.trim();
    }

    public String getfConfig() {
        return fConfig;
    }

    public void setfConfig(String fConfig) {
        this.fConfig = fConfig == null ? null : fConfig.trim();
    }
}