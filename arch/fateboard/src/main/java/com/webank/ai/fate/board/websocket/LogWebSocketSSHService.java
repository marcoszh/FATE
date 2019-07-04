package com.webank.ai.fate.board.websocket;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.webank.ai.fate.board.conf.Configurator;
import com.webank.ai.fate.board.disruptor.LogFileTransferEventProducer;
import com.webank.ai.fate.board.log.LogFileService;
import com.webank.ai.fate.board.log.LogScanner;
import com.webank.ai.fate.board.log.RandomFileScanner;
import com.webank.ai.fate.board.pojo.SshInfo;
import com.webank.ai.fate.board.ssh.SftpUtils;
import com.webank.ai.fate.board.ssh.SshLogScanner;
import com.webank.ai.fate.board.ssh.SshService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description TODO
 **/

@ServerEndpoint(value = "/log/{jobId}/{componentId}/{type}", configurator = Configurator.class)
@Component
public class LogWebSocketSSHService implements InitializingBean, ApplicationContextAware {

    final String DEFAULT_COMPONENT_ID = "default";

    static ApplicationContext applicationContext;

    static SshService sshService;

    static LogFileService logFileService;

    private Integer tailNum = 10;

   // success/failed/partial/setFailed

    static Set<String> jobFinishStatus =  new  HashSet<String>(){
        {
            add("success");
            add("failed");
            add("partial");
            add("setFailed");
        }
    };

    private static final Logger logger = LoggerFactory.getLogger(LogWebSocketSSHService.class);

    static LogFileTransferEventProducer logFileTransferEventProducer;
    // private ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    ExecutorService executorService = Executors.newCachedThreadPool();


    Map<Session, LogScanner> sessionMap = Maps.newHashMap();


    public void test() {

        SshInfo sshInfo = sshService.getSSHInfo("localhost");

        SftpUtils.batchDownLoadFile(sshInfo, "/Users/kaideng/test/", "/Users/kaideng/test2/", null, null, false);


    }


    /**
     * call method when connection build
     */
    @OnOpen
    public synchronized void onOpen(Session session, @PathParam("jobId") String jobId,
                                    @PathParam("componentId") String componentId,
                                    @PathParam("type") String type

    ) throws Exception {

        logger.info("jobId:{}, componentId:{}, type:{}", jobId, componentId, type);

        //test();

        String filePath = logFileService.buildFilePath(jobId, componentId, type);

        Preconditions.checkArgument(filePath != null, "file path is null");

        /**
         * 本地文件
         */
         if(LogFileService.checkFileIsExist(filePath)){
       //if (false) {
            File file = new File(filePath);

            long lines = LogFileService.getLocalFileLineCount(file);

            logger.info("local file {} has {} line",filePath,lines);

            long skipLines = 0;

            if (lines > tailNum) {
                skipLines = lines - tailNum;
            }
            RandomFileScanner randomFileScanner = new RandomFileScanner(file, session, skipLines);

            sessionMap.put(session, randomFileScanner);

            Thread thread = new Thread(randomFileScanner);

            thread.start();

            return;

        } else {
            /**
             * 远程文件
             */

            LogFileService.JobTaskInfo jobTaskInfo = logFileService.getJobTaskInfo(jobId, componentId);

            SshInfo sshInfo = sshService.getSSHInfo(jobTaskInfo.ip);
          // SshInfo  sshInfo =  sshService.getSSHInfo("10.107.116.39");


            Preconditions.checkArgument(jobTaskInfo.ip != null && !jobTaskInfo.ip.equals(""));
//
            Preconditions.checkArgument(sshInfo != null);



            /**
             *   success/failed/partial/setFailed
             */

            //logger.info("job stauts {}",jobTaskInfo.jobStatus);
             if(jobFinishStatus.contains(jobTaskInfo.jobStatus)){
            //if (true) {
                String jobDir = logFileService.getJobDir(jobId);
                logFileTransferEventProducer.onData(sshInfo, jobDir, jobDir);

            }

            SshLogScanner sshLogScanner = new SshLogScanner(session, logFileService, sshInfo, jobId, componentId, type);

            sessionMap.put(session, sshLogScanner);

            Thread thread = new Thread((Runnable) sshLogScanner);

            thread.start();

        }

    }

    /**
     * call method when connection close
     */
    @OnClose
    public void onClose(Session session) {

        logger.info("websocket session {} closed", session);

        LogScanner logScanner = sessionMap.get(session);

        if (logScanner != null) {
            logScanner.setNeedStop(true);
        }

        sessionMap.remove(session);
    }


    /**
     * call method when receive message from client
     *
     * @param message message from client
     */
    @OnMessage
    public void onMessage(String message, Session session) {

    }

    /**
     * call method when error occurs
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("there is a error");
        error.printStackTrace();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        LogWebSocketSSHService.logFileService = (LogFileService) applicationContext.getBean("logFileService");
        LogWebSocketSSHService.sshService = (SshService) applicationContext.getBean("sshService");
        //    LogWebSocketSSHService.applicationEventPublisher  =  (ApplicationEventPublisher)applicationContext.getBean(ApplicationEventPublisher.class);
        LogWebSocketSSHService.logFileTransferEventProducer = (LogFileTransferEventProducer) applicationContext.getBean("logFileTransferEventProducer");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LogWebSocketSSHService.applicationContext = applicationContext;
    }




}
