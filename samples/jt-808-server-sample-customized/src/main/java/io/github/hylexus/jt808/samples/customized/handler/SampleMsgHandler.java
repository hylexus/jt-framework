package io.github.hylexus.jt808.samples.customized.handler;

import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.handler.MsgHandler;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.req.BuiltinAuthRequestMsgBody;
import io.github.hylexus.jt808.session.Session;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;

/**
 * 示例性的没啥用的 MsgHandler
 *
 * @author hylexus
 * Created At 2020-02-02 7:30 下午
 */
@Slf4j
public class SampleMsgHandler implements MsgHandler<BuiltinAuthRequestMsgBody> {

    static byte SUCCESS = 0;
    static byte AUTH_CODE_ERROR = 2;

    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Collections.singleton(BuiltinJt808MsgType.CLIENT_AUTH);
    }

    @Override
    public void handleMsg(RequestMsgMetadata metadata, BuiltinAuthRequestMsgBody body, Session session) throws InterruptedException {
        final String authCode = body.getAuthCode();

        // 鉴权逻辑
        final byte result = isValidAuthCode(authCode, session.getTerminalId())
                ? SUCCESS
                : AUTH_CODE_ERROR;

        // 组装响应消息的字节数组(别忘了转义)
        final byte[] respMsgBody = this.encodeMsgBody(result, metadata, session);

        // 发送给客户端
        this.send2Client(session.getChannel(), respMsgBody);
    }

    private byte[] encodeMsgBody(byte result, RequestMsgMetadata metadata, Session session) {
        // ...
        // 按文档格式组装字节数组
        return new byte[0];
    }

    private boolean isValidAuthCode(String authCode, String terminalId) {
        // ...
        // 具体鉴权逻辑
        return true;
    }

    protected void send2Client(Channel channel, byte[] bytes) throws InterruptedException {
        final ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(bytes)).sync();
        if (!future.isSuccess()) {
            log.error("ERROR : 'send data to client:'", future.cause());
        }
    }
}
