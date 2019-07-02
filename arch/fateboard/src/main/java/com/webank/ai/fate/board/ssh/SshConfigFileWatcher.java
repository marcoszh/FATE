package com.webank.ai.fate.board.ssh;

import com.google.common.collect.Maps;
import com.webank.ai.fate.board.utils.Dict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.Executors;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

@Component
class SshConfigFileWatcher implements InitializingBean {

    Logger logger = LoggerFactory.getLogger(SshConfigFileWatcher.class);

    private WatchService watcher;
    private final Map<WatchKey, Path> keys = Maps.newHashMap();
    private boolean trace = false;
    @Autowired
    SshService sshService;


    SshConfigFileWatcher() {


    }

    <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Process all events for keys queued to the watcher
     */
    private void processEvents() {
        for (; ; ) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);


                logger.error("\n############################################################################\n");
                logger.error("Reload config file ... changed File:{} change Type:{}", child, event.kind().name());
                logger.error("\n############################################################################\n");

                try {
                    sshService.load(new FileInputStream(child.toFile()));
                } catch (Exception e) {

                    logger.error("Reload config file Failed, Ignore this update, error:", e);
                }

                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }
            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        String sshConfigFilePath = System.getProperty(Dict.SSH_CONFIG_FILE);
        String listeningPathString = "";
        if (sshConfigFilePath != null) {
            logger.info("use system path {}", sshConfigFilePath);
            listeningPathString = sshConfigFilePath;

            try {
                File listeningPath = new File(listeningPathString);
                this.watcher = FileSystems.getDefault().newWatchService();
                logger.info("Scanning {} ...", listeningPath);
                //  log.error("\n \n Scanning : {} ...\n \n", listeningPath);

                registerAll(Paths.get(listeningPath.getPath()));
                // enable trace after initial registration
                this.trace = true;

                Executors.newFixedThreadPool(1).submit(new Runnable() {
                    @Override
                    public void run() {
                        processEvents();
                    }
                });
            } catch (IOException e) {
                logger.error(" prepare watch file fail ! check Config Listening Path:{}", listeningPathString);
            }
        }

    }


}