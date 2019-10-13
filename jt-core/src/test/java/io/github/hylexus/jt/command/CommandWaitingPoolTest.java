package io.github.hylexus.jt.command;

import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import org.junit.Assert;
import org.junit.Test;
import org.thavam.util.concurrent.blockingMap.BlockingHashMap;
import org.thavam.util.concurrent.blockingMap.BlockingMap;

import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 * Created At 2019-10-09 10:09 下午
 */
public class CommandWaitingPoolTest {

    @Test
    public void waitingForKeyTest() throws InterruptedException {
        CommandWaitingPool waitingPool = CommandWaitingPool.getInstance();
        final Jt808CommandKey commandKey = Jt808CommandKey.of(BuiltinJt808MsgType.CLIENT_AUTH, "123");

        int timeout = 5;
        putKeyFromOtherThread(waitingPool, commandKey, timeout);

        long start = System.currentTimeMillis();
        Object msg = waitingPool.waitingForKey(commandKey, 4, TimeUnit.SECONDS);
        Assert.assertTrue("time >= 4 seconds", (System.currentTimeMillis() - start) >= 4000);
        Assert.assertNull(msg);

        start = System.currentTimeMillis();
        msg = waitingPool.waitingForKey(commandKey, 4, TimeUnit.SECONDS);
        Assert.assertTrue("time >= 1 second", (System.currentTimeMillis() - start) >= 1000);

        Assert.assertNotNull(msg);
    }

    private void putKeyFromOtherThread(CommandWaitingPool waitingPool, Jt808CommandKey commandKey, int timeout) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            waitingPool.putIfNecessary(commandKey, "value");
            System.out.println("put value");
        }).start();
    }

    @Test
    public void testBlockingMap() throws InterruptedException {
        BlockingMap<String, String> map = new BlockingHashMap<>();
        map.put("1", "1");
        System.out.println(map.size());

        System.out.println("value: " + map.take("1"));
        System.out.println(map.size());
    }
}