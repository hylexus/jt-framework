package io.github.hylexus.jt.command;

import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import org.junit.Test;

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

        Object msg = waitingPool.waitingForKey(commandKey, 4, TimeUnit.SECONDS);
        System.out.println(msg);
    }
}