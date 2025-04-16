package io.github.hylexus.jt.jt808.boot.config;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.boot.props.attachment.AttachmentServerProps;
import io.github.hylexus.jt.jt808.boot.props.attachment.IdleStateHandlerProps;
import io.github.hylexus.jt.jt808.support.extension.attachment.AttachmentJt808TerminalHeatBeatHandler;
import io.github.hylexus.jt.jt808.support.extension.attachment.DelimiterAndLengthFieldBasedByteToMessageDecoder;
import io.github.hylexus.jt.jt808.support.extension.attachment.Jt808AttachmentServerNettyConfigure;
import io.github.hylexus.jt.netty.JtServerNettyConfigureUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.*;

@BuiltinComponent
public class BuiltinJt808AttachmentServerNettyConfigure implements Jt808AttachmentServerNettyConfigure {
    private static final Logger log = LoggerFactory.getLogger(BuiltinJt808AttachmentServerNettyConfigure.class);
    protected final Jt808ServerProps serverProps;
    protected final AttachmentJt808TerminalHeatBeatHandler heatBeatHandler;
    protected final EventExecutorGroup eventExecutorGroup;
    protected final ChannelInboundHandlerAdapter jt808DispatchChannelHandlerAdapter;

    public BuiltinJt808AttachmentServerNettyConfigure(Jt808ServerProps serverProps, AttachmentJt808TerminalHeatBeatHandler heatBeatHandler, EventExecutorGroup eventExecutorGroup, ChannelInboundHandlerAdapter jt808DispatchChannelHandlerAdapter) {
        this.serverProps = serverProps;
        this.heatBeatHandler = heatBeatHandler;
        this.eventExecutorGroup = eventExecutorGroup;
        this.jt808DispatchChannelHandlerAdapter = jt808DispatchChannelHandlerAdapter;
    }

    @Override
    public ServerBootstrap configureServerBootstrap(ConfigurationProvider configProvider, ServerBootstrap serverBootstrap) {
        final AttachmentServerProps nettyProps = serverProps.getAttachmentServer();
        final Class<? extends ServerChannel> channel = JtServerNettyConfigureUtils.configureChannel(serverBootstrap, nettyProps.isPreferEpoll(), nettyProps.getBossThreadCount(), nettyProps.getWorkerThreadCount());
        log.info("\n<<<JT/T-808-ATTACHMENT-SERVER\nEpoll.isAvailable(): {}\njt808.attachment-server.prefer-epoll: {}\nchannelClass: {}\nJT/T-808-ATTACHMENT-SERVER>>>\n", Epoll.isAvailable(), nettyProps.isPreferEpoll(), channel.getName());

        return serverBootstrap
                .option(ChannelOption.SO_BACKLOG, 2048)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    @Override
    public void configureSocketChannel(ConfigurationProvider configProvider, SocketChannel ch) {
        final AttachmentServerProps attachmentServer = this.serverProps.getAttachmentServer();
        if (attachmentServer.isEnabled()) {
            final IdleStateHandlerProps idleStateHandler = attachmentServer.getIdleStateHandler();
            final long readerIdleTime = idleStateHandler.getReaderIdleTime().toMillis();
            final long writerIdleTime = idleStateHandler.getWriterIdleTime().toMillis();
            final long allIdleTime = idleStateHandler.getAllIdleTime().toMillis();
            ch.pipeline().addLast(
                    NETTY_HANDLER_NAME_ATTACHMENT_808_IDLE_STATE,
                    new IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime, TimeUnit.MILLISECONDS)
            );
        }
        ch.pipeline().addLast(BEAN_NAME_NETTY_HANDLER_NAME_ATTACHMENT_808_HEART_BEAT, heatBeatHandler);
        ch.pipeline().addLast(
                NETTY_HANDLER_NAME_ATTACHMENT_808_FRAME,
                new DelimiterAndLengthFieldBasedByteToMessageDecoder(
                        serverProps.getProtocol().getMaxFrameLength(),
                        attachmentServer.getMaxFrameLength()
                )
        );

        ch.pipeline().addLast(this.eventExecutorGroup, NETTY_HANDLER_NAME_808_MSG_DISPATCHER_ADAPTER, jt808DispatchChannelHandlerAdapter);
    }
}
