package io.github.hylexus.jt.jt808.support.extension.attachment;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.netty.JtServerNettyConfigure;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.Data;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.NETTY_HANDLER_NAME_808_FRAME;
import static io.github.hylexus.jt.jt808.JtProtocolConstant.NETTY_HANDLER_NAME_808_MSG_DISPATCHER_ADAPTER;

public interface Jt808AttachmentServerNettyConfigure extends JtServerNettyConfigure {

    @BuiltinComponent
    class DefaultJt808AttachmentServerNettyConfigure implements Jt808AttachmentServerNettyConfigure {
        private final DefaultJt808AttachmentServerNettyConfigure.BuiltInServerBootstrapProps serverBootstrapProps;
        private final ChannelInboundHandlerAdapter jt808DispatchChannelHandlerAdapter;
        private final EventExecutorGroup eventExecutorGroup;

        public DefaultJt808AttachmentServerNettyConfigure(
                DefaultJt808AttachmentServerNettyConfigure.BuiltInServerBootstrapProps serverBootstrapProps,
                ChannelInboundHandlerAdapter channelHandlerAdapter,
                EventExecutorGroup eventExecutorGroup) {

            this.serverBootstrapProps = serverBootstrapProps;
            this.jt808DispatchChannelHandlerAdapter = channelHandlerAdapter;
            this.eventExecutorGroup = eventExecutorGroup;
        }

        @Override
        public void configureSocketChannel(SocketChannel ch) {
            ch.pipeline().addLast(
                    NETTY_HANDLER_NAME_808_FRAME,
                    new DelimiterAndLengthFieldBasedByteToMessageDecoder(
                            serverBootstrapProps.getDelimiterBasedFrameMaxFrameLength(),
                            serverBootstrapProps.getLengthFieldBasedFrameMaxFrameLength()
                    )
            );
            ch.pipeline().addLast(this.eventExecutorGroup, NETTY_HANDLER_NAME_808_MSG_DISPATCHER_ADAPTER, jt808DispatchChannelHandlerAdapter);
        }

        @Data
        public static class BuiltInServerBootstrapProps {
            final int delimiterBasedFrameMaxFrameLength;
            final int lengthFieldBasedFrameMaxFrameLength;

            public BuiltInServerBootstrapProps(int delimiterBasedFrameMaxFrameLength, int lengthFieldBasedFrameMaxFrameLength) {
                this.delimiterBasedFrameMaxFrameLength = delimiterBasedFrameMaxFrameLength;
                this.lengthFieldBasedFrameMaxFrameLength = lengthFieldBasedFrameMaxFrameLength;
            }
        }
    }
}
