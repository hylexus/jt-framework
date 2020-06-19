package io.github.hylexus.jt808.samples.annotation.config;

import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.session.Session;
import io.github.hylexus.jt808.session.SessionManager;
import io.github.hylexus.jt808.support.netty.Jt808ChannelHandlerAdapter;
import io.github.hylexus.jt808.support.netty.Jt808ServerConfigure;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

import static io.github.hylexus.jt.config.JtProtocolConstant.BEAN_NAME_JT808_BYTES_ENCODER;
import static io.github.hylexus.jt.config.JtProtocolConstant.NETTY_HANDLER_NAME_808_HEART_BEAT;
import static io.github.hylexus.jt808.session.SessionCloseReason.IDLE_TIMEOUT;

/**
 * @author hylexus
 * Created At 2020-02-02 1:22 下午
 */
@Configuration
public class Jt808Config extends Jt808ServerConfigure {

    @Override
    public MsgTypeParser supplyMsgTypeParser() {
        return msgType -> {
            Optional<MsgType> type = Jt808MsgType.CLIENT_AUTH.parseFromInt(msgType);
            return type.isPresent()
                    ? type
                    // 自定义解析器无法解析,使用内置解析器
                    : BuiltinJt808MsgType.CLIENT_AUTH.parseFromInt(msgType);
        };
    }

    @Override
    public void configureSocketChannel(SocketChannel ch, Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter) {
        super.configureSocketChannel(ch, jt808ChannelHandlerAdapter);

        ch.pipeline().replace(NETTY_HANDLER_NAME_808_HEART_BEAT, NETTY_HANDLER_NAME_808_HEART_BEAT, new ChannelInboundHandlerAdapter() {
            @Override
            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                if (!(evt instanceof IdleStateEvent)) {
                    super.userEventTriggered(ctx, evt);
                    return;
                }

                if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
                    System.out.println("disconnected idle connection");
                    SessionManager.getInstance().removeBySessionIdAndClose(Session.generateSessionId(ctx.channel()), IDLE_TIMEOUT);
                    ctx.channel().close();
                }
            }
        });
    }

    @Bean(BEAN_NAME_JT808_BYTES_ENCODER)
    public BytesEncoder bytesEncoder() {
        return new BytesEncoder.DefaultBytesEncoder();
    }

}
