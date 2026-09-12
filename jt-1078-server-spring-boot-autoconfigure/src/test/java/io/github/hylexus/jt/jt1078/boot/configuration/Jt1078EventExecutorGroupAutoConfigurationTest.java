package io.github.hylexus.jt.jt1078.boot.configuration;

import io.github.hylexus.jt.jt1078.boot.configuration.netty.Jt1078NettyAutoConfiguration;
import io.github.hylexus.jt.jt1078.boot.props.Jt1078ServerProps;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078DispatcherChannelHandler;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078NettyTcpServer;
import io.github.hylexus.jt.netty.DefaultJtEventExecutorGroupProvider;
import io.github.hylexus.jt.netty.JtEventExecutorGroupProvider;
import io.netty.util.concurrent.EventExecutorGroup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.hylexus.jt.jt1078.Jt1078ProtocolConstant.BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Jt1078EventExecutorGroupAutoConfigurationTest {

    @Test
    void shouldExposeExecutorThroughProviderAndCloseDefaultProvider() {
        final AtomicReference<DefaultJtEventExecutorGroupProvider> providerRef = new AtomicReference<>();

        this.contextRunner().run(context -> {
            final Object bean = context.getBean(BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP);
            // noinspection resource
            assertInstanceOf(DefaultJtEventExecutorGroupProvider.class, bean);
            assertFalse(bean instanceof ScheduledExecutorService);
            assertTrue(context.getBeansOfType(ScheduledExecutorService.class).isEmpty());

            final DefaultJtEventExecutorGroupProvider provider = (DefaultJtEventExecutorGroupProvider) bean;
            final BuiltinJt1078ServerNettyConfigure nettyConfigure = context.getBean(BuiltinJt1078ServerNettyConfigure.class);
            assertSame(provider.getEventExecutorGroup(), nettyConfigure.eventExecutorGroup);
            providerRef.set(provider);
        });

        assertTrue(providerRef.get().getEventExecutorGroup().isShuttingDown());
    }

    @Test
    void shouldBackOffForNamedCustomProviderWithoutManagingItsExecutor() {
        final EventExecutorGroup eventExecutorGroup = mock(EventExecutorGroup.class);
        final JtEventExecutorGroupProvider provider = () -> eventExecutorGroup;

        this.contextRunner()
                .withBean(BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP, JtEventExecutorGroupProvider.class, () -> provider)
                .run(context -> {
                    assertSame(provider, context.getBean(BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP));
                    assertSame(eventExecutorGroup, context.getBean(BuiltinJt1078ServerNettyConfigure.class).eventExecutorGroup);
                    assertTrue(context.getBeansOfType(ScheduledExecutorService.class).isEmpty());
                });

        verify(eventExecutorGroup, never()).shutdownGracefully();
    }

    private ApplicationContextRunner contextRunner() {
        return new ApplicationContextRunner()
                .withUserConfiguration(Jt1078NettyAutoConfiguration.class)
                .withBean(Jt1078ServerProps.class, Jt1078ServerProps::new)
                .withBean(Jt1078SessionManager.class, () -> mock(Jt1078SessionManager.class))
                .withBean(Jt1078DispatcherChannelHandler.class, () -> mock(Jt1078DispatcherChannelHandler.class))
                .withBean(Jt1078NettyTcpServer.class, () -> mock(Jt1078NettyTcpServer.class));
    }

}
