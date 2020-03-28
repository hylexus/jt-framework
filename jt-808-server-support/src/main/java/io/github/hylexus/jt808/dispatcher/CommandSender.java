package io.github.hylexus.jt808.dispatcher;

import io.github.hylexus.jt.command.Jt808CommandKey;
import io.github.hylexus.jt808.msg.resp.CommandMsg;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 * Created At 2020-03-11 22:07
 */
public interface CommandSender {

    Object sendCommandAndWaitingForReply(CommandMsg commandMsg, boolean withFlowId, Long timeout, TimeUnit unit) throws IOException, InterruptedException;

    default Object sendCommandAndWaitingForReply(CommandMsg commandMsg, Long timeout, TimeUnit unit) throws IOException, InterruptedException {
        return this.sendCommandAndWaitingForReply(commandMsg, true, timeout, unit);
    }

    Object sendCommandAndWaitingForReply(Jt808CommandKey key, byte[] data, long time, TimeUnit unit) throws InterruptedException;

    void sendCommand(CommandMsg commandMsg, long time, TimeUnit unit) throws InterruptedException, IOException;

    void sendCommand(String terminalId, byte[] data, long time, TimeUnit unit) throws InterruptedException;
}
