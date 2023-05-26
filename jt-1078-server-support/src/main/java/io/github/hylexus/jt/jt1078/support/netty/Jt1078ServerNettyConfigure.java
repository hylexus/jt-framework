package io.github.hylexus.jt.jt1078.support.netty;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.netty.JtServerNettyConfigure;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * @author hylexus
 */
public interface Jt1078ServerNettyConfigure extends JtServerNettyConfigure {

    @BuiltinComponent
    class DefaultJt1078ServerNettyConfigure implements Jt1078ServerNettyConfigure {

        private final Jt1078DispatcherChannelHandler jt1078DispatcherChannelHandler;

        public DefaultJt1078ServerNettyConfigure(Jt1078DispatcherChannelHandler jt1078DispatcherChannelHandler) {
            this.jt1078DispatcherChannelHandler = jt1078DispatcherChannelHandler;
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
            final ChannelPipeline pipeline = ch.pipeline();

            pipeline.addLast(
                    "handler",
                    new DelimiterBasedFrameDecoder(
                            2048,
                            Unpooled.copiedBuffer(new byte[]{0x30, 0x31, 0x63, 0x64})
                    )
            );

            pipeline.addLast(jt1078DispatcherChannelHandler);
        }
    }
}
