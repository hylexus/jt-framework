package io.github.hylexus.jt.jt808.support.extension.attachment.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.extension.attachment.AttachmentJt808TerminalHeatBeatHandler;
import io.github.hylexus.jt.jt808.support.extension.attachment.DelimiterAndLengthFieldBasedByteToMessageDecoder;
import io.github.hylexus.jt.jt808.support.extension.attachment.Jt808AttachmentServerNettyConfigure;
import io.github.hylexus.jt.jt808.support.netty.InternalIdleStateHandlerProps;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.Data;

import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.*;

@BuiltinComponent
public class DefaultJt808AttachmentServerNettyConfigure implements Jt808AttachmentServerNettyConfigure {

    private final DefaultJt808AttachmentServerNettyConfigure.BuiltInServerBootstrapProps serverBootstrapProps;
    private final ChannelInboundHandlerAdapter jt808DispatchChannelHandlerAdapter;
    private final EventExecutorGroup eventExecutorGroup;
    private final AttachmentJt808TerminalHeatBeatHandler heatBeatHandler;

    public DefaultJt808AttachmentServerNettyConfigure(
            DefaultJt808AttachmentServerNettyConfigure.BuiltInServerBootstrapProps serverBootstrapProps,
            ChannelInboundHandlerAdapter channelHandlerAdapter,
            EventExecutorGroup eventExecutorGroup, AttachmentJt808TerminalHeatBeatHandler heatBeatHandler) {

        this.serverBootstrapProps = serverBootstrapProps;
        this.jt808DispatchChannelHandlerAdapter = channelHandlerAdapter;
        this.eventExecutorGroup = eventExecutorGroup;
        this.heatBeatHandler = heatBeatHandler;
    }

    @Override
    public void configureSocketChannel(SocketChannel ch) {
        if (serverBootstrapProps.getIdleStateHandlerProps().isEnabled()) {
            final long readerIdleTime = serverBootstrapProps.getIdleStateHandlerProps().getReaderIdleTime().toMillis();
            final long writerIdleTime = serverBootstrapProps.getIdleStateHandlerProps().getWriterIdleTime().toMillis();
            final long allIdleTime = serverBootstrapProps.getIdleStateHandlerProps().getAllIdleTime().toMillis();
            ch.pipeline().addLast(
                    NETTY_HANDLER_NAME_ATTACHMENT_808_IDLE_STATE,
                    new IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime, TimeUnit.MILLISECONDS)
            );
        }
        ch.pipeline().addLast(BEAN_NAME_NETTY_HANDLER_NAME_ATTACHMENT_808_HEART_BEAT, heatBeatHandler);
        ch.pipeline().addLast(
                NETTY_HANDLER_NAME_ATTACHMENT_808_FRAME,
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
        final InternalIdleStateHandlerProps idleStateHandlerProps;

        public BuiltInServerBootstrapProps(int delimiterBasedFrameMaxFrameLength, int lengthFieldBasedFrameMaxFrameLength, InternalIdleStateHandlerProps idleStateHandlerProps) {
            this.delimiterBasedFrameMaxFrameLength = delimiterBasedFrameMaxFrameLength;
            this.lengthFieldBasedFrameMaxFrameLength = lengthFieldBasedFrameMaxFrameLength;
            this.idleStateHandlerProps = idleStateHandlerProps;
        }
    }
}