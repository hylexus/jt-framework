package io.github.hylexus.jt.jt1078.boot.configuration;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt1078.boot.props.Jt1078ServerProps;
import io.github.hylexus.jt.jt1078.boot.props.server.Jt1078NettyTcpServerProps;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078DispatcherChannelHandler;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078ServerNettyConfigure;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078TerminalHeatBeatHandler;
import io.github.hylexus.jt.netty.JtServerNettyConfigureUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt.jt1078.Jt1078ProtocolConstant.*;

@BuiltinComponent
public class BuiltinJt1078ServerNettyConfigure implements Jt1078ServerNettyConfigure {
    private static final Logger log = LoggerFactory.getLogger(BuiltinJt1078ServerNettyConfigure.class);
    protected final Jt1078ServerProps serverProps;
    protected final Jt1078TerminalHeatBeatHandler heatBeatHandler;
    protected final EventExecutorGroup eventExecutorGroup;
    protected final Jt1078DispatcherChannelHandler jt1078DispatcherChannelHandler;

    public BuiltinJt1078ServerNettyConfigure(Jt1078ServerProps serverProps, Jt1078TerminalHeatBeatHandler heatBeatHandler, EventExecutorGroup eventExecutorGroup, Jt1078DispatcherChannelHandler jt1078DispatcherChannelHandler) {
        this.serverProps = serverProps;
        this.heatBeatHandler = heatBeatHandler;
        this.eventExecutorGroup = eventExecutorGroup;
        this.jt1078DispatcherChannelHandler = jt1078DispatcherChannelHandler;
    }

    @Override
    public ServerBootstrap configureServerBootstrap(ConfigurationProvider configProvider, ServerBootstrap serverBootstrap) {
        final Jt1078NettyTcpServerProps nettyProps = serverProps.getServer();
        final boolean useEpollIfAvailable = nettyProps.isPreferEpoll();
        final boolean epollAvailable = Epoll.isAvailable();
        final Class<? extends ServerChannel> channel = JtServerNettyConfigureUtils.configureChannel(serverBootstrap, nettyProps.isPreferEpoll(), nettyProps.getBossThreadCount(), nettyProps.getWorkerThreadCount());
        log.info("\n<<<JT/T-1078\nEpoll.isAvailable(): {}\njt1078.server.prefer-epoll: {}\nchannelClass: {}\nJT/T-1078>>>\n", epollAvailable, useEpollIfAvailable, channel.getName());

        return serverBootstrap
                .option(ChannelOption.SO_BACKLOG, this.serverProps.getProtocol().getMaxFrameLength())
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    @Override
    public void configureSocketChannel(ConfigurationProvider configProvider, SocketChannel ch) {
        final ChannelPipeline pipeline = ch.pipeline();

        final Jt1078NettyTcpServerProps.IdleStateHandlerProps idleStateHandlerProps = serverProps.getServer().getIdleStateHandler();
        if (idleStateHandlerProps.isEnabled()) {
            final long readerIdleTime = idleStateHandlerProps.getReaderIdleTime().toMillis();
            final long writerIdleTime = idleStateHandlerProps.getWriterIdleTime().toMillis();
            final long allIdleTime = idleStateHandlerProps.getAllIdleTime().toMillis();
            pipeline.addLast(
                    NETTY_HANDLER_NAME_1078_IDLE_STATE,
                    new IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime, TimeUnit.MILLISECONDS)
            );
        }
        pipeline.addLast(NETTY_HANDLER_NAME_1078_HEARTBEAT, heatBeatHandler);
        // TODO 自定义分包器
        pipeline.addLast(
                NETTY_HANDLER_NAME_1078_FRAME,
                new DelimiterBasedFrameDecoder(
                        serverProps.getProtocol().getMaxFrameLength(),
                        Unpooled.copiedBuffer(new byte[]{0x30, 0x31, 0x63, 0x64}),
                        Unpooled.copiedBuffer(new byte[]{0x30, 0x31, 0x63, 0x64, 0x30, 0x31, 0x63, 0x64})
                )
        );

        pipeline.addLast(eventExecutorGroup, NETTY_HANDLER_NAME_1078_MSG_DISPATCHER_ADAPTER, jt1078DispatcherChannelHandler);
    }
}
