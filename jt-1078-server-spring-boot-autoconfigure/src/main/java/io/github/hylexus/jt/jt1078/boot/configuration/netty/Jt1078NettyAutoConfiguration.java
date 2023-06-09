package io.github.hylexus.jt.jt1078.boot.configuration.netty;

import io.github.hylexus.jt.jt1078.boot.props.Jt1078ServerProps;
import io.github.hylexus.jt.jt1078.boot.props.msg.processor.MsgProcessorExecutorGroupProps;
import io.github.hylexus.jt.jt1078.boot.props.server.Jt1078NettyTcpServerProps;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.support.netty.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static io.github.hylexus.jt.jt1078.Jt1078ProtocolConstant.*;

@Slf4j
public class Jt1078NettyAutoConfiguration {

    private final Jt1078ServerProps serverProps;

    public Jt1078NettyAutoConfiguration(Jt1078ServerProps serverProps) {
        this.serverProps = serverProps;
    }

    @Bean
    @ConditionalOnMissingBean(Jt1078ServerNettyConfigure.class)
    public Jt1078ServerNettyConfigure.DefaultJt1078ServerNettyConfigure jt1078ServerNettyConfigure(
            @Qualifier(BEAN_NAME_1078_SERVER_BOOTSTRAP_CONFIGURE) Consumer<ServerBootstrap> serverBootstrapConfigure,
            @Qualifier(BEAN_NAME_1078_SOCKET_CHANNEL_CONFIGURE) Consumer<SocketChannel> socketChannelConfigure) {

        return new Jt1078ServerNettyConfigure.DefaultJt1078ServerNettyConfigure(serverBootstrapConfigure, socketChannelConfigure);
    }

    @Bean(name = BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
    @ConditionalOnMissingBean(name = BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
    public EventExecutorGroup eventExecutorGroup() {

        final MsgProcessorExecutorGroupProps poolProps = serverProps.getMsgProcessor().getExecutorGroup();
        final DefaultThreadFactory threadFactory = new DefaultThreadFactory(poolProps.getPoolName());

        log.info("Jt1078MsgProcessorConfig = {}", poolProps);
        return new DefaultEventExecutorGroup(
                poolProps.getThreadCount(),
                threadFactory,
                poolProps.getMaxPendingTasks(),
                RejectedExecutionHandlers.reject()
        );
    }

    @Bean(BEAN_NAME_NETTY_HANDLER_NAME_1078_HEART_BEAT)
    @ConditionalOnMissingBean(name = BEAN_NAME_NETTY_HANDLER_NAME_1078_HEART_BEAT)
    public Jt1078TerminalHeatBeatHandler heatBeatHandler(Jt1078SessionManager sessionManager) {
        return new Jt1078TerminalHeatBeatHandler(sessionManager);
    }

    @Bean(BEAN_NAME_1078_SOCKET_CHANNEL_CONFIGURE)
    @ConditionalOnBean(Jt1078ServerNettyConfigure.DefaultJt1078ServerNettyConfigure.class)
    public Consumer<SocketChannel> serverBootstrapConfigure(
            @Qualifier(BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP) EventExecutorGroup eventExecutorGroup,
            @Qualifier(BEAN_NAME_NETTY_HANDLER_NAME_1078_HEART_BEAT) Jt1078TerminalHeatBeatHandler heatBeatHandler,
            Jt1078DispatcherChannelHandler jt1078DispatcherChannelHandler) {

        return ch -> {

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
        };
    }

    @Bean(BEAN_NAME_1078_SERVER_BOOTSTRAP_CONFIGURE)
    @ConditionalOnBean(Jt1078ServerNettyConfigure.DefaultJt1078ServerNettyConfigure.class)
    public Consumer<ServerBootstrap> serverBootstrapConfigure() {
        return serverBootstrap -> serverBootstrap
                .option(ChannelOption.SO_BACKLOG, this.serverProps.getProtocol().getMaxFrameLength())
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    @Bean(initMethod = "doStart", destroyMethod = "doStop")
    @ConditionalOnMissingBean
    public Jt1078NettyTcpServer jt1078NettyTcpServer(Jt1078ServerNettyConfigure configure) {
        final Jt1078NettyTcpServer server = new Jt1078NettyTcpServer(
                "1078-tcp-server",
                configure,
                new Jt1078NettyChildHandlerInitializer(configure)
        );

        final Jt1078NettyTcpServerProps props = this.serverProps.getServer();
        server.setPort(props.getPort());
        server.setBossThreadCount(props.getBossThreadCount());
        server.setWorkThreadCount(props.getWorkerThreadCount());
        return server;
    }
}
