package com.webank.ai.fate.board.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.webank.ai.fate.board.conf.Configurator;
import com.webank.ai.fate.board.pojo.Job;
import com.webank.ai.fate.board.services.JobManagerService;
import com.webank.ai.fate.board.utils.Dict;
import com.webank.ai.fate.board.utils.HttpClientPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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


@ServerEndpoint(value = "/websocket/progress/{jobId}/{role}/{partyId}", configurator = Configurator.class)
@Component
public class JobWebSocketService implements InitializingBean, ApplicationContextAware {

    static HttpClientPool  httpClientPool;

    static JobManagerService jobManagerService;

    static ApplicationContext  applicationContext;

    static Logger logger = LoggerFactory.getLogger(JobWebSocketService.class);

    static ConcurrentHashMap jobSessionMap = new ConcurrentHashMap();

    private ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    /**
     * call method when building connection
     **/
    @OnOpen
    public void onOpen(Session session, @PathParam("jobId") String jobId,String  role,String partyId) {

        String  jobKey =  jobId+"\\|"+role+"\\|"+partyId;


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
        logger.error("there is a error!",error);
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
            String jobKey = (String) v;
            Set<Session> sessions = jobMaps.get(jobKey);
            if (sessions == null) {
                sessions = new HashSet<Session>();
            }
            sessions.add(session);
            jobMaps.put(jobKey, sessions);

        });
        if (logger.isDebugEnabled()) {
            logger.debug("job maps {}", jobMaps);
        }


        jobMaps.forEach((k, v) -> {
                   // logger.info("try to query job {} process", k);

                    String[] args = k.split("\\|");
                    Preconditions.checkArgument(args.length==3);
                    Job job = jobManagerService.queryJobByConditions(args[0],args[1],args[2]);
                    if (job != null) {
                        HashMap<String, Object> stringObjectHashMap = new HashMap<>(8);
                        Integer process = job.getfProgress();
                        long  now=  System.currentTimeMillis();
                        long duration =0;
                        Long startTime = job.getfStartTime();
                        Long endTime =job.getfEndTime();
                        if(endTime!=null)
                        {
                            duration= endTime-startTime;

                        }else{
                            duration = now - startTime;
                        }

                        String status = job.getfStatus();

                        stringObjectHashMap.put(Dict.JOB_PROCESS, process);
                        stringObjectHashMap.put(Dict.JOB_DURATION, duration);
                        stringObjectHashMap.put(Dict.JOB_STATUS, status);

                        if(JobManagerService.jobFinishStatus.contains(status)){


                        }
                        v.forEach(session -> {

                            if (session.isOpen()) {
                                try {
                                    session.getBasicRemote().sendText(JSON.toJSONString(stringObjectHashMap));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    logger.error("IOException", e);
                                }
                            } else {
                                v.remove(session);

                                jobSessionMap.remove(session);
                            }

                        });
                    }

                }
        );

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jobManagerService =(JobManagerService)applicationContext.getBean("jobManagerService");
        httpClientPool  =  (HttpClientPool)applicationContext.getBean("httpClientPool");

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JobWebSocketService.applicationContext =  applicationContext;

    }
}

