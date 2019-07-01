//
//
//package com.webank.ai.fate.board.services;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//
//import com.google.common.base.Preconditions;
//import com.google.common.util.concurrent.ThreadFactoryBuilder;
//
//import javax.websocket.Session;
//import java.nio.charset.Charset;
//
//
//public class LogScanner {
//
//    //      command = "tail " + " -n +" + (endNum + 1) + " -f " + filePath;
//    String key;
//
//    public LogScanner(String key, LogWebSocketService logWebSocketService) {
//
//        this.key = key;
//
//        charset = Charset.forName("UTF-8");
//        this.logWebSocketService = logWebSocketService;
//
//
//    }
//
//    private LogWebSocketService logWebSocketService;
//
//    public Session getSession() {
//        return session;
//    }
//
//    public void setSession(Session session) {
//        this.session = session;
//    }
//
//    private Session session;
//
//
//    public Object lock = new Object();
//
//    volatile boolean isStopping = false;
//
//    private static final Logger logger = LoggerFactory.getLogger(LogScanner.class);
//
//    private String command;
//
//    private ExecRunnable runner;
//
//    public String getCommand() {
//        return command;
//    }
//
//    public void setCommand(String command) {
//        this.command = command;
//    }
//
//
//    public ExecRunnable getRunner() {
//        return runner;
//    }
//
//    public void setRunner(ExecRunnable runner) {
//        this.runner = runner;
//    }
//
//    public Charset getCharset() {
//        return charset;
//    }
//
//    public void setCharset(Charset charset) {
//        this.charset = charset;
//    }
//
//
//    private Charset charset;
//
//    private ExecutorService executor;
//
//    private Future runnerFuture;
//
//
//    public void start() {
//        logger.info("Exec source starting with command:{}", command);
//        executor = Executors.newSingleThreadExecutor();
//        runner = new ExecRunnable(command, charset, this);
//
//        // FIXME: Use a callback-like executor / future to signal us upon failure.
//        runnerFuture = executor.submit(runner);
//
//
//        logger.debug("Exec source started");
//    }
//
//
//    public void stop() {
//
//
//        logger.info("Stopping exec source with command:{}", command);
//
//        if (isStopping)
//            return;
//
//        isStopping = true;
//
//
//        if (runnerFuture != null) {
//            logger.debug("Stopping exec runner");
//            runnerFuture.cancel(true);
//            logger.debug("Exec runner stopped");
//        }
//        // executor.shutdown()
//        try {
//            session.close();
//            // logWebSocketService.closeInner(session);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        executor.shutdownNow();
//
//        while (!executor.isTerminated()) {
//            logger.debug("Waiting for exec executor service to stop");
//            try {
//                executor.awaitTermination(500, TimeUnit.MILLISECONDS);
//
//            } catch (InterruptedException e) {
//                logger.debug("Interrupted while waiting for exec executor service " + "to stop. Just exiting.");
//                Thread.currentThread().interrupt();
//            }
//        }
//
//
//    }
//
//
//    static class ExecRunnable implements Runnable {
//
//        public ExecRunnable(String command,
//                            Charset charset, LogScanner logScanner) {
//            this.logScanner = logScanner;
//            this.command = command;
//
//
//            this.charset = charset;
//
//
//        }
//
//        private final LogScanner logScanner;
//
//        private final String command;
//
//
//        private final Charset charset;
//        private Process process = null;
//
//
//        @Override
//        public void run() {
//
//
//            String exitCode = "unknown";
//            BufferedReader reader = null;
//            String line = null;
//            final List<String> eventList = new ArrayList<String>();
//
//            try {
//                synchronized (logScanner.lock) {
//
//                    if (logScanner.isStopping)
//                        return;
//                    String[] commandArgs = command.split("\\s+");
//
//                    process = new ProcessBuilder(commandArgs).start();
//
//
//                    reader = new BufferedReader(new InputStreamReader(process.getInputStream(), charset));
//
//                }
//                while (reader != null && (line = reader.readLine()) != null) {
//
//
//                    if (logScanner.getSession() != null) {
//                        if (logScanner.getSession().isOpen()) {
//
//                            //synchronized (logScanner.getSession()) {
//                            logScanner.getSession().getBasicRemote().sendText(line + System.currentTimeMillis());
//
//
//                            //}
//                            // logScanner.getSession().getBasicRemote().flushBatch();
//                        } else {
//                            logger.error("session close ,prepare to stop");
//                            logScanner.logWebSocketService.closeInner(logScanner.getSession());
//                            break;
//                        }
//
//                    } else {
//                        logger.error("session is null");
//                    }
//
//                }
//
//
//            } catch (Exception e) {
//                logger.error("Failed while running command: " + command, e);
//                if (e instanceof InterruptedException) {
//                    Thread.currentThread().interrupt();
//                }
//            } finally {
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException ex) {
//                        logger.error("Failed to close reader for exec source", ex);
//                    }
//                }
//
//
//                exitCode = String.valueOf(kill());
//
//                try {
//                    logScanner.getSession().close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//        }
//
//
//        private static String[] formulateShellCommand(String shell, String command) {
//            String[] shellArgs = shell.split("\\s+");
//            String[] result = new String[shellArgs.length + 1];
//            System.arraycopy(shellArgs, 0, result, 0, shellArgs.length);
//            result[shellArgs.length] = command;
//            return result;
//        }
//
//        public int kill() {
//
//
//            //logScanner.stop();
//
//
//            synchronized (logScanner.lock) {
//
//
//                logger.info("prepare  to  kill  process ");
//                if (process != null) {
//
//
//                    //  这个地方应该改为强制停止
//                    logger.info("process  prepare destroyForcibly   ++");
//                    process.destroyForcibly();
//                    logger.info("process destroyForcibly  over ");
//                    try {
//
//                        boolean exit = process.waitFor(30, TimeUnit.SECONDS);
//
//                        int exitValue = 0;
//                        if (exit) {
//                            exitValue = process.exitValue();
//                        } else {
//
//
//                            Process pp = process.destroyForcibly();
//
//                            if (pp != null)
//
//                                exitValue = pp.exitValue();
//
//                            logger.error("process destroyForcibly  exitValue {}", exitValue);
//                        }
//
//
//                        return exitValue;
//                    } catch (InterruptedException ex) {
//                        Thread.currentThread().interrupt();
//                    }
//                    // }
//                    return Integer.MIN_VALUE;
//                } else {
//                    logger.info("process is null");
//                }
//
//                return Integer.MIN_VALUE / 2;
//            }
//        }
//    }
//
//    ;
//
//
//    public static void main(String[] args) {
//
//        //command = "tail " + " -n +" + (endNum + 1) + " -f " + filePath;
////        LogScanner  logScanner = new  LogScanner();
////        String command = "tail -n +1 -f /Users/kaideng/work/hhhhhh.txt";
////        logScanner.setCommand(command);
////
////        logScanner.start();
////
////
////        try {
////            Thread.sleep(10000);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////
////        logScanner.stop();;
//
//
//    }
//}
