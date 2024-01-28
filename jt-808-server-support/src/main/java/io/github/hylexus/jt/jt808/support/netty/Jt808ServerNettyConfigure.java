package io.github.hylexus.jt.jt808.support.netty;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.netty.JtServerNettyConfigure;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.Data;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.*;

/**
 * Created At 2020-07-04 19:02
 *
 * @author lirenhao
 * @author hylexus
 */
public interface Jt808ServerNettyConfigure extends JtServerNettyConfigure {

    @BuiltinComponent
    class DefaultJt808ServerNettyConfigure implements Jt808ServerNettyConfigure {
        @Data
        public static class BuiltInServerBootstrapProps {
            private final int maxFrameLength;
            private final IdleStateHandlerProps idleStateHandlerProps;

            public BuiltInServerBootstrapProps(int maxFrameLength, IdleStateHandlerProps idleStateHandlerProps) {
                this.maxFrameLength = maxFrameLength;
                this.idleStateHandlerProps = idleStateHandlerProps;
            }
        }

        @Data
        public static class IdleStateHandlerProps {
            private boolean enabled;
            private Duration readerIdleTime;
            private Duration writerIdleTime;
            private Duration allIdleTime;

            public IdleStateHandlerProps(boolean enabled, Duration readerIdleTime, Duration writerIdleTime, Duration allIdleTime) {
                this.enabled = enabled;
                this.readerIdleTime = readerIdleTime;
                this.writerIdleTime = writerIdleTime;
                this.allIdleTime = allIdleTime;
            }
        }

        private final BuiltInServerBootstrapProps serverBootstrapProps;
        private final Jt808TerminalHeatBeatHandler heatBeatHandler;
        private final Jt808DispatchChannelHandlerAdapter jt808DispatchChannelHandlerAdapter;
        private final EventExecutorGroup eventExecutorGroup;

        public DefaultJt808ServerNettyConfigure(
                BuiltInServerBootstrapProps serverBootstrapProps,
                Jt808TerminalHeatBeatHandler heatBeatHandler,
                Jt808DispatchChannelHandlerAdapter channelHandlerAdapter, EventExecutorGroup eventExecutorGroup) {

            this.serverBootstrapProps = serverBootstrapProps;
            this.heatBeatHandler = heatBeatHandler;
            this.jt808DispatchChannelHandlerAdapter = channelHandlerAdapter;
            this.eventExecutorGroup = eventExecutorGroup;
        }

        @Override
        public void configureSocketChannel(SocketChannel ch) {
            if (serverBootstrapProps.getIdleStateHandlerProps().isEnabled()) {
                final long readerIdleTime = serverBootstrapProps.getIdleStateHandlerProps().getReaderIdleTime().toMillis();
                final long writerIdleTime = serverBootstrapProps.getIdleStateHandlerProps().getWriterIdleTime().toMillis();
                final long allIdleTime = serverBootstrapProps.getIdleStateHandlerProps().getAllIdleTime().toMillis();
                ch.pipeline().addLast(
                        NETTY_HANDLER_NAME_808_IDLE_STATE,
                        new IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime, TimeUnit.MILLISECONDS)
                );
            }
            ch.pipeline().addLast(BEAN_NAME_NETTY_HANDLER_NAME_808_HEART_BEAT, heatBeatHandler);
            ch.pipeline().addLast(
                    NETTY_HANDLER_NAME_808_FRAME,
                    new DelimiterBasedFrameDecoder(
                            serverBootstrapProps.getMaxFrameLength(),
                            Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER}),
                            Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER, PACKAGE_DELIMITER})
                    )
            );
            ch.pipeline().addLast(this.eventExecutorGroup, NETTY_HANDLER_NAME_808_MSG_DISPATCHER_ADAPTER, jt808DispatchChannelHandlerAdapter);
        }
    }
}
