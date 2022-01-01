package io.github.hylexus.jt.jt808.spec;

import lombok.extern.slf4j.Slf4j;
import org.thavam.util.concurrent.blockingMap.BlockingHashMap;
import org.thavam.util.concurrent.blockingMap.BlockingMap;

import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 */
@Slf4j(topic = "jt-808.command-sender")
public class CommandWaitingPool {
    private static final CommandWaitingPool instance = new CommandWaitingPool();
    private final BlockingMap<String, Object> blockingMap = new BlockingHashMap<>();

    private CommandWaitingPool() {
    }

    public static CommandWaitingPool getInstance() {
        return instance;
    }

    public void putIfNecessary(Jt808CommandKey commandKey, Object msg) {
        final String waitingFlag = commandKey.getWaitingFlag();
        final String key = commandKey.getKeyAsString();
        if (!blockingMap.containsKey(waitingFlag)) {
            log.warn("[<<<COMMAND>>> ---> ] No waitingFlag found for key [{}]", key);
            return;
        }

        // Put result into blockingMap if there is a waiting-flag for this key
        this.blockingMap.put(key, msg);
        log.debug("[<<<COMMAND>>> ---> ] Put value for key [{}], {}", key, commandKey);
    }

    public Object waitingForKey(Jt808CommandKey commandKey, long time, TimeUnit unit) throws InterruptedException {

        final String waitingFlag = commandKey.getWaitingFlag();
        // Put a waiting-flag for put operation
        this.blockingMap.put(waitingFlag, System.currentTimeMillis());

        // Get result blocked
        Object result;
        try {
            final String key = commandKey.getKeyAsString();
            log.debug("[<<<COMMAND>>> <--- ] Waiting for key [{}] {} {}", key, time, unit);
            result = this.blockingMap.take(key, time, unit);
        } finally {
            // Remove tmp waiting-flag
            this.blockingMap.remove(waitingFlag);
        }

        return result;
    }
}
