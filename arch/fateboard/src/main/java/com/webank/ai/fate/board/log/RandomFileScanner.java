package com.webank.ai.fate.board.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class RandomFileScanner implements Runnable, LogScanner {

    private static final Logger logger = LoggerFactory.getLogger(RandomFileScanner.class);

    public RandomFileScanner(File file, Session session, long skipLine) {

        try {
            tailFile = new TailFile(file, 0);
        } catch (IOException e) {
            logger.error("init RandomFileScanner failed", e);

        }
        this.session = session;
        this.skipLine = skipLine;
    }

    long skipLine;

    TailFile tailFile;

    long flushNum;

    Session session;

    public TailFile getTailFile() {
        return tailFile;
    }

    public void setTailFile(TailFile tailFile) {
        this.tailFile = tailFile;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public boolean isNeedStop() {
        return needStop;
    }

    @Override
    public void setNeedStop(boolean needStop) {
        this.needStop = needStop;
    }

    boolean needStop = false;

    @Override
    public void run() {
        try {
            while (true) {

                try {
                    if (needStop) {
                        logger.info("roll file thread return");
                        return;
                    }
                    List<String> lines = tailFile.readEvents(100);

                    if (lines == null) {
                        throw new Exception("lines not exist");
                    }
                    if (lines.size() == 0) {
                        Thread.sleep(500);
                    } else {
                        lines.forEach(content -> {

                            flushNum++;
                            if (session.isOpen()) {
                                try {
                                    if (flushNum > skipLine) {

                                        session.getBasicRemote().sendText(content);
                                    }
                                } catch (IOException e) {

                                    logger.error("IOException", e);
                                }
                            } else {
                                needStop = true;
                                return ;
                            }

                        });
                    }
                } catch (Exception e) {
                    logger.error("roll local file error", e);
                }
            }
        } finally {
            if (tailFile != null) {
                tailFile.close();
            }
        }

    }
}
