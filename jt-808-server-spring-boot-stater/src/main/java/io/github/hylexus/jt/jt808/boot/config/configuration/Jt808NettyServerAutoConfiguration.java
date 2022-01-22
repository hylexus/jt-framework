package io.github.hylexus.jt.jt808.boot.config.configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.boot.props.msg.processor.MsgProcessorThreadPoolProps;
import io.github.hylexus.jt.jt808.boot.props.server.Jt808NettyTcpServerProps;
import io.github.hylexus.jt.jt808.spec.Jt808RequestMsgQueue;
import io.github.hylexus.jt.jt808.spec.Jt808RequestMsgQueueListener;
import io.github.hylexus.jt.jt808.spec.impl.request.queue.LocalEventBus;
import io.github.hylexus.jt.jt808.spec.impl.request.queue.LocalEventBusListener;
import io.github.hylexus.jt.jt808.spec.session.DefaultJt808SessionManager;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGeneratorFactory;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionEventListener;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageStorage;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.LocalEventBusDispatcher;
import io.github.hylexus.jt.jt808.support.netty.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_NETTY_HANDLER_NAME_808_HEART_BEAT;

/**
 * @author hylexus
 */
public class Jt808NettyServerAutoConfiguration {

    private final Jt808ServerProps serverProps;

    public Jt808NettyServerAutoConfiguration(Jt808ServerProps serverProps) {
        this.serverProps = serverProps;
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808FlowIdGeneratorFactory jt808FlowIdGeneratorFactory() {
        return new Jt808FlowIdGeneratorFactory.DefaultJt808FlowIdGeneratorFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808SessionManager jt808SessionManager(ObjectProvider<Jt808SessionEventListener> listeners, Jt808FlowIdGeneratorFactory factory) {
        final Jt808SessionManager manager = DefaultJt808SessionManager.getInstance(factory);
        listeners.stream().sorted(Comparator.comparing(OrderedComponent::getOrder)).forEach(manager::addListener);
        return manager;
    }

    @Bean(BEAN_NAME_NETTY_HANDLER_NAME_808_HEART_BEAT)
    @ConditionalOnMissingBean(name = BEAN_NAME_NETTY_HANDLER_NAME_808_HEART_BEAT)
    public Jt808TerminalHeatBeatHandler heatBeatHandler(Jt808SessionManager jt808SessionManager) {
        return new Jt808TerminalHeatBeatHandler(jt808SessionManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestMsgQueue requestMsgQueue() {
        final MsgProcessorThreadPoolProps poolProps = serverProps.getMsgProcessor().getThreadPool();
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(poolProps.getThreadNameFormat())
                .setDaemon(true)
                .build();
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(
                poolProps.getCorePoolSize(),
                poolProps.getMaximumPoolSize(),
                poolProps.getKeepAliveTime().getSeconds(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(poolProps.getBlockingQueueSize()),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        return new LocalEventBus(executor);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestMsgDispatcher requestMsgDispatcher(Jt808RequestMsgQueue requestMsgQueue) {
        return new LocalEventBusDispatcher(requestMsgQueue);
    }

    @Bean
    @ConditionalOnMissingBean(Jt808DispatchChannelHandlerAdapter.class)
    public Jt808DispatchChannelHandlerAdapter jt808ChannelHandlerAdapter(
            Jt808SessionManager jt808SessionManager, Jt808MsgDecoder decoder,
            Jt808RequestMsgDispatcher requestMsgDispatcher, Jt808ExceptionHandler exceptionHandler) {

        return new Jt808DispatchChannelHandlerAdapter(decoder,
                jt808SessionManager, requestMsgDispatcher, exceptionHandler
        );
    }

    @Bean
    @ConditionalOnMissingBean(Jt808ServerNettyConfigure.class)
    public Jt808ServerNettyConfigure jt808ServerNettyConfigure(
            Jt808TerminalHeatBeatHandler heatBeatHandler,
            Jt808DispatchChannelHandlerAdapter channelHandlerAdapter) {

        final Jt808NettyTcpServerProps.IdleStateHandlerProps idleStateHandler = this.serverProps.getServer().getIdleStateHandler();
        final Jt808ServerNettyConfigure.DefaultJt808ServerNettyConfigure.BuiltInServerBootstrapProps serverBootstrapProps
                = new Jt808ServerNettyConfigure.DefaultJt808ServerNettyConfigure.BuiltInServerBootstrapProps(
                this.serverProps.getProtocol().getMaxFrameLength(),
                new Jt808ServerNettyConfigure.DefaultJt808ServerNettyConfigure.IdleStateHandlerProps(
                        idleStateHandler.isEnabled(),
                        idleStateHandler.getReaderIdleTime(),
                        idleStateHandler.getWriterIdleTime(),
                        idleStateHandler.getAllIdleTime()
                )
        );

        return new Jt808ServerNettyConfigure.DefaultJt808ServerNettyConfigure(serverBootstrapProps, heatBeatHandler, channelHandlerAdapter);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808NettyTcpServer jt808NettyTcpServer(Jt808ServerNettyConfigure configure) {
        final Jt808NettyTcpServer server = new Jt808NettyTcpServer(
                "808-tcp-server",
                configure,
                new Jt808NettyChildHandlerInitializer(configure)
        );

        final Jt808NettyTcpServerProps nettyProps = serverProps.getServer();
        server.setPort(nettyProps.getPort());
        server.setBossThreadCount(nettyProps.getBossThreadCount());
        server.setWorkThreadCount(nettyProps.getWorkerThreadCount());
        return server;
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestMsgQueueListener msgQueueListener(
            Jt808RequestMsgQueue requestMsgQueue, Jt808DispatcherHandler dispatcherHandler,
            Jt808SessionManager sessionManager, Jt808RequestSubPackageStorage subPackageStorage,
            Jt808RequestSubPackageEventListener requestSubPackageEventListener) {
        return new LocalEventBusListener((LocalEventBus) requestMsgQueue, dispatcherHandler, sessionManager, subPackageStorage, requestSubPackageEventListener);
    }
}
