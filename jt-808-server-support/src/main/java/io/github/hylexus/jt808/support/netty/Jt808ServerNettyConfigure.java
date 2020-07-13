package io.github.hylexus.jt808.support.netty;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt.config.JtProtocolConstant.*;

/**
 * Created At 2020-07-04 19:02
 *
 * @author hylexus
 */
public interface Jt808ServerNettyConfigure {

    void configureServerBootstrap(ServerBootstrap serverBootstrap);

    void configureSocketChannel(SocketChannel ch);

    @BuiltinComponent
    class DefaultJt808ServerNettyConfigure implements Jt808ServerNettyConfigure {
        private final HeatBeatHandler heatBeatHandler;
        private final Jt808DecodeHandler decodeHandler;
        private final TerminalValidatorHandler terminalValidatorHandler;
        private final Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter;

        public DefaultJt808ServerNettyConfigure(HeatBeatHandler heatBeatHandler, Jt808DecodeHandler decodeHandler,
                                                TerminalValidatorHandler terminalValidatorHandler,
                                                Jt808ChannelHandlerAdapter jt808ChannelHandlerAdapter) {
            this.heatBeatHandler = heatBeatHandler;
            this.decodeHandler = decodeHandler;
            this.terminalValidatorHandler = terminalValidatorHandler;
            this.jt808ChannelHandlerAdapter = jt808ChannelHandlerAdapter;
        }

        @Override
        public void configureServerBootstrap(ServerBootstrap serverBootstrap) {
            serverBootstrap
                    .option(ChannelOption.SO_BACKLOG, 2048)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
        }

        @Override
        public void configureSocketChannel(SocketChannel ch) {
            ch.pipeline().addLast(NETTY_HANDLER_NAME_808_IDLE_STATE, new IdleStateHandler(20, 20, 20, TimeUnit.MINUTES));
            ch.pipeline().addLast(NETTY_HANDLER_NAME_808_HEART_BEAT, heatBeatHandler);
            ch.pipeline().addLast(
                    NETTY_HANDLER_NAME_808_FRAME,
                    new DelimiterBasedFrameDecoder(
                            MAX_PACKAGE_LENGTH,
                            Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER}),
                            Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER, PACKAGE_DELIMITER})
                    )
            );
            ch.pipeline().addLast(NETTY_HANDLER_NAME_808_DECODE, decodeHandler);
            ch.pipeline().addLast(NETTY_HANDLER_NAME_808_TERMINAL_VALIDATOR, terminalValidatorHandler);
            ch.pipeline().addLast(NETTY_HANDLER_NAME_808_MSG_DISPATCHER_ADAPTER, jt808ChannelHandlerAdapter);
        }
    }
}
