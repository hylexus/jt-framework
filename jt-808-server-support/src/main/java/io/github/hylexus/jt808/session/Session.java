package io.github.hylexus.jt808.session;

import io.github.hylexus.jt.exception.JtCommunicationException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Session {
    private String id;
    private Channel channel = null;
    private String terminalId;
    private int currentFlowId = 0;
    private long lastCommunicateTimeStamp = 0L;

    public void sendMsgToClient(byte[] bytes) throws InterruptedException, JtCommunicationException {
        this.sendMsgToClient(Unpooled.wrappedBuffer(bytes));
    }

    public void sendMsgToClient(ByteBuf byteBuf) throws InterruptedException, JtCommunicationException {
        ChannelFuture sync = channel.writeAndFlush(byteBuf).sync();
        if (!sync.isSuccess()) {
            throw new JtCommunicationException("sendMsgToClient failed");
        }
    }

    public static String generateSessionId(Channel channel) {
        return channel.id().asLongText();
    }

    public static Session buildSession(Channel channel, String terminalId) {
        Session session = new Session();
        session.setChannel(channel);
        session.setId(generateSessionId(channel));
        session.setTerminalId(terminalId);
        session.setLastCommunicateTimeStamp(System.currentTimeMillis());
        return session;
    }

    public int getCurrentFlowId() {
        return currentFlowId();
    }

    private synchronized int currentFlowId() {
        if (currentFlowId >= 0xffff) {
            currentFlowId = 0;
        }
        return currentFlowId++;
    }
}
