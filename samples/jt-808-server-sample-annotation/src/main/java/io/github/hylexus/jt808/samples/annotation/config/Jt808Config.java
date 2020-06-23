package io.github.hylexus.jt808.samples.annotation.config;

import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.session.*;
import io.github.hylexus.jt808.support.netty.Jt808ChannelHandlerAdapter;
import io.github.hylexus.jt808.support.netty.Jt808ServerConfigure;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;
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

    @Autowired
    private Jt808SessionManager sessionManager;

    // [[必须配置]] -- 自定义消息类型解析器
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

    // [非必须配置] -- 可替换内置SessionManager
    @Bean
    public Jt808SessionManager sessionManager() {
        return SessionManager.getInstance();
    }

    // [非必须配置] -- 可替换内置Jt808SessionManagerEventListener
    @Bean
    public Jt808SessionManagerEventListener jt808SessionManagerEventListener() {
        return new Jt808SessionManagerEventListener() {
            @Override
            public void onSessionClose(@Nullable Jt808Session session, ISessionCloseReason closeReason) {
                Jt808SessionManagerEventListener.super.onSessionClose(session, closeReason);
            }
        };
    }

    // [非必须配置] -- 可替换内置Netty相关配置
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
                    sessionManager.removeBySessionIdAndClose(sessionManager.generateSessionId(ctx.channel()), IDLE_TIMEOUT);
                }
            }
        });
    }

    // [非必须配置] -- 可替换内置转义逻辑
    @Bean(BEAN_NAME_JT808_BYTES_ENCODER)
    public BytesEncoder bytesEncoder() {
        return new BytesEncoder.DefaultBytesEncoder();
    }

}
