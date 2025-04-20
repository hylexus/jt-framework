package io.github.hylexus.jt.jt808.boot.config.configuration;

import io.github.hylexus.jt.jt808.boot.config.BuiltinJt808AttachmentServerNettyConfigure;
import io.github.hylexus.jt.jt808.boot.config.condition.ConditionalOnJt808Server;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.boot.props.msg.processor.MsgProcessorExecutorGroupProps;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestRouteExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.support.extension.attachment.*;
import io.github.hylexus.jt.jt808.support.extension.attachment.impl.SimpleAttachmentJt808RequestProcessor;
import io.github.hylexus.jt.netty.JtServerNettyConfigure;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;
import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_NETTY_HANDLER_NAME_ATTACHMENT_808_HEART_BEAT;

@Slf4j
@ConditionalOnJt808Server(type = ConditionalOnJt808Server.Type.ATTACHMENT_SERVER)
public class Jt808AttachmentServerAutoConfiguration {

    private final Jt808ServerProps instructionServerProps;

    public Jt808AttachmentServerAutoConfiguration(Jt808ServerProps instructionServerProps) {
        this.instructionServerProps = instructionServerProps;
    }

    @Bean
    @ConditionalOnMissingBean(SimpleAttachmentJt808RequestProcessor.class)
    public SimpleAttachmentJt808RequestProcessor simpleAttachmentJt808RequestProcessor(
            Jt808MsgDecoder jt808MsgDecoder,
            Jt808SessionManager sessionManager,
            Jt808RequestRouteExceptionHandler routeExceptionHandler,
            Jt808RequestMsgDispatcher msgDispatcher,
            Jt808DispatcherHandler dispatcherHandler) {
        return new SimpleAttachmentJt808RequestProcessor(jt808MsgDecoder, sessionManager, routeExceptionHandler, msgDispatcher, dispatcherHandler);
    }

    @Bean(name = BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
    @ConditionalOnMissingBean(name = BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
    public EventExecutorGroup eventExecutorGroup() {

        final MsgProcessorExecutorGroupProps poolProps = instructionServerProps.getAttachmentServer().getMsgProcessor().getExecutorGroup();
        final DefaultThreadFactory threadFactory = new DefaultThreadFactory(poolProps.getPoolName());

        log.info("MsgProcessorConfig = {}", poolProps);
        return new DefaultEventExecutorGroup(
                poolProps.getThreadCount(),
                threadFactory,
                poolProps.getMaxPendingTasks(),
                RejectedExecutionHandlers.reject()
        );
    }

    @Bean(BEAN_NAME_NETTY_HANDLER_NAME_ATTACHMENT_808_HEART_BEAT)
    @ConditionalOnMissingBean(name = BEAN_NAME_NETTY_HANDLER_NAME_ATTACHMENT_808_HEART_BEAT)
    public AttachmentJt808TerminalHeatBeatHandler heatBeatHandler() {
        return new AttachmentJt808TerminalHeatBeatHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public AttachmentJt808DispatchChannelHandlerAdapter attachmentJt808DispatchChannelHandlerAdapter(AttachmentJt808RequestProcessor requestProcessor) {
        return new AttachmentJt808DispatchChannelHandlerAdapter(requestProcessor);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808AttachmentServerNettyConfigure jt808AttachmentServerNettyConfigure(
            AttachmentJt808TerminalHeatBeatHandler heatBeatHandler,
            AttachmentJt808DispatchChannelHandlerAdapter attachmentJt808DispatchChannelHandlerAdapter,
            @Qualifier(BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP) EventExecutorGroup executors) {

        return new BuiltinJt808AttachmentServerNettyConfigure(
                instructionServerProps,
                heatBeatHandler,
                executors,
                attachmentJt808DispatchChannelHandlerAdapter
        );
    }

    @Bean(initMethod = "doStart", destroyMethod = "doStop")
    @ConditionalOnMissingBean
    public Jt808NettyTcpAttachmentServer jt808NettyTcpAttachmentServer(Jt808AttachmentServerNettyConfigure configure, JtServerNettyConfigure.ConfigurationProvider provider) {
        final Jt808NettyTcpAttachmentServer server = new Jt808NettyTcpAttachmentServer(
                "TcpServer(JT/T-808-Attachment)",
                configure,
                provider
        );

        server.setPort(instructionServerProps.getAttachmentServer().getPort());
        return server;
    }
}
