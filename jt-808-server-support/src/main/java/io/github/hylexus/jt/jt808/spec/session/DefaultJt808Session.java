package io.github.hylexus.jt.jt808.spec.session;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.exception.JtCommunicationException;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 **/
@ToString(exclude = "channel")
@Accessors(chain = true, fluent = true)
@BuiltinComponent
public class DefaultJt808Session implements Jt808Session {
    protected final Jt808FlowIdGenerator delegateFlowIdGenerator;

    public DefaultJt808Session(Jt808FlowIdGenerator delegateFlowIdGenerator) {
        this.delegateFlowIdGenerator = delegateFlowIdGenerator;
    }

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
    private long lastCommunicateTimestamp = 0L;

    @Getter
    @Setter
    private Jt808ProtocolVersion protocolVersion;

    @Override
    public void sendMsgToClient(ByteBuf byteBuf) throws JtCommunicationException {
        try {
            final ChannelFuture sync = channel.writeAndFlush(byteBuf).sync();
            if (!sync.isSuccess()) {
                throw new JtCommunicationException("sendMsgToClient failed");
            }
        } catch (InterruptedException e) {
            throw new JtCommunicationException(e);
        }
    }

    @Override
    public int flowId(int increment) {
        return this.delegateFlowIdGenerator.flowId(increment);
    }
}
