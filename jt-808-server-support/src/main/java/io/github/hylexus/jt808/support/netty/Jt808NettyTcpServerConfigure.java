package io.github.hylexus.jt808.support.netty;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt808.support.MsgConverterMapping;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt.config.JtProtocolConstant.*;

/**
 * @author hylexus
 * Created At 2019-08-27 16:53
 */
public class Jt808NettyTcpServerConfigure {

    public void configureMsgConverterMapping(MsgConverterMapping mapping) {
    }

    public void configureMsgHandlerMapping(MsgHandlerMapping mapping) {
    }

    public void configureServerBootstrap(ServerBootstrap serverBootstrap) {
        serverBootstrap
                .option(ChannelOption.SO_BACKLOG, 2048)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    public void configureSocketChannel(SocketChannel ch, Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter) {
        ch.pipeline().addLast(NETTY_HANDLER_NAME_808_IDLE_STATE, new IdleStateHandler(3, 0, 0, TimeUnit.MINUTES));
        ch.pipeline().addLast(NETTY_HANDLER_NAME_808_HEART_BEAT, new HeatBeatHandler());
        ch.pipeline().addLast(
                NETTY_HANDLER_NAME_808_FRAME,
                new DelimiterBasedFrameDecoder(
                        MAX_PACKAGE_LENGTH,
                        Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER}),
                        Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER, PACKAGE_DELIMITER})
                )
        );
        ch.pipeline().addLast(NETTY_HANDLER_NAME_808_MSG_DISPATCHER_ADAPTER, jt808ChannelHandlerAdapter);
    }

    @DebugOnly
    public static class BuiltinNoOpsConfigureNettyTcp extends Jt808NettyTcpServerConfigure {
    }
}
