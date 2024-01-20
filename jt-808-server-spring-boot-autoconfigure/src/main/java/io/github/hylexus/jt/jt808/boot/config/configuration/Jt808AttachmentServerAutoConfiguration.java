package io.github.hylexus.jt.jt808.boot.config.configuration;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.boot.props.attachment.AttachmentServerProps;
import io.github.hylexus.jt.jt808.boot.props.msg.processor.MsgProcessorExecutorGroupProps;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGeneratorFactory;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionEventListener;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestRouteExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.support.extension.attachment.*;
import io.github.hylexus.jt.jt808.support.extension.attachment.impl.DefaultAttachmentJt808CommandSender;
import io.github.hylexus.jt.jt808.support.extension.attachment.impl.DefaultAttachmentJt808SessionManager;
import io.github.hylexus.jt.jt808.support.extension.attachment.impl.SimpleAttachmentJt808RequestProcessor;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.Comparator;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;

@Slf4j
@ConditionalOnProperty(prefix = "jt808.attachment-server", name = "enabled", havingValue = "true", matchIfMissing = false)
public class Jt808AttachmentServerAutoConfiguration {
    private final Jt808ServerProps instructionServerProps;

    public Jt808AttachmentServerAutoConfiguration(Jt808ServerProps instructionServerProps) {
        this.instructionServerProps = instructionServerProps;
    }

    @Bean
    @ConditionalOnMissingBean
    public AttachmentJt808SessionManager attachmentJt808SessionManager(ObjectProvider<Jt808SessionEventListener> listeners, Jt808FlowIdGeneratorFactory factory) {
        final AttachmentJt808SessionManager manager = new DefaultAttachmentJt808SessionManager(factory);
        listeners.stream().sorted(Comparator.comparing(OrderedComponent::getOrder)).forEach(manager::addListener);
        return manager;
    }

    @Bean
    @ConditionalOnMissingBean
    public AttachmentJt808CommandSender attachmentJt808CommandSender(
            AttachmentJt808SessionManager sessionManager, Jt808MsgEncoder encoder,
            Jt808AnnotationBasedEncoder annotationBasedEncoder) {
        return new DefaultAttachmentJt808CommandSender(sessionManager, encoder, annotationBasedEncoder);
    }

    @Bean
    @ConditionalOnMissingBean(SimpleAttachmentJt808RequestProcessor.class)
    public SimpleAttachmentJt808RequestProcessor simpleAttachmentJt808RequestProcessor(
            Jt808MsgDecoder jt808MsgDecoder,
            AttachmentJt808SessionManager sessionManager,
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

    @Bean
    @ConditionalOnMissingBean
    public AttachmentJt808DispatchChannelHandlerAdapter attachmentJt808DispatchChannelHandlerAdapter(
            AttachmentJt808RequestProcessor requestProcessor,
            AttachmentJt808SessionManager sessionManager) {

        return new AttachmentJt808DispatchChannelHandlerAdapter(requestProcessor, sessionManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808AttachmentServerNettyConfigure jt808AttachmentServerNettyConfigure(
            AttachmentJt808DispatchChannelHandlerAdapter attachmentJt808DispatchChannelHandlerAdapter,
            @Qualifier(BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP) EventExecutorGroup executors) {

        final AttachmentServerProps attachmentServerProps = instructionServerProps.getAttachmentServer();
        return new Jt808AttachmentServerNettyConfigure.DefaultJt808AttachmentServerNettyConfigure(
                new Jt808AttachmentServerNettyConfigure.DefaultJt808AttachmentServerNettyConfigure.BuiltInServerBootstrapProps(
                        instructionServerProps.getProtocol().getMaxFrameLength(),
                        attachmentServerProps.getMaxFrameLength()
                ),
                attachmentJt808DispatchChannelHandlerAdapter,
                executors
        );
    }

    @Bean(initMethod = "doStart", destroyMethod = "doStop")
    @ConditionalOnMissingBean
    public Jt808NettyTcpAttachmentServer jt808NettyTcpAttachmentServer(Jt808AttachmentServerNettyConfigure configure) {
        final Jt808NettyTcpAttachmentServer server = new Jt808NettyTcpAttachmentServer(
                "808-tcp-attachment-server",
                configure
        );

        final AttachmentServerProps nettyProps = instructionServerProps.getAttachmentServer();
        server.setPort(nettyProps.getPort());
        server.setBossThreadCount(nettyProps.getBossThreadCount());
        server.setWorkThreadCount(nettyProps.getWorkerThreadCount());
        return server;
    }
}
