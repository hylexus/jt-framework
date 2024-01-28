package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.exception.JtCommunicationException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.concurrent.TimeUnit;

/**
 * 主动下发数据给终端。
 * <p>
 * Created At 2020-03-11 22:07
 *
 * @author hylexus
 */
public interface Jt808CommandSenderInternal {

    void sendCommand(String terminalId, ByteBuf byteBuf) throws JtCommunicationException;

    default void sendCommand(String terminalId, byte[] data) throws JtCommunicationException {
        this.sendCommand(terminalId, ByteBufAllocator.DEFAULT.buffer(data.length).writeBytes(data));
    }

    void sendCommand(Jt808Response response) throws JtCommunicationException;

    void sendCommand(String terminalId, Object entity) throws JtCommunicationException;

    <T> T sendCommandAndWaitingForReply(Jt808CommandKey key, ByteBuf byteBuf, long timeout, TimeUnit timeUnit)
            throws JtCommunicationException, InterruptedException;

    default <T> T sendCommandAndWaitingForReply(Jt808CommandKey key, byte[] data, long timeout, TimeUnit timeUnit)
            throws JtCommunicationException, InterruptedException {

        return this.sendCommandAndWaitingForReply(key, ByteBufAllocator.DEFAULT.buffer(data.length).writeBytes(data), timeout, timeUnit);
    }

    <T> T sendCommandAndWaitingForReply(Jt808CommandKey key, Jt808Response response, Long timeout, TimeUnit timeUnit)
            throws JtCommunicationException, InterruptedException;

    <T> T sendCommandAndWaitingForReply(Jt808CommandKey key, Object entity, Long timeout, TimeUnit timeUnit)
            throws JtCommunicationException, InterruptedException;

}
