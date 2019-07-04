package com.webank.ai.fate.board.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Description TODO
 **/
public class RandomFileScanner implements Runnable, LogScanner {

    private static final Logger logger = LoggerFactory.getLogger(RandomFileScanner.class);

    public RandomFileScanner(File file, Session session, long skipLine) {

        try {
            tailFile = new TailFile(file, 0);
        } catch (IOException e) {
            logger.error("file error", e);
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
            logger.info("roll file start");

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
                   // logger.info("roll file ============ {} ",lines.size());
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
                                    e.printStackTrace();
                                    logger.error("IOException", e);
                                }
                            } else {
                                needStop = true;
                                return ;
                            }

                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Exception", e);
                }
            }
        } finally {
            if (tailFile != null) {
                tailFile.close();
            }
        }

    }
}
