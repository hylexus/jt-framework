package io.github.hylexus.jt.jt808.boot.config.configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.boot.props.msg.processor.MsgProcessorThreadPoolProps;
import io.github.hylexus.jt.jt808.boot.props.server.Jt808NettyTcpServerProps;
import io.github.hylexus.jt.jt808.request.queue.RequestMsgQueue;
import io.github.hylexus.jt.jt808.request.queue.RequestMsgQueueListener;
import io.github.hylexus.jt.jt808.request.queue.impl.LocalEventBus;
import io.github.hylexus.jt.jt808.request.queue.impl.LocalEventBusListener;
import io.github.hylexus.jt.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.session.SessionManager;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808SubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808SubPackageStorage;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.LocalEventBusDispatcher;
import io.github.hylexus.jt.jt808.support.netty.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt.config.JtProtocolConstant.*;

/**
 * @author hylexus
 */
public class Jt808NettyServerAutoConfiguration {

    private final Jt808ServerProps serverProps;

    public Jt808NettyServerAutoConfiguration(Jt808ServerProps serverProps) {
        this.serverProps = serverProps;
    }

    @Bean(BEAN_NAME_JT808_SESSION_MANAGER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_SESSION_MANAGER)
    public Jt808SessionManager supplyJt808SessionManager() {
        return SessionManager.getInstance();
    }

    @Bean(NETTY_HANDLER_NAME_808_HEART_BEAT)
    @ConditionalOnMissingBean(name = NETTY_HANDLER_NAME_808_HEART_BEAT)
    public HeatBeatHandler heatBeatHandler(Jt808SessionManager jt808SessionManager) {
        return new HeatBeatHandler(jt808SessionManager);
    }


    @Bean(BEAN_NAME_JT808_REQ_MSG_QUEUE)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_REQ_MSG_QUEUE)
    public RequestMsgQueue requestMsgQueue() {
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

    @Bean(BEAN_NAME_JT808_REQ_MSG_DISPATCHER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_REQ_MSG_DISPATCHER)
    public Jt808RequestMsgDispatcher requestMsgDispatcher(RequestMsgQueue requestMsgQueue) {
        return new LocalEventBusDispatcher(requestMsgQueue);
    }

    @Bean
    @ConditionalOnMissingBean(Jt808DispatchChannelHandlerAdapter.class)
    public Jt808DispatchChannelHandlerAdapter jt808ChannelHandlerAdapter(
            Jt808SessionManager jt808SessionManager, Jt808MsgDecoder decoder,
            Jt808RequestMsgDispatcher requestMsgDispatcher, Jt808ExceptionHandler exceptionHandler) {

        return new Jt808DispatchChannelHandlerAdapter(
                serverProps.getProtocol().getVersion(), decoder,
                jt808SessionManager, requestMsgDispatcher, exceptionHandler
        );
    }

    @Bean
    @ConditionalOnMissingBean(Jt808ServerNettyConfigure.class)
    public Jt808ServerNettyConfigure jt808ServerNettyConfigure(HeatBeatHandler heatBeatHandler, Jt808DispatchChannelHandlerAdapter channelHandlerAdapter) {

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

    @Bean(BEAN_NAME_JT808_NETTY_TCP_SERVER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_NETTY_TCP_SERVER)
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
    public Jt808SubPackageStorage.SubPackageEventListener debugSubPackageEventListener() {
        return new Jt808SubPackageStorage.DefaultDebuggingSubPackageEventListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808SubPackageStorage jt808SubPackageStorage(ObjectProvider<Jt808SubPackageStorage.SubPackageEventListener> subPackageEventListeners) {
        final DefaultJt808SubPackageStorage storage = new DefaultJt808SubPackageStorage(128, Duration.ofSeconds(30));
        subPackageEventListeners.stream().sorted(Comparator.comparing(OrderedComponent::getOrder)).forEach(storage::addListener);
        return storage;
    }

    @Bean(BEAN_NAME_JT808_REQ_MSG_QUEUE_LISTENER)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_REQ_MSG_QUEUE_LISTENER)
    public RequestMsgQueueListener msgQueueListener(
            RequestMsgQueue requestMsgQueue, Jt808DispatcherHandler dispatcherHandler,
            Jt808SessionManager sessionManager, Jt808SubPackageStorage jt808SubPackageStorage) {
        return new LocalEventBusListener(
                (LocalEventBus) requestMsgQueue, dispatcherHandler, sessionManager,
                jt808SubPackageStorage
        );
    }
}
