package io.github.hylexus.jt.jt1078.support.dispatcher.impl;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestLifecycleListener;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestLifecycleListenerAware;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078RequestSubPackageCombiner;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestHandler;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestMsgDispatcher;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hylexus
 */
@Slf4j
public class DefaultJt1078RequestMsgDispatcher implements Jt1078RequestMsgDispatcher, Jt1078RequestLifecycleListenerAware {

    private final List<Jt1078RequestHandler> handlers;

    private Jt1078RequestLifecycleListener lifecycleListener;

    private final Jt1078RequestSubPackageCombiner subPackageCombiner;

    private final Jt1078RequestHandler loggingOnlyHandler = new Jt1078RequestHandler.LoggingJt1078RequestHandler();

    public DefaultJt1078RequestMsgDispatcher(List<Jt1078RequestHandler> handlers, Jt1078RequestSubPackageCombiner subPackageCombiner) {
        this.handlers = handlers.stream().sorted(Comparator.comparing(OrderedComponent::getOrder)).collect(Collectors.toList());
        this.subPackageCombiner = subPackageCombiner;
    }

    @Override
    public void doDispatch(Jt1078Request request) {
        try {
            this.subPackageCombiner.tryCombine(request).ifPresent(combinedRequest -> {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("combinedRequest: {}", HexStringUtils.byteBufToHexString(combinedRequest.body()));
                        log.debug("combinedRequest: {}", combinedRequest);
                    }
                    this.doDispatchInternal(combinedRequest);
                } finally {
                    combinedRequest.release();
                }
            });
        } finally {
            request.release();
        }
    }

    private void doDispatchInternal(Jt1078Request request) {
        final boolean continueProcess = this.lifecycleListener.beforeHandle(request);
        if (!continueProcess) {
            return;
        }
        this.getHandler(request).handle(request);
    }

    private Jt1078RequestHandler getHandler(Jt1078Request request) {
        for (Jt1078RequestHandler handler : this.handlers) {
            if (handler.support(request)) {
                return handler;
            }
        }
        return loggingOnlyHandler;
    }

    @Override
    public void setRequestLifecycleListener(Jt1078RequestLifecycleListener listener) {
        this.lifecycleListener = listener;
    }
}
