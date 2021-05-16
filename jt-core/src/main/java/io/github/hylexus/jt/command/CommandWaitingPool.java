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
    private final BlockingMap<Jt808CommandKey, Object> blockingMap = new BlockingHashMap<>();

    private CommandWaitingPool() {
    }

    public static CommandWaitingPool getInstance() {
        return instance;
    }

    public void putIfNecessary(Jt808CommandKey commandKey, Object msg) {
        if (!blockingMap.containsKey(commandKey)) {
            log.warn("No waitingFlag found for key {}", commandKey);
            return;
        }

        // Put result into blockingMap if there is a waiting-flag for this key
        this.blockingMap.put(commandKey, msg);
        log.info("Put value for waitingFlag [{}]", commandKey);
    }

    public Object waitingForKey(Jt808CommandKey commandKey, long time, TimeUnit unit) throws InterruptedException {

        // Put a waiting-flag for put operation
        this.blockingMap.put(commandKey, null);

        // Get result blocked
        Object result;
        try {
            log.info("Waiting for key {}", commandKey);
            result = this.blockingMap.take(commandKey, time, unit);
        } finally {
            // Remove tmp waiting-flag
            this.blockingMap.remove(commandKey);
        }

        return result;
    }
}
