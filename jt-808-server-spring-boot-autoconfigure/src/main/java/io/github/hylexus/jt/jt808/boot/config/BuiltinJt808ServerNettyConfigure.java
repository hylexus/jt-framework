package io.github.hylexus.jt.jt808.boot.config;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.boot.props.server.Jt808NettyTcpServerProps;
import io.github.hylexus.jt.jt808.support.netty.Jt808DispatchChannelHandlerAdapter;
import io.github.hylexus.jt.jt808.support.netty.Jt808ServerNettyConfigure;
import io.github.hylexus.jt.jt808.support.netty.Jt808TerminalHeatBeatHandler;
import io.github.hylexus.jt.netty.JtServerNettyConfigureUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.*;

@BuiltinComponent
public class BuiltinJt808ServerNettyConfigure implements Jt808ServerNettyConfigure {

    private static final Logger log = LoggerFactory.getLogger(BuiltinJt808ServerNettyConfigure.class);
    protected final Jt808ServerProps serverProps;
    protected final Jt808TerminalHeatBeatHandler heatBeatHandler;
    protected final EventExecutorGroup eventExecutorGroup;
    protected final Jt808DispatchChannelHandlerAdapter jt808DispatchChannelHandlerAdapter;

    public BuiltinJt808ServerNettyConfigure(
            Jt808ServerProps serverProps,
            EventExecutorGroup eventExecutorGroup,
            Jt808DispatchChannelHandlerAdapter jt808DispatchChannelHandlerAdapter,
            Jt808TerminalHeatBeatHandler heatBeatHandler) {
        this.serverProps = serverProps;
        this.heatBeatHandler = heatBeatHandler;
        this.eventExecutorGroup = eventExecutorGroup;
        this.jt808DispatchChannelHandlerAdapter = jt808DispatchChannelHandlerAdapter;
    }

    @Override
    public ServerBootstrap configureServerBootstrap(ConfigurationProvider configProvider, ServerBootstrap serverBootstrap) {
        final Jt808NettyTcpServerProps nettyProps = serverProps.getServer();
        final Class<? extends ServerChannel> channel = JtServerNettyConfigureUtils.configureChannel(serverBootstrap, nettyProps.isPreferEpoll(), nettyProps.getBossThreadCount(), nettyProps.getWorkerThreadCount());
        log.info("\n<<<JT/T-808-INSTRUCTION-SERVER\nEpoll.isAvailable(): {}\njt808.server.prefer-epoll: {}\nchannelClass: {}\nJT/T-808-INSTRUCTION-SERVER>>>\n", Epoll.isAvailable(), nettyProps.isPreferEpoll(), channel.getName());

        return serverBootstrap
                .option(ChannelOption.SO_BACKLOG, 2048)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    @Override
    public void configureSocketChannel(ConfigurationProvider configProvider, SocketChannel ch) {
        final Jt808NettyTcpServerProps.IdleStateHandlerProps idleStateHandler = this.serverProps.getServer().getIdleStateHandler();
        if (idleStateHandler.isEnabled()) {
            final long readerIdleTime = idleStateHandler.getReaderIdleTime().toMillis();
            final long writerIdleTime = idleStateHandler.getWriterIdleTime().toMillis();
            final long allIdleTime = idleStateHandler.getAllIdleTime().toMillis();
            ch.pipeline().addLast(
                    NETTY_HANDLER_NAME_808_IDLE_STATE,
                    new IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime, TimeUnit.MILLISECONDS)
            );
        }
        ch.pipeline().addLast(BEAN_NAME_NETTY_HANDLER_NAME_808_HEART_BEAT, heatBeatHandler);
        ch.pipeline().addLast(
                NETTY_HANDLER_NAME_808_FRAME,
                new DelimiterBasedFrameDecoder(
                        this.serverProps.getProtocol().getMaxFrameLength(),
                        Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER}),
                        Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER, PACKAGE_DELIMITER})
                )
        );
        ch.pipeline().addLast(this.eventExecutorGroup, NETTY_HANDLER_NAME_808_MSG_DISPATCHER_ADAPTER, jt808DispatchChannelHandlerAdapter);
    }

}
