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

    public void putIfNecessary(CommandKey commandKey, Object msg) {
        String waitingFlag = commandKey.getWaitingFlag();
        String key = commandKey.getKeyAsString();
        if (!blockingMap.containsKey(waitingFlag)) {
            log.debug("No waiting waitingFlag found for key {}", key);
            return;
        }

        // Put result into blockingMap if there is a waiting flag for this key
        this.blockingMap.put(key, msg);
        log.info("put value for waitingFlag [{}]", waitingFlag);
    }

    public Object waitingForKey(CommandKey commandKey, long time, TimeUnit unit) throws InterruptedException {
        String key = commandKey.getKeyAsString();
        String waitingFlag = commandKey.getWaitingFlag();

        // Put a waiting flag for put operation
        this.blockingMap.put(waitingFlag, null);

        // Get result blocked
        Object result;
        try {
            result = this.blockingMap.take(key, time, unit);
        } finally {
            // Remove tmp waiting flag
            this.blockingMap.remove(waitingFlag);
        }

        return result;
    }
}
