package io.github.hylexus.jt.jt1078.spec.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestLifecycleListener;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;

@Slf4j
public class Jt1078RequestLifecycleListeners implements Jt1078RequestLifecycleListener {

    private final List<Jt1078RequestLifecycleListener> listeners;

    public Jt1078RequestLifecycleListeners(List<Jt1078RequestLifecycleListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public boolean beforeDecode(ByteBuf request, Channel channel) {
        return this.forEach(listener -> listener.beforeDecode(request, channel));
    }

    @Override
    public boolean beforeDispatch(Jt1078Request request, Channel channel) {
        return this.forEach(listener -> listener.beforeDispatch(request, channel));
    }

    @Override
    public boolean beforeHandle(Jt1078Request request) {
        return this.forEach(listener -> listener.beforeHandle(request));
    }

    private boolean forEach(Function<Jt1078RequestLifecycleListener, Boolean> function) {
        boolean continueProcess = true;
        for (Jt1078RequestLifecycleListener listener : listeners) {
            try {
                final boolean success = function.apply(listener);
                if (!success) {
                    continueProcess = false;
                }
            } catch (Throwable e) {
                log.error("An error occurred while invoke [" + listener.getClass() + "]", e);
            }
        }
        return continueProcess;
    }
}
