package io.github.hylexus.jt.jt808.boot.config;

import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808AttachmentServerAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808InstructionServerAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.Jt808ServerSchedulerFactory;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestProcessor;
import io.github.hylexus.jt.jt808.support.extension.attachment.Jt808NettyTcpAttachmentServer;
import io.github.hylexus.jt.jt808.support.extension.attachment.impl.SimpleAttachmentJt808RequestProcessor;
import io.github.hylexus.jt.jt808.support.netty.Jt808DispatchChannelHandlerAdapter;
import io.github.hylexus.jt.jt808.support.netty.Jt808NettyTcpServer;
import io.github.hylexus.jt.netty.DefaultJtEventExecutorGroupProvider;
import io.github.hylexus.jt.netty.JtEventExecutorGroupProvider;
import io.netty.util.concurrent.EventExecutorGroup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;
import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @see <a href="https://github.com/hylexus/jt-framework/issues/101">issues#101</a>
 */
class Jt808EventExecutorGroupAutoConfigurationTest {

    @Test
    void shouldExposeInstructionExecutorThroughProviderAndCloseDefaultProvider() {
        final AtomicReference<DefaultJtEventExecutorGroupProvider> providerRef = new AtomicReference<>();

        this.instructionRunner().run(context -> {
            final Object bean = context.getBean(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP);
            // noinspection resource
            assertInstanceOf(DefaultJtEventExecutorGroupProvider.class, bean);
            assertFalse(bean instanceof ScheduledExecutorService);
            assertTrue(context.getBeansOfType(ScheduledExecutorService.class).isEmpty());

            final DefaultJtEventExecutorGroupProvider provider = (DefaultJtEventExecutorGroupProvider) bean;
            final BuiltinJt808ServerNettyConfigure nettyConfigure = context.getBean(BuiltinJt808ServerNettyConfigure.class);
            assertSame(provider.getEventExecutorGroup(), nettyConfigure.eventExecutorGroup);
            providerRef.set(provider);
        });

        assertTrue(providerRef.get().getEventExecutorGroup().isShuttingDown());
    }

    @Test
    void shouldBackOffForNamedCustomInstructionProviderWithoutManagingItsExecutor() {
        final EventExecutorGroup eventExecutorGroup = mock(EventExecutorGroup.class);
        final JtEventExecutorGroupProvider provider = () -> eventExecutorGroup;

        this.instructionRunner()
                .withBean(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP, JtEventExecutorGroupProvider.class, () -> provider)
                .run(context -> {
                    assertSame(provider, context.getBean(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP));
                    assertSame(eventExecutorGroup, context.getBean(BuiltinJt808ServerNettyConfigure.class).eventExecutorGroup);
                    assertTrue(context.getBeansOfType(ScheduledExecutorService.class).isEmpty());
                });

        verify(eventExecutorGroup, never()).shutdownGracefully();
    }

    @Test
    void shouldKeepInstructionAndAttachmentProvidersIndependent() {
        this.instructionRunner()
                .withUserConfiguration(Jt808AttachmentServerAutoConfiguration.class)
                .withPropertyValues("jt808.attachment-server.enabled=true")
                .withBean(SimpleAttachmentJt808RequestProcessor.class, () -> mock(SimpleAttachmentJt808RequestProcessor.class))
                .withBean(Jt808NettyTcpAttachmentServer.class, () -> mock(Jt808NettyTcpAttachmentServer.class))
                .run(context -> {
                    final JtEventExecutorGroupProvider instructionProvider = context.getBean(
                            BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP, JtEventExecutorGroupProvider.class
                    );
                    final JtEventExecutorGroupProvider attachmentProvider = context.getBean(
                            BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP, JtEventExecutorGroupProvider.class
                    );
                    assertNotSame(instructionProvider, attachmentProvider);
                    assertNotSame(instructionProvider.getEventExecutorGroup(), attachmentProvider.getEventExecutorGroup());
                    assertSame(
                            attachmentProvider.getEventExecutorGroup(),
                            context.getBean(BuiltinJt808AttachmentServerNettyConfigure.class).eventExecutorGroup
                    );
                    assertTrue(context.getBeansOfType(ScheduledExecutorService.class).isEmpty());
                });
    }

    private ApplicationContextRunner instructionRunner() {
        return new ApplicationContextRunner()
                .withUserConfiguration(Jt808InstructionServerAutoConfiguration.class)
                .withPropertyValues(
                        "jt808.built-components.component-statistics.enabled=false",
                        "jt808.built-components.request-handlers.enabled=false"
                )
                .withBean(Jt808ServerProps.class, Jt808ServerProps::new)
                .withBean(Jt808SessionManager.class, () -> mock(Jt808SessionManager.class))
                .withBean(Jt808CommandSender.class, () -> mock(Jt808CommandSender.class))
                .withBean(Jt808RequestProcessor.class, () -> mock(Jt808RequestProcessor.class))
                .withBean(Jt808DispatchChannelHandlerAdapter.class, () -> mock(Jt808DispatchChannelHandlerAdapter.class))
                .withBean(Jt808NettyTcpServer.class, () -> mock(Jt808NettyTcpServer.class))
                .withBean(Jt808ServerSchedulerFactory.class, () -> mock(Jt808ServerSchedulerFactory.class));
    }

}
