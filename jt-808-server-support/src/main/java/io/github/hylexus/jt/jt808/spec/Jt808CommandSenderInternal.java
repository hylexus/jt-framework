package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.exception.JtCommunicationException;
import io.github.hylexus.jt.jt808.spec.utils.DynamicField;
import io.github.hylexus.jt.jt808.spec.utils.DynamicFieldBasedJt808MsgEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.time.Duration;
import java.util.List;
import java.util.Map;
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

    void sendCommand(DynamicFieldBasedJt808MsgEncoder.Metadata metadata, List<DynamicField> body) throws JtCommunicationException;

    /**
     * @see DynamicField#fromMap(Map)
     * @see #sendCommandWithDynamicFieldsAndWaitingForReply(Jt808CommandKey, DynamicFieldBasedJt808MsgEncoder.Metadata, List, Duration)
     */
    void sendCommandWithDynamicFields(DynamicFieldBasedJt808MsgEncoder.Metadata metadata, List<Map<String, Object>> body) throws JtCommunicationException;

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

    <T> T sendCommandAndWaitingForReply(Jt808CommandKey commandKey, DynamicFieldBasedJt808MsgEncoder.Metadata metadata, List<DynamicField> body, Duration timeout)
            throws JtCommunicationException, InterruptedException;

    /**
     * @see DynamicField#fromMap(Map)
     * @see #sendCommandWithDynamicFields(DynamicFieldBasedJt808MsgEncoder.Metadata, List)
     */
    <T> T sendCommandWithDynamicFieldsAndWaitingForReply(Jt808CommandKey commandKey, DynamicFieldBasedJt808MsgEncoder.Metadata metadata, List<Map<String, Object>> body, Duration timeout)
            throws JtCommunicationException, InterruptedException;

}
