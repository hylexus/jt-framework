package io.github.hylexus.jt.jt1078.support.dispatcher.impl;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestHandler;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestMsgDispatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hylexus
 */
@Slf4j
public class DefaultJt1078RequestMsgDispatcher implements Jt1078RequestMsgDispatcher {

    private final Jt1078SessionManager sessionManager;
    private final List<Jt1078RequestHandler> handlers;
    private final Jt1078RequestHandler loggingHandler = new Jt1078RequestHandler.LoggingJt1078RequestHandler();

    public DefaultJt1078RequestMsgDispatcher(Jt1078SessionManager sessionManager, List<Jt1078RequestHandler> handlers) {
        this.sessionManager = sessionManager;
        this.handlers = handlers.stream().sorted(Comparator.comparing(OrderedComponent::getOrder)).collect(Collectors.toList());
    }

    @Override
    public void doDispatch(Jt1078Request request) throws Throwable {
        try {
            this.doDispatchInternal(request);
        } finally {
            request.release();
        }
    }

    private void doDispatchInternal(Jt1078Request request) throws Throwable {

        // TODO mapping, adapter, result
        this.getHandler(request).handle(request);
    }

    private Jt1078RequestHandler getHandler(Jt1078Request request) {
        for (Jt1078RequestHandler handler : this.handlers) {
            if (handler.support(request)) {
                return handler;
            }
        }
        return loggingHandler;
    }

}
