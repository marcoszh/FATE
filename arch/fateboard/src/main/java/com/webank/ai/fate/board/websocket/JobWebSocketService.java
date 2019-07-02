package com.webank.ai.fate.board.websocket;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.webank.ai.fate.board.conf.Configurator;
import com.webank.ai.fate.board.pojo.Job;
import com.webank.ai.fate.board.services.JobManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@ServerEndpoint(value = "/websocket/progress/{jobId}", configurator = Configurator.class)
@Component
public class JobWebSocketService {
    @Autowired
    JobManagerService jobManagerService;

    static Logger logger = LoggerFactory.getLogger(JobWebSocketService.class);


    static ConcurrentHashMap jobSessionMap = new ConcurrentHashMap();

    private ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    /**
     * call method when building connection
     **/
    @OnOpen
    public void onOpen(Session session, @PathParam("jobId") String jobId) {


        jobSessionMap.put(session, jobId);

        logger.info("websocket job id {} open ,session size{}", jobId, jobSessionMap.size());

    }

    /**
     * call method when closing connection
     */
    @OnClose
    public void onClose(Session session) {

        logger.info("websocket session closed");
        jobSessionMap.remove(session);
    }

    /**
     * call method when receiving message from client
     *
     * @param message message from client
     */
    @OnMessage
    public void onMessage(String message, Session session) {


    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("there is a error!");
        error.printStackTrace();
    }

    @Scheduled(fixedRate = 1000)
    private void schedule() {

        Set<Map.Entry> entrySet = jobSessionMap.entrySet();
        if (logger.isDebugEnabled()) {
            logger.debug("job process schedule start,session map size {}", jobSessionMap.size());
        }
        Map<String, Set<Session>> jobMaps = Maps.newHashMap();
        jobSessionMap.forEach((k, v) -> {
            Session session = (Session) k;
            String jobId = (String) v;
            Set<Session> sessions = jobMaps.get(jobId);
//            if (sessions != null) {
//                sessions.add(session);
//            } else {
//                sessions = new HashSet<Session>();
//                sessions.add(session);
//            }
            if (sessions == null) {
                sessions = new HashSet<Session>();
            }
            sessions.add(session);
            jobMaps.put(jobId, sessions);

        });
        if (logger.isDebugEnabled()) {
            logger.debug("job maps {}", jobMaps);
        }


        jobMaps.forEach((k, v) -> {
                   // logger.info("try to query job {} process", k);
                    Job job = jobManagerService.queryJobByFJobId(k);
                    if (job != null) {
                        HashMap<String, Object> stringObjectHashMap = new HashMap<>(8);
                        Integer process = job.getfProgress();
                        Long time = job.getfElapsed();
                        String status = job.getfStatus();

                        stringObjectHashMap.put("process", process);
                        stringObjectHashMap.put("time", time);
                        stringObjectHashMap.put("status", status);

                       // logger.info("jobStatus: " + JSON.toJSONString(stringObjectHashMap));

                        v.forEach(session -> {

                            if (session.isOpen()) {
                                try {
                                    session.getBasicRemote().sendText(JSON.toJSONString(stringObjectHashMap));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    logger.error("IOException", e);
                                }
                            } else {
                                //???
                                v.remove(session);

                                jobSessionMap.remove(session);
                            }

                        });
                    }

                }
        );

    }
}

