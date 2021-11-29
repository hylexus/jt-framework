package io.github.hylexus.jt.jt808.support.dispatcher;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.exception.JtSessionNotFoundException;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.session.Jt808Session;
import io.github.hylexus.jt.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.exception.Jt808HandlerAdapterNotFoundException;
import io.github.hylexus.jt.jt808.support.exception.Jt808HandlerNotFoundException;
import io.github.hylexus.jt.jt808.support.exception.Jt808HandlerResultHandlerNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author hylexus
 */
@Slf4j
public class Jt808DispatcherHandler {
    private final Jt808SessionManager sessionManager;
    private final List<Jt808HandlerMapping> handlerMappings;
    private final List<Jt808HandlerAdapter> handlerAdapters;
    private final List<Jt808HandlerResultHandler> resultHandlers;
    private final Jt808ExceptionHandler exceptionHandler;

    public Jt808DispatcherHandler(
            Jt808SessionManager sessionManager,
            List<Jt808HandlerMapping> handlerMappings,
            List<Jt808HandlerAdapter> handlerAdapters,
            List<Jt808HandlerResultHandler> resultHandlers,
            Jt808ExceptionHandler exceptionHandler) {

        this.sessionManager = sessionManager;

        this.handlerMappings = this.sort(handlerMappings);
        this.handlerAdapters = this.sort(handlerAdapters);
        this.resultHandlers = this.sort(resultHandlers);
        this.exceptionHandler = exceptionHandler;
    }

    public void handleRequest(Jt808Request request) {
        final Jt808Session session = getSession(request);

        try {
            final Object handler;
            try {
                // 1. detect a handler that can handle the current request
                handler = this.getHandler(request, session);
            } catch (Throwable throwable) {
                final Jt808HandlerResult exceptionResult = this.invokeExceptionHandler(request, session, throwable, null);
                this.handleResult(request, session, exceptionResult);
                return;
            }

            final Jt808HandlerResult handlerResult;
            try {
                // 2. invoke handler to handle current request
                handlerResult = this.invokeHandler(request, session, handler);
            } catch (Throwable throwable) {
                final Jt808HandlerResult exceptionResult = this.invokeExceptionHandler(request, session, throwable, handler);
                this.handleResult(request, session, exceptionResult);
                return;
            }
            // 3. handle the result if necessary
            this.handleResult(request, session, handlerResult);
        } catch (Throwable dispatcherException) {
            this.invokeExceptionHandler(request, session, dispatcherException, null);
        }

        try {
            final Object handler = this.getHandler(request, session);
            final Jt808HandlerResult handlerResult = this.invokeHandler(request, session, handler);
            this.handleResult(request, session, handlerResult);
        } catch (Throwable dispatcherException) {
            this.invokeExceptionHandler(request, session, dispatcherException, null);
        }
    }

    protected void handleResult(Jt808Request request, Jt808Session session, Jt808HandlerResult result) {
        final Jt808HandlerResultHandler resultHandler = getResultHandler(result, request);
        try {
            resultHandler.handleResult(request, session, result);
        } catch (Throwable throwable) {
            this.invokeExceptionHandler(request, session, throwable, result.getHandler());
        }
    }

    protected Jt808HandlerResult invokeHandler(Jt808Request request, Jt808Session session, Object handler) {
        return this.getHandlerAdapter(request, handler).handle(request, session, handler);
    }

    private Jt808HandlerResult invokeExceptionHandler(Jt808Request request, Jt808Session session, Throwable throwable, Object handler) {
        try {
            return exceptionHandler.handleException(handler, ArgumentContext.of(throwable, request, session));
        } catch (Throwable e) {
            log.error("An exception occurred while invoke ExceptionHandler", throwable);
            return null;
        }
    }

    protected Object getHandler(Jt808Request request, Jt808Session session) {
        for (Jt808HandlerMapping handlerMapping : this.handlerMappings) {
            final Optional<Object> handler = handlerMapping.getHandler(request, session);
            if (handler.isPresent()) {
                return handler.get();
            }
        }
        throw new Jt808HandlerNotFoundException("No [" + Jt808HandlerMapping.class.getSimpleName() + "] found for msgType " + request.msgType(), request);
    }

    protected Jt808HandlerAdapter getHandlerAdapter(Jt808Request request, Object handler) {
        for (Jt808HandlerAdapter adapter : this.handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new Jt808HandlerAdapterNotFoundException(
                "No [" + Jt808HandlerAdapter.class.getSimpleName() + "] found for msgType " + request.msgType(), request);
    }

    private Jt808HandlerResultHandler getResultHandler(Jt808HandlerResult handlerResult, Jt808Request request) {
        for (Jt808HandlerResultHandler resultHandler : this.resultHandlers) {
            if (resultHandler.supports(handlerResult)) {
                return resultHandler;
            }
        }
        throw new Jt808HandlerResultHandlerNotFoundException(
                "No [" + Jt808HandlerResultHandler.class.getSimpleName() + "] found for msgType " + request.msgType(), request);
    }

    protected Jt808Session getSession(Jt808Request metadata) {
        final String terminalId = metadata.header().terminalId();
        Optional<Jt808Session> session = sessionManager.findByTerminalId(terminalId);
        if (session.isPresent()) {
            return session.get();
        }

        throw new JtSessionNotFoundException(terminalId);
    }

    protected <T extends OrderedComponent> List<T> sort(Collection<T> components) {
        return components.stream().sorted(Comparator.comparing(OrderedComponent::getOrder)).collect(Collectors.toUnmodifiableList());
    }
}
