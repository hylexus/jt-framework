package io.github.hylexus.jt.jt808.spec.session;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.exception.JtCommunicationException;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

/**
 * Created At 2020-06-20 15:58
 *
 * @author hylexus
 */
@BuiltinComponent
public interface Jt808Session {

    int MAX_FLOW_ID = 0xffff;

    /**
     * @param bytes 待发送给客户端的数据
     * @see #sendMsgToClient(ByteBuf)
     */
    default void sendMsgToClient(byte[] bytes) throws JtCommunicationException {
        this.sendMsgToClient(Unpooled.wrappedBuffer(bytes));
    }

    /**
     * @param byteBuf 待发送给客户端的数据
     */
    void sendMsgToClient(ByteBuf byteBuf) throws JtCommunicationException;

    /**
     * @return 当前流水号(并自增)
     * @see #flowId(boolean)
     */
    default int nextFlowId() {
        return flowId(true);
    }

    /**
     * @return 当前流水号(不会自增)
     */
    default int currentFlowId() {
        return flowId(false);
    }

    /**
     * @param autoIncrement 是否自增
     * @return 当前流水号
     */
    int flowId(boolean autoIncrement);

    String id();

    default String sessionId() {
        return id();
    }

    Channel channel();

    Jt808Session channel(Channel channel);

    String terminalId();

    /**
     * @return 当前终端的协议版本号
     */
    Jt808ProtocolVersion protocolVersion();

    /**
     * @return 上次通信时间
     */
    long lastCommunicateTimestamp();

    Jt808Session lastCommunicateTimestamp(long lastCommunicateTimestamp);

    String toString();
}
