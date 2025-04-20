package io.github.hylexus.jt.jt808.boot.config.configuration;

import io.github.hylexus.jt.jt808.boot.config.BuiltinJt808ServerNettyConfigure;
import io.github.hylexus.jt.jt808.boot.config.condition.BuiltinComponentType;
import io.github.hylexus.jt.jt808.boot.config.condition.ConditionalOnJt808BuiltinComponentsEnabled;
import io.github.hylexus.jt.jt808.boot.config.condition.ConditionalOnJt808Server;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.boot.props.msg.processor.MsgProcessorExecutorGroupProps;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.Jt808ServerSchedulerFactory;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808CommandSender;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ServerSchedulerFactory;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManagerAware;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestRouteExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestProcessor;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin.BuiltinCommonHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin.BuiltinTerminalAuthenticationMsgHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin.BuiltinTerminalRegisterMsgHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.SimpleJt808RequestProcessor;
import io.github.hylexus.jt.jt808.support.netty.Jt808DispatchChannelHandlerAdapter;
import io.github.hylexus.jt.jt808.support.netty.Jt808NettyTcpServer;
import io.github.hylexus.jt.jt808.support.netty.Jt808ServerNettyConfigure;
import io.github.hylexus.jt.jt808.support.netty.Jt808TerminalHeatBeatHandler;
import io.github.hylexus.jt.netty.JtServerNettyConfigure;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;
import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_NETTY_HANDLER_NAME_808_HEART_BEAT;

@ConditionalOnJt808Server(type = ConditionalOnJt808Server.Type.INSTRUCTION_SERVER)
@Import({
        Jt808InstructionServerAutoConfiguration.Jt808SessionManagerBinder.class
})
public class Jt808InstructionServerAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(Jt808InstructionServerAutoConfiguration.class);
    private final Jt808ServerProps serverProps;

    public Jt808InstructionServerAutoConfiguration(Jt808ServerProps serverProps) {
        this.serverProps = serverProps;
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808CommandSender jt808CommandSender(
            Jt808SessionManager sessionManager, Jt808MsgEncoder encoder,
            Jt808AnnotationBasedEncoder annotationBasedEncoder) {
        return new DefaultJt808CommandSender(sessionManager, encoder, annotationBasedEncoder);
    }

    @Bean
    @ConditionalOnJt808BuiltinComponentsEnabled(BuiltinComponentType.REQUEST_HANDLER)
    public BuiltinTerminalRegisterMsgHandler builtinJt808RequestHandlerForDebugging() {
        return new BuiltinTerminalRegisterMsgHandler();
    }

    @Bean
    @ConditionalOnJt808BuiltinComponentsEnabled(BuiltinComponentType.REQUEST_HANDLER)
    public BuiltinTerminalAuthenticationMsgHandler builtinTerminalAuthenticationMsgHandlerForDebugging() {
        return new BuiltinTerminalAuthenticationMsgHandler();
    }

    @Bean
    @ConditionalOnJt808BuiltinComponentsEnabled(BuiltinComponentType.REQUEST_HANDLER)
    public BuiltinCommonHandler builtinCommonHandler() {
        return new BuiltinCommonHandler();
    }

    @Bean
    @ConditionalOnJt808BuiltinComponentsEnabled(BuiltinComponentType.COMPONENT_STATISTICS)
    public Jt808ServerComponentStatistics jt808ServerComponentStatistics() {
        return new Jt808ServerComponentStatistics();
    }

    @Bean(BEAN_NAME_NETTY_HANDLER_NAME_808_HEART_BEAT)
    @ConditionalOnMissingBean(name = BEAN_NAME_NETTY_HANDLER_NAME_808_HEART_BEAT)
    public Jt808TerminalHeatBeatHandler heatBeatHandler(Jt808SessionManager jt808SessionManager) {
        return new Jt808TerminalHeatBeatHandler(jt808SessionManager);
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
        return new BuiltinJt808ServerNettyConfigure(serverProps, eventExecutorGroup, channelHandlerAdapter, heatBeatHandler);
    }

    @Bean(initMethod = "doStart", destroyMethod = "doStop")
    @ConditionalOnMissingBean
    public Jt808NettyTcpServer jt808NettyTcpServer(Jt808ServerNettyConfigure configure, JtServerNettyConfigure.ConfigurationProvider provider) {
        final Jt808NettyTcpServer server = new Jt808NettyTcpServer(
                "TcpServer(JT/T-808-Instruction)",
                configure,
                provider
        );

        server.setPort(serverProps.getServer().getPort());
        return server;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "jt808.msg-handler", name = "enabled", havingValue = "true", matchIfMissing = true)
    Jt808ServerSchedulerFactory jt808ServerSchedulerFactory() {
        return new DefaultJt808ServerSchedulerFactory(serverProps.getMsgHandler().toExecutorProps());
    }


    @Slf4j
    static class Jt808SessionManagerBinder {
        public Jt808SessionManagerBinder(ApplicationContext applicationContext, Jt808SessionManager sessionManager) {
            this.doBind(applicationContext, sessionManager);
        }

        private void doBind(ApplicationContext applicationContext, Jt808SessionManager sessionManager) {
            applicationContext.getBeansOfType(Jt808SessionManagerAware.class).forEach((name, instance) -> {
                instance.setJt808SessionManager(sessionManager);
                log.info("--> Binding [{}] to [{}]", sessionManager.getClass().getName(), instance.getClass().getName());
            });
        }
    }
}
