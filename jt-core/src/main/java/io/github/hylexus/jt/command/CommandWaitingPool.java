package io.github.hylexus.jt.command;

import lombok.extern.slf4j.Slf4j;
import org.thavam.util.concurrent.blockingMap.BlockingHashMap;
import org.thavam.util.concurrent.blockingMap.BlockingMap;

import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 * Created At 2019-10-09 9:45 下午
 */
@Slf4j
public class CommandWaitingPool {
    private static final CommandWaitingPool instance = new CommandWaitingPool();

    public static CommandWaitingPool getInstance() {
        return instance;
    }

    private CommandWaitingPool() {
    }

    private BlockingMap<String, Object> blockingMap = new BlockingHashMap<>();
    //    private ReadWriteLock lock = new ReentrantReadWriteLock();
    //    private Lock readLock = this.lock.readLock();
    //    private Lock writeLock = this.lock.writeLock();

    public void puIfNecessary(CommandKey commandKey, Object msg) {
        String key = commandKey.getKeyAsString();
        if (!blockingMap.containsKey(key)) {
            log.debug("No waiting key found for key {}", key);
            return;
        }

        log.info("put for key [{}]", key);
        this.blockingMap.put(key, msg);
    }

    public Object waitingForKey(CommandKey commandKey, long time, TimeUnit unit) throws InterruptedException {
        String key = commandKey.getKeyAsString();
        return this.blockingMap.take(key, time, unit);
    }
}
