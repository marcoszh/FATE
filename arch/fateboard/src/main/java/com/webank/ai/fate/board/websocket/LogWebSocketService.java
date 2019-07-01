//package com.webank.ai.fate.board.websocket;
//
//import com.webank.ai.fate.board.conf.Configurator;
//import com.webank.ai.fate.board.services.LogScannerRandomFile;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.LineNumberReader;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * @Description TODO
// **/
//
//@ServerEndpoint(value = "/log/{componentId}/{jobId}/{num}", configurator = Configurator.class)
//@Component
//public class LogWebSocketService implements InitializingBean {
//
//    //    @Value("${file.tailNum:10}")
//    private Integer tailNum = 10;
//
//    private static final Logger logger = LoggerFactory.getLogger(LogWebSocketService.class);
//
//
//    private ConcurrentHashMap<Session, LogScannerRandomFile> sessionLogMap = new ConcurrentHashMap();
//
//    // private ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//
//    ExecutorService executorService = Executors.newCachedThreadPool();
//
//
//    /**
//     * call method when connection build
//     */
//    @OnOpen
//    public synchronized void onOpen(Session session, @PathParam("jobId") String jobId,
//                                    @PathParam("componentId") String componentId,
//                                    @PathParam("num") Integer num
//    ) throws Exception {
//        logger.info("jobId:{}, componentId:{}, num:{}", jobId, componentId, num);
//
//        String filePath = this.buildFilePath(jobId, componentId);
//
//        File file = new File(filePath);
//
//        boolean exist = file.exists();
//
//        if (!exist) {
//            throw new Exception("file not exist");
//        }
//        long lines = getFileLineNumber(file);
//
//        logger.info("lines:" + lines + " tailNum:" + tailNum);
//
//        System.err.println("lines =========================" + lines + " " + tailNum);
//
//        long skipLines = 0;
//
//        if (lines > tailNum) {
//            skipLines = lines - tailNum;
//        }
//// FIXME: 2019/6/12  ???
//        LogScannerRandomFile logScannerRandomFile = new LogScannerRandomFile(file, session, skipLines);
//
//        logger.info("logScannerRandowFile:" + logScannerRandomFile);
//
//        sessionLogMap.put(session, logScannerRandomFile);
//
//        Thread thread = new Thread(logScannerRandomFile);
//
//        thread.start();
//
//        logger.info("thread start:" + thread);
//
//
//    }
//
//
//    private int getFileLineNumber(File file) throws IOException {
//        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
//
//// FIXME: 2019/6/12 lineNo +1?
//        lnr.skip(Long.MAX_VALUE);
//        int lineNo = lnr.getLineNumber();
//        lnr.close();
//        return lineNo;
//    }
//
//
//    // FIXME: 2019/6/12
//    private String buildFilePath(String jobId, String componentId) {
//
//
//        return "/data/projects/fate/serving-server/logs/console.log";
////           return "/Users/kaideng/work/webank/code/fate-board/logs/borad/2019-05-30/info.0.log";
////        return "E:\\workspace\\console.log";
//    }
//
//    /**
//     * call method when connection close
//     */
//    @OnClose
//    public void onClose(Session session) {
//        this.closeInner(session);
//
//    }
//
//    public synchronized void closeInner(Session session) {
//
//        LogScannerRandomFile logScanner = sessionLogMap.get(session);
//        if (logScanner != null) {
//            logScanner.setNeedStop(true);
//            sessionLogMap.remove(session);
//
//            logger.info("release session :" + session);
//
//        }
//
//
//    }
//
//    /**
//     * call method when receive message from client
//     *
//     * @param message message from client
//     */
//    @OnMessage
//    public void onMessage(String message, Session session) {
//
//    }
//
//    /**
//     * call method when error occurs
//     *
//     * @param session
//     * @param error
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//        logger.error("there is a error");
//        error.printStackTrace();
//    }
//
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        System.err.println(this.tailNum);
//
//        logger.info("tailNum:" + tailNum);
//    }
//
//
//
//    private String filePrefix = "/data/projects/fate/";
//}
