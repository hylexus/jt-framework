package io.github.hylexus.jt.jt808.boot.config.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.boot.props.msg.processor.MsgProcessorExecutorGroupProps;
import io.github.hylexus.jt.jt808.boot.props.server.Jt808NettyTcpServerProps;
import io.github.hylexus.jt.jt808.spec.Jt808RequestFilter;
import io.github.hylexus.jt.jt808.spec.Jt808RequestMsgQueueListener;
import io.github.hylexus.jt.jt808.spec.impl.request.queue.DefaultJt808RequestMsgQueueListener;
import io.github.hylexus.jt.jt808.spec.impl.request.queue.FilteringJt808RequestMsgQueueListener;
import io.github.hylexus.jt.jt808.spec.session.DefaultJt808SessionManager;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGeneratorFactory;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionEventListener;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestRouteExceptionHandler;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808RequestRouteExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestProcessor;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.DefaultJt808RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.SimpleJt808RequestProcessor;
import io.github.hylexus.jt.jt808.support.netty.*;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;
import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_NETTY_HANDLER_NAME_808_HEART_BEAT;

/**
 * @author hylexus
 */
@Slf4j
@Import({
        Jt808NettyServerAutoConfiguration.Jt808ServerParamPrinterConfig.class,
        Jt808AttachmentServerAutoConfiguration.class,
})
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

    //@Bean
    //@ConditionalOnMissingBean
    // public Jt808RequestMsgQueue requestMsgQueue() {
    //    final MsgProcessorThreadPoolProps poolProps = serverProps.getMsgProcessor().getThreadPool();
    //    final ThreadFactory threadFactory = new ThreadFactoryBuilder()
    //            .setNameFormat(poolProps.getThreadNameFormat())
    //            .setDaemon(true)
    //            .build();
    //    final ThreadPoolExecutor executor = new ThreadPoolExecutor(
    //            poolProps.getCorePoolSize(),
    //            poolProps.getMaximumPoolSize(),
    //            poolProps.getKeepAliveTime().getSeconds(),
    //            TimeUnit.SECONDS,
    //            new LinkedBlockingQueue<>(poolProps.getBlockingQueueSize()),
    //            threadFactory,
    //            new ThreadPoolExecutor.AbortPolicy()
    //    );
    //    return new LocalEventBus(executor);
    //}

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestMsgDispatcher requestMsgDispatcher(Jt808RequestMsgQueueListener listener) {
        return new DefaultJt808RequestMsgDispatcher(listener);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestRouteExceptionHandler jt808RequestRouteExceptionHandler() {
        return new DefaultJt808RequestRouteExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestProcessor jt808RequestProcessor(
            Jt808SessionManager jt808SessionManager, Jt808MsgDecoder decoder,
            Jt808RequestMsgDispatcher dispatcher,
            Jt808ExceptionHandler exceptionHandler,
            Jt808RequestRouteExceptionHandler routeExceptionHandler) {
        return new SimpleJt808RequestProcessor(decoder, dispatcher, jt808SessionManager, exceptionHandler, routeExceptionHandler);
    }

    @Bean
    @ConditionalOnMissingBean(Jt808DispatchChannelHandlerAdapter.class)
    public Jt808DispatchChannelHandlerAdapter jt808ChannelHandlerAdapter(
            Jt808SessionManager jt808SessionManager,
            Jt808RequestProcessor requestProcessor) {

        return new Jt808DispatchChannelHandlerAdapter(requestProcessor, jt808SessionManager);
    }

    @Bean(name = BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
    public EventExecutorGroup eventExecutorGroup() {

        final MsgProcessorExecutorGroupProps poolProps = serverProps.getMsgProcessor().getExecutorGroup();
        final DefaultThreadFactory threadFactory = new DefaultThreadFactory(poolProps.getPoolName());

        log.info("MsgProcessorConfig = {}", poolProps);
        return new DefaultEventExecutorGroup(
                poolProps.getThreadCount(),
                threadFactory,
                poolProps.getMaxPendingTasks(),
                RejectedExecutionHandlers.reject()
        );
    }

    @Bean
    @ConditionalOnMissingBean(Jt808ServerNettyConfigure.class)
    public Jt808ServerNettyConfigure jt808ServerNettyConfigure(
            @Qualifier(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP) EventExecutorGroup eventExecutorGroup,
            Jt808TerminalHeatBeatHandler heatBeatHandler,
            Jt808DispatchChannelHandlerAdapter channelHandlerAdapter) {

        final Jt808NettyTcpServerProps.IdleStateHandlerProps idleStateHandler = this.serverProps.getServer().getIdleStateHandler();
        final Jt808ServerNettyConfigure.DefaultJt808ServerNettyConfigure.BuiltInServerBootstrapProps serverBootstrapProps = new Jt808ServerNettyConfigure.DefaultJt808ServerNettyConfigure.BuiltInServerBootstrapProps(
                this.serverProps.getProtocol().getMaxFrameLength(),
                new Jt808ServerNettyConfigure.DefaultJt808ServerNettyConfigure.IdleStateHandlerProps(
                        idleStateHandler.isEnabled(),
                        idleStateHandler.getReaderIdleTime(),
                        idleStateHandler.getWriterIdleTime(),
                        idleStateHandler.getAllIdleTime()
                )
        );

        return new Jt808ServerNettyConfigure.DefaultJt808ServerNettyConfigure(serverBootstrapProps, heatBeatHandler, channelHandlerAdapter, eventExecutorGroup);
    }

    @Bean(initMethod = "doStart", destroyMethod = "doStop")
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
    @ConditionalOnProperty(prefix = "jt808.features.request-filter", name = "enabled", havingValue = "true", matchIfMissing = false)
    public Jt808RequestMsgQueueListener filteringMsgQueueListener(
            Jt808DispatcherHandler dispatcherHandler,
            // Jt808SessionManager sessionManager,
            Jt808RequestSubPackageStorage subPackageStorage,
            Jt808RequestSubPackageEventListener requestSubPackageEventListener,
            ObjectProvider<Jt808RequestFilter> filters) {

        final List<Jt808RequestFilter> allFilters = filters.orderedStream().collect(Collectors.toList());
        return new FilteringJt808RequestMsgQueueListener(dispatcherHandler, subPackageStorage, requestSubPackageEventListener, allFilters);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestMsgQueueListener msgQueueListener(
            Jt808DispatcherHandler dispatcherHandler,
            // Jt808SessionManager sessionManager,
            Jt808RequestSubPackageStorage subPackageStorage,
            Jt808RequestSubPackageEventListener requestSubPackageEventListener) {
        return new DefaultJt808RequestMsgQueueListener(dispatcherHandler, subPackageStorage, requestSubPackageEventListener);
    }


    @ConditionalOnProperty(prefix = "jt808.features.program-param-printer", name = "enabled", havingValue = "true")
    // @ConditionalOnClass({ObjectMapper.class, JavaTimeModule.class})
    static class Jt808ServerParamPrinterConfig {
        @Bean
        public CommandLineRunner jt808ServerParamPrinter(Jt808ServerProps props) {
            return args -> {
                final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
                if (props.getFeatures().getProgramParamPrinter().isPretty()) {
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                }
                log.info("Jt808 server config ::: {}", objectMapper.writeValueAsString(props));
            };
        }
    }
}
