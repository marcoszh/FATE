//package com.webank.ai.fate.board.services;
//
//import com.google.common.collect.Sets;
//import com.webank.ai.fate.board.conf.Configurator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @Description TODO
// * @Author kaideng
// **/
//
//@ServerEndpoint(value="/log/{componentId}/{jobId}/{num}",configurator= Configurator.class)
//@Component
//public class LogWebSocketServiceBak {
//
//    static Logger logger= LoggerFactory.getLogger(LogWebSocketServiceBak.class);
//
//    private ConcurrentHashMap<String ,Set<LogScanner>> jobLogMap  = new  ConcurrentHashMap();
//
//    private ConcurrentHashMap<Session,LogScanner> sessionLogMap =  new ConcurrentHashMap();
//
//   // private ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//
//
//    /**
//     * 连接建立成功调用的方法*/
//    @OnOpen
//    public synchronized void onOpen(Session session, @PathParam("jobId") String jobId,
//                       @PathParam("componentId") String componentId,
//                                    @PathParam("num")  Integer num
//
//    ) {
//
//
//
//        String key = jobId+componentId;
//        if(jobLogMap.get(key)==null){
//            Set<LogScanner>  logScanners = Sets.newHashSet();
//            LogScanner  logScanner = new LogScanner(key,this);
//
//            if(jobLogMap.putIfAbsent(key,logScanners)==null){
//
//                logScanner.setSession(session);
//                logScanner.setCommand(buildCommand(jobId,componentId,num));
//                logScanner.start();
//                logScanners.add(logScanner);
//                sessionLogMap.put(session,logScanner);
//
//            }
//
//        }
//
//        logger.info("log socket open jobId {} componentId {} jobLogMap size {}  sessionMap size {}",jobId,componentId,jobLogMap.size(),sessionLogMap.size());
//
//    }
//
//    public  String buildCommand (String jobId,String componentId,int  endNum){
//
//       String  command = "tail " + " -n +" + (endNum + 1) + " -f " + buildFilePath(jobId,componentId);
//       return command;
//    }
//
//
//    private  String filePrefix = "/data/projects/fate/";
//
//    private  String  buildFilePath(String jobId,String componentId){
//
//
//         return  "/data/projects/fate/serving-server/logs/console.log";
//      //      return "/Users/kaideng/work/webank/code/fate-board/logs/borad/2019-05-30/info.0.log";
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public   void onClose(Session  session) {
//
//
//        closeInner(session);
//
//    }
//
//    public  synchronized void   closeInner(Session  session){
//
//        LogScanner  logScanner = sessionLogMap.get(session);
//        if(logScanner!=null){
//            logScanner.stop();
//            sessionLogMap.remove(session);
//            jobLogMap.get(logScanner.key).remove(logScanner);
//
//        }
//
//        logger.info("log socket closed  jobLogMap size {}  sessionMap size {}",jobLogMap.size(),sessionLogMap.size());
//
//    }
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param message 客户端发送过来的消息*/
//    @OnMessage
//    public void onMessage(String message, Session session) {
//
//
//
//    }
//
//    /**
//     *
//     * @param session
//     * @param error
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//        logger.error("发生错误");
//        error.printStackTrace();
//    }
//
//
//
//}
