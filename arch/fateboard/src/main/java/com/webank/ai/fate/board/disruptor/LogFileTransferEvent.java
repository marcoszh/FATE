package com.webank.ai.fate.board.disruptor;

import com.webank.ai.fate.board.pojo.SshInfo;

public class LogFileTransferEvent {

    public SshInfo getSshInfo() {
        return sshInfo;
    }

    public void setSshInfo(SshInfo sshInfo) {
        this.sshInfo = sshInfo;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public void setSourceFilePath(String sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
    }

    public String getDesFilePath() {
        return desFilePath;
    }

    public void setDesFilePath(String desFilePath) {
        this.desFilePath = desFilePath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    SshInfo sshInfo;

    String sourceFilePath;

    String desFilePath;

    int status = 0;

    public LogFileTransferEvent() {

    }

    public LogFileTransferEvent(
            SshInfo sshInfo,
            String sourceFilePath,
            String desFilePath) {
        this.sshInfo = sshInfo;
        this.sourceFilePath = sourceFilePath;
        this.desFilePath = desFilePath;
    }
}