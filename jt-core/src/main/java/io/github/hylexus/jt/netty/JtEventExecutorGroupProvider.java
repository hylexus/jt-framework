package io.github.hylexus.jt.netty;

import io.netty.util.concurrent.EventExecutorGroup;

/**
 * 提供用于处理协议消息的 Netty 事件执行器组。
 *
 * @author Codex
 * @author hylexus
 * @see <a href="https://github.com/hylexus/jt-framework/issues/101">issues#101</a>
 * @see "io.github.hylexus.jt.jt808.spec.Jt808ServerSchedulerFactory"
 */
public interface JtEventExecutorGroupProvider {

    /**
     * 返回与当前 Provider 关联的稳定事件执行器组。
     * 在当前 Provider 的生命周期内，多次调用必须返回同一个实例。
     *
     * @return 事件执行器组
     */
    EventExecutorGroup getEventExecutorGroup();

}
