package io.github.hylexus.jt.netty;

import io.netty.util.concurrent.EventExecutorGroup;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 默认的事件执行器组 Provider，持有且管理单个 {@link EventExecutorGroup} 实例。
 *
 * @author Codex
 * @author hylexus
 * @see <a href="https://github.com/hylexus/jt-framework/issues/101">issues#101</a>
 * @see "io.github.hylexus.jt.jt808.spec.Jt808ServerSchedulerFactory"
 */
public class DefaultJtEventExecutorGroupProvider implements JtEventExecutorGroupProvider, AutoCloseable {

    private final EventExecutorGroup eventExecutorGroup;
    private final AtomicBoolean closed = new AtomicBoolean();

    public DefaultJtEventExecutorGroupProvider(EventExecutorGroup eventExecutorGroup) {
        this.eventExecutorGroup = Objects.requireNonNull(eventExecutorGroup, "eventExecutorGroup");
    }

    @Override
    public EventExecutorGroup getEventExecutorGroup() {
        return this.eventExecutorGroup;
    }

    @Override
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            this.eventExecutorGroup.shutdownGracefully();
        }
    }

}
