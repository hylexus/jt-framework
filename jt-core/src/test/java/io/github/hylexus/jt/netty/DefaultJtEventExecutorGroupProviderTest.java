package io.github.hylexus.jt.netty;

import io.netty.util.concurrent.EventExecutorGroup;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DefaultJtEventExecutorGroupProviderTest {

    @Test
    void shouldHideEventExecutorGroupTypeAndReturnStableInstance() {
        final EventExecutorGroup eventExecutorGroup = mock(EventExecutorGroup.class);
        final DefaultJtEventExecutorGroupProvider provider = new DefaultJtEventExecutorGroupProvider(eventExecutorGroup);

        assertFalse(EventExecutorGroup.class.isAssignableFrom(JtEventExecutorGroupProvider.class));
        assertFalse(ScheduledExecutorService.class.isAssignableFrom(JtEventExecutorGroupProvider.class));
        assertFalse(ScheduledExecutorService.class.isAssignableFrom(DefaultJtEventExecutorGroupProvider.class));
        assertSame(eventExecutorGroup, provider.getEventExecutorGroup());
        assertSame(provider.getEventExecutorGroup(), provider.getEventExecutorGroup());
    }

    @Test
    void shouldShutdownOwnedEventExecutorGroupOnce() {
        final EventExecutorGroup eventExecutorGroup = mock(EventExecutorGroup.class);
        final DefaultJtEventExecutorGroupProvider provider = new DefaultJtEventExecutorGroupProvider(eventExecutorGroup);

        provider.close();
        provider.close();

        verify(eventExecutorGroup, times(1)).shutdownGracefully();
        assertSame(eventExecutorGroup, provider.getEventExecutorGroup());
    }

}
