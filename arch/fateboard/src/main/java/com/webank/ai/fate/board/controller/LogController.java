package com.webank.ai.fate.board.controller;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.webank.ai.fate.board.global.ErrorCode;
import com.webank.ai.fate.board.global.ResponseResult;
import com.webank.ai.fate.board.log.LogFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 **/
@Controller
public class LogController {
    private final Logger logger = LoggerFactory.getLogger(LogController.class);
    @Autowired
    LogFileService logFileService;

    public  com.google.common.cache.CacheLoader<String, List<Map>> createCacheLoader() {
        return new com.google.common.cache.CacheLoader<String, List<Map>>() {
            @Override
            public List<Map> load(String key) throws Exception {

                long  begin = System.currentTimeMillis();
                logger.info("===================load key: {}",key);
                String[] args = key.split("\\|");
                try {

                    return queryLog(args[0], args[1], args[2], new Integer(args[3]), new Integer(args[4]));
                }catch(Exception e){
                    e.printStackTrace();
                    logger.error("============== load cache error",e);
                }
                finally {
                    long  end = System.currentTimeMillis();

                    logger.info("============load cache cost {}",end-begin);
                }
                return null;

            }
        };
    }
    LoadingCache<String, List<Map> > cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(3L, TimeUnit.MILLISECONDS)
            .build(createCacheLoader());



    @RequestMapping(value = "/queryLogWithSizeSSH/{jobId}/{componentId}/{type}/{begin}/{end}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult queryLogWithSizeSSH(@PathVariable String componentId, @PathVariable String jobId,
                                              @PathVariable Integer begin, @PathVariable String type, @PathVariable Integer end) throws Exception {
        logger.info("parameters for " + "componentId:" + componentId + ", jobId:" + jobId + ", begin;" + begin + ", end:" + end + "type");

        String filePath = logFileService.buildFilePath(jobId, componentId, type);

        Preconditions.checkArgument(filePath != null && !filePath.equals(""));

        String ip = logFileService.getJobTaskInfo(jobId, componentId).ip;

        Preconditions.checkArgument(ip != null && !ip.equals(""));

        List<Map> logs = logFileService.getRemoteLogWithFixSize(jobId, componentId, type, begin, end - begin + 1);

        ResponseResult result = new ResponseResult();

        result.setData(logs);

        return result;

    }

    public long getLineNumber(File file) {
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
                lineNumberReader.skip(Long.MAX_VALUE);

                long lines = lineNumberReader.getLineNumber() + 1;
                fileReader.close();
                lineNumberReader.close();
                return lines;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    List<Map>  queryLog( String componentId,  String jobId, String type,
                         Integer begin,
                         Integer end) throws Exception {
        String filePath = logFileService.buildFilePath(jobId, componentId, type);

        Preconditions.checkArgument(filePath != null && !filePath.equals(""));

        if(LogFileService.checkFileIsExist(filePath)){

            RandomAccessFile file = null;
            List<Map> result = Lists.newArrayList();

//            if (begin > end || begin <= 0) {
//                return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters!");
//            }

            String[] cmd = { "sh", "-c", "tail -n +" + begin + " " + filePath +" | head -n " + (end -begin) };



            Process process =Runtime.getRuntime().exec(cmd);





            logger.error("process {}",process.toString());

            InputStream inputStream= process.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {

                String content = null;
                int index = 0;
                do {
                    content = reader.readLine();
                    if (content != null) {
                        result.add(LogFileService.toLogMap(content, begin + index));
                    }
                    index++;



                } while (content != null);

                logger.error("===========execute 1 cmd {} return count {}",cmd,index);
            }finally {
                if(inputStream!=null) {
                    inputStream.close();
                }
                if(process!=null){
                    process.destroyForcibly();
                }


            }
//            try {
//                int lineCount = 0;
//                int byteSize = 0;
//
//                file = new RandomAccessFile(filePath, "r");
//
//                String line = null;
//                do {
//                    line = file.readLine();
//                    if (line != null) {
//                        lineCount++;
//                        byteSize += line.getBytes().length;
//                        if (lineCount >= begin) {
//                            Map logMap = LogFileService.toLogMap(line, lineCount);
//                            result.add(logMap);
//                            logger.info("wrapped log information：" + logMap);
//                        }
//                    }
//                }
//                while (line != null && lineCount < end);





//            } catch (Throwable e) {
//                e.printStackTrace();
//                logger.error("Exception for building file stream!", e);
//            } finally {
//                if (file != null) {
//                    try {
//                        file.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        logger.error("Exception for closing file stream!", e);
//                    }
//                }
//            }

            return  result;

        }else{



            String ip = logFileService.getJobTaskInfo(jobId, componentId).ip;

            Preconditions.checkArgument(ip != null && !ip.equals(""));

            List<Map> logs = logFileService.getRemoteLogWithFixSize(jobId, componentId, type, begin, end - begin + 1);

            return  logs;

        }

    }


    @RequestMapping(value = "/queryLogWithSize/{jobId}/{componentId}/{type}/{begin}/{end}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult queryLogWithSize(@PathVariable String componentId, @PathVariable String jobId,
                                           @PathVariable String type,@PathVariable Integer begin,
                                           @PathVariable Integer end) throws Exception {

        logger.info("parameters for " + "componentId:" + componentId + ", jobId:" + jobId + ", begin;" + begin + ", end:" + end);

       // List<Map> result = queryLog(componentId,jobId,type,begin,end);
        StringBuilder  sb = new  StringBuilder();
        String  key =sb.append(componentId).append("|").append(jobId).append("|").append(type).append("|").
                append(begin).append("|").append(end).toString();
//        List<Map> result = cache.get(key);

        List<Map> result = this.queryLog(componentId, jobId, type, begin, end);

//        String filePath = logFileService.buildFilePath(jobId, componentId, type);
//
//        Preconditions.checkArgument(filePath != null && !filePath.equals(""));
//
//        logger.info("path for logfile：" + filePath);
//
//
//        RandomAccessFile file = null;
//        List<Map> result = Lists.newArrayList();
//
//        if (begin > end || begin <= 0) {
//            return new ResponseResult<>(ErrorCode.PARAM_ERROR, "Error for incoming parameters!");
//        }
//        try {
//            int lineCount = 0;
//            int byteSize = 0;
//
//            file = new RandomAccessFile(filePath, "r");
//
//            String line = null;
//            do {
//                line = file.readLine();
//                if (line != null) {
//                    lineCount++;
//                    byteSize += line.getBytes().length;
//                    if (lineCount >= begin) {
//                        Map logMap = LogFileService.toLogMap(line, lineCount);
//                        result.add(logMap);
//                        logger.info("wrapped log information：" + logMap);
//                    }
//                }
//            }
//            while (line != null && lineCount < end);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("Exception for building file stream!", e);
//        } finally {
//            if (file != null) {
//                try {
//                    file.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    logger.error("Exception for closing file stream!", e);
//                }
//            }
//        }


      //  logger.info("===================log result : {}",result);
        return new ResponseResult<>(ErrorCode.SUCCESS, result);
    }

//    private String getFilePath(String j) {
//
//        LogFileUtil.buildFilePath()
//
//        return "/data/projects/fate/serving-server/logs/console.log";
////            return "/Users/kaideng/work/webank/code/fate-board/logs/borad/2019-05-30/info.0.log";
////        return "E:\\workspace\\console.log";
//    }


//    public  static  void main(String[] args){
//
//        String  cmd ="tail -n +" + 100 + " " + "/tmp/test" +" | head -n " + 10;
//
//
//
//
//        try {
//            Runtime.getRuntime().e(cmd);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}
