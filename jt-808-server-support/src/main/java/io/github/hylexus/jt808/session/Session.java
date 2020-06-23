package io.github.hylexus.jt808.session;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.exception.JtCommunicationException;
import io.netty.buffer.ByteBuf;
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

@ToString
@Accessors(chain = true)
@BuiltinComponent
public class Session implements Jt808Session {
    private final AtomicInteger nextFlowId = new AtomicInteger(0);
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private Channel channel;
    @Getter
    @Setter
    private String terminalId;
    @Getter
    @Setter
    private long lastCommunicateTimeStamp = 0L;

    @Override
    public void sendMsgToClient(ByteBuf byteBuf) throws InterruptedException, JtCommunicationException {
        ChannelFuture sync = channel.writeAndFlush(byteBuf).sync();
        if (!sync.isSuccess()) {
            throw new JtCommunicationException("sendMsgToClient failed");
        }
    }

    @Override
    public int getCurrentFlowId(boolean autoIncrement) {

        if (!autoIncrement) {
            int flowId = this.nextFlowId.get();
            if (flowId >= MAX_FLOW_ID) {
                if (nextFlowId.compareAndSet(flowId, 0)) {
                    return 0;
                }
                return nextFlowId.get();
            }
            return flowId;
        }

        final int flowId = this.nextFlowId.getAndIncrement();
        if (flowId >= MAX_FLOW_ID) {
            if (nextFlowId.compareAndSet(flowId, 0)) {
                return 0;
            }
            return nextFlowId.getAndIncrement();
        }

        return flowId;
    }

}
