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

import javax.annotation.Nullable;
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
        Throwable dispatcherException = null;
        Jt808HandlerExecutionChain executionChain = null;
        Jt808HandlerResult handlerResult = null;
        try {
            try {
                // 1. Detect a handler that can handle the current request
                executionChain = this.getHandler(request, session);
                if (!executionChain.applyPreHandle(request, session)) {
                    return;
                }

                // 2. Invoke handler to handle current request
                handlerResult = this.invokeHandler(request, session, executionChain.getHandler());
                executionChain.applyPostHandle(request, session, handlerResult);

            } catch (Throwable throwable) {
                dispatcherException = throwable;
            }
            // 3. Handle the result from handler if necessary
            this.processHandlerResult(request, session, executionChain, handlerResult, dispatcherException);
        } catch (Throwable throwable) {
            dispatcherException = throwable;
        } finally {
            if (executionChain != null) {
                try {
                    executionChain.triggerAfterCompletion(request, session, dispatcherException);
                } catch (Throwable e) {
                    log.error("An error occurred while invoke triggerAfterCompletion()", e);
                }
            }
        }

    }

    private void processHandlerResult(
            Jt808Request request, Jt808Session session,
            @Nullable Jt808HandlerExecutionChain executionChain,
            @Nullable Jt808HandlerResult handlerResult,
            @Nullable Throwable dispatcherException) {

        if (dispatcherException != null) {
            try {
                final Jt808HandlerResult exceptionResult = this.processException(
                        request,
                        session,
                        executionChain != null ? executionChain.getHandler() : null,
                        dispatcherException
                );
                this.handleResult(request, session, exceptionResult);
            } catch (Throwable throwable) {
                log.error("An error occurred while invoke ExceptionHandler", throwable);
            }
        } else {
            try {
                this.handleResult(request, session, handlerResult);
            } catch (Throwable exception) {
                try {
                    final Jt808HandlerResult exceptionResult = this.processException(request, session, null, exception);
                    this.handleResult(request, session, exceptionResult);
                } catch (Throwable e) {
                    log.error("An error occurred while invoke ExceptionHandler", e);
                }
            }
        }
    }

    private Jt808HandlerResult processException(
            Jt808Request request, Jt808Session session,
            @Nullable Object handler,
            Throwable dispatcherException) throws Throwable {
        return exceptionHandler.handleException(handler, ArgumentContext.of(request, session, dispatcherException));
    }

    protected void handleResult(Jt808Request request, Jt808Session session, Jt808HandlerResult result) {
        if (Jt808HandlerResult.isEmptyResult(result)) {
            return;
        }
        final Jt808HandlerResultHandler resultHandler = getResultHandler(result, request);
        resultHandler.handleResult(request, session, result);
    }

    protected Jt808HandlerResult invokeHandler(Jt808Request request, Jt808Session session, Object handler) throws Throwable {
        return this.getHandlerAdapter(request, handler).handle(request, session, handler);
    }

    protected Jt808HandlerExecutionChain getHandler(Jt808Request request, Jt808Session session) {
        for (Jt808HandlerMapping handlerMapping : this.handlerMappings) {
            final Optional<Jt808HandlerExecutionChain> handler = handlerMapping.getHandler(request, session);
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
