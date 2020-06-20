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

import java.util.concurrent.atomic.AtomicInteger;

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
    private Channel channel;
    private String terminalId;
    private AtomicInteger currentFlowId = new AtomicInteger(0);
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
        final int flowId = this.currentFlowId.getAndIncrement();
        if (flowId >= 0xffff) {
            if (currentFlowId.compareAndSet(flowId, 0)) {
                return 0;
            }
            return currentFlowId.getAndIncrement();
        }

        return flowId;
    }

}
