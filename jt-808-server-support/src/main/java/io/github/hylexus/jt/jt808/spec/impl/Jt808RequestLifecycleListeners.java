package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListener;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;

/**
 * @author hylexus
 */
@Slf4j
public class Jt808RequestLifecycleListeners implements Jt808RequestLifecycleListener {
    private final List<Jt808RequestLifecycleListener> listeners;

    public Jt808RequestLifecycleListeners(List<Jt808RequestLifecycleListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public boolean beforeDecode(ByteBuf request, Channel channel) {
        return this.forEach(listener -> listener.beforeDecode(request, channel));
    }

    @Override
    public boolean beforeDispatch(Jt808Request request) {
        return this.forEach(listener -> listener.beforeDispatch(request));
    }

    @Override
    public boolean beforeDispatch(Jt808ServerExchange exchange) {
        return this.forEach(listener -> listener.beforeDispatch(exchange));
    }

    @Override
    public boolean beforeHandle(Jt808ServerExchange exchange, Object handler) {
        return this.forEach(listener -> listener.beforeHandle(exchange, handler));
    }

    @Override
    public boolean beforeEncode(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult) {
        return this.forEach(listener -> listener.beforeEncode(exchange, handlerResult));
    }

    @Override
    public boolean beforeResponse(Jt808ServerExchange exchange, Jt808HandlerResult handlerResult, ByteBuf response) {
        return this.forEach(listener -> listener.beforeResponse(exchange, handlerResult, response));
    }

    private boolean forEach(Function<Jt808RequestLifecycleListener, Boolean> function) {
        boolean continueProcess = true;
        for (Jt808RequestLifecycleListener listener : listeners) {
            try {
                final boolean success = function.apply(listener);
                if (!success) {
                    continueProcess = false;
                }
            } catch (Throwable e) {
                log.error("An error occurred while invoke [{}]", listener.getClass());
            }
        }
        return continueProcess;
    }
}
