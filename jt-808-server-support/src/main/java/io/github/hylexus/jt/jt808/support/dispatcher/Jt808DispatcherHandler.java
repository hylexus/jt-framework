package io.github.hylexus.jt.jt808.support.dispatcher;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListener;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListenerAware;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.exception.Jt808HandlerAdapterNotFoundException;
import io.github.hylexus.jt.jt808.support.exception.Jt808HandlerNotFoundException;
import io.github.hylexus.jt.jt808.support.exception.Jt808HandlerResultHandlerNotFoundException;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * @author hylexus
 */
@Slf4j
public class Jt808DispatcherHandler implements Jt808RequestLifecycleListenerAware {
    private final List<Jt808HandlerMapping> handlerMappings;
    private final List<Jt808HandlerAdapter> handlerAdapters;
    private final List<Jt808HandlerResultHandler> resultHandlers;
    private final Jt808ExceptionHandler exceptionHandler;
    private Jt808RequestLifecycleListener lifecycleListener;

    public Jt808DispatcherHandler(
            List<Jt808HandlerMapping> handlerMappings,
            List<Jt808HandlerAdapter> handlerAdapters,
            List<Jt808HandlerResultHandler> resultHandlers,
            Jt808ExceptionHandler exceptionHandler) {

        this.handlerMappings = this.sort(handlerMappings);
        this.handlerAdapters = this.sort(handlerAdapters);
        this.resultHandlers = this.sort(resultHandlers);
        this.exceptionHandler = exceptionHandler;
    }

    public void handleRequest(Jt808ServerExchange exchange) {
        if (!this.lifecycleListener.beforeDispatch(exchange)) {
            return;
        }
        // final Jt808Session session = getSession(request);
        Throwable dispatcherException = null;
        Jt808HandlerExecutionChain executionChain = null;
        Jt808HandlerResult handlerResult = null;
        try {
            try {
                // 1. Detect a handler that can handle the current request
                executionChain = this.getHandler(exchange);
                if (!this.lifecycleListener.beforeHandle(exchange, executionChain.getHandler())) {
                    return;
                }

                if (!executionChain.applyPreHandle(exchange)) {
                    return;
                }

                // 2. Invoke handler to handle current request
                handlerResult = this.invokeHandler(exchange, executionChain.getHandler());
                executionChain.applyPostHandle(exchange, handlerResult);

            } catch (Throwable throwable) {
                dispatcherException = throwable;
            }
            // 3. Handle the result from handler if necessary
            this.processHandlerResult(exchange, executionChain, handlerResult, dispatcherException);
        } catch (Throwable throwable) {
            dispatcherException = throwable;
        } finally {
            if (executionChain != null) {
                try {
                    executionChain.triggerAfterCompletion(exchange, dispatcherException);
                } catch (Throwable e) {
                    log.error("An error occurred while invoke triggerAfterCompletion()", e);
                }
            }
        }

    }

    private void processHandlerResult(
            Jt808ServerExchange exchange,
            @Nullable Jt808HandlerExecutionChain executionChain,
            @Nullable Jt808HandlerResult handlerResult,
            @Nullable Throwable dispatcherException) {

        if (dispatcherException != null) {
            try {
                final Jt808HandlerResult exceptionResult = this.processException(
                        exchange,
                        executionChain != null ? executionChain.getHandler() : null,
                        dispatcherException
                );
                this.handleResult(exchange, exceptionResult);
            } catch (Throwable throwable) {
                log.error("An error occurred while invoke ExceptionHandler", throwable);
            }
        } else {
            try {
                this.handleResult(exchange, handlerResult);
            } catch (Throwable exception) {
                try {
                    final Jt808HandlerResult exceptionResult = this.processException(exchange, null, exception);
                    this.handleResult(exchange, exceptionResult);
                } catch (Throwable e) {
                    log.error("An error occurred while invoke ExceptionHandler", e);
                }
            }
        }
    }

    private Jt808HandlerResult processException(
            Jt808ServerExchange exchange,
            @Nullable Object handler,
            Throwable dispatcherException) throws Throwable {
        // TODO exchange
        return exceptionHandler.handleException(handler, ArgumentContext.of(exchange, dispatcherException));
    }

    protected void handleResult(Jt808ServerExchange exchange, Jt808HandlerResult result) {
        if (!this.lifecycleListener.beforeEncode(exchange, result)) {
            return;
        }
        if (Jt808HandlerResult.isEmptyResult(result)) {
            return;
        }
        final Jt808HandlerResultHandler resultHandler = getResultHandler(result, exchange);
        resultHandler.handleResult(exchange, result);
    }

    protected Jt808HandlerResult invokeHandler(Jt808ServerExchange exchange, Object handler) throws Throwable {
        return this.getHandlerAdapter(exchange, handler).handle(exchange, handler);
    }

    protected Jt808HandlerExecutionChain getHandler(Jt808ServerExchange exchange) {
        for (Jt808HandlerMapping handlerMapping : this.handlerMappings) {
            final Optional<Jt808HandlerExecutionChain> handler = handlerMapping.getHandler(exchange);
            if (handler.isPresent()) {
                return handler.get();
            }
        }
        throw new Jt808HandlerNotFoundException(
                "No [" + Jt808HandlerMapping.class.getSimpleName() + "] found for msgType " + exchange.request().msgType(), exchange.request()
        );
    }

    protected Jt808HandlerAdapter getHandlerAdapter(Jt808ServerExchange exchange, Object handler) {
        for (Jt808HandlerAdapter adapter : this.handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new Jt808HandlerAdapterNotFoundException(
                "No [" + Jt808HandlerAdapter.class.getSimpleName() + "] found for msgType " + exchange.request().msgType(), exchange.request()
        );
    }

    private Jt808HandlerResultHandler getResultHandler(Jt808HandlerResult handlerResult, Jt808ServerExchange exchange) {
        for (Jt808HandlerResultHandler resultHandler : this.resultHandlers) {
            if (resultHandler.supports(handlerResult)) {
                return resultHandler;
            }
        }
        throw new Jt808HandlerResultHandlerNotFoundException(
                "No [" + Jt808HandlerResultHandler.class.getSimpleName() + "] found for msgType " + exchange.request().msgType(), exchange.request());
    }

    protected <T extends OrderedComponent> List<T> sort(Collection<T> components) {
        Jdk8Adapter.mark("Collectors.toUnmodifiableList()替换为collectingAndThen(toList(), Collections::unmodifiableList)");
        return components.stream().sorted(Comparator.comparing(OrderedComponent::getOrder)).collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    @Override
    public void setRequestLifecycleListener(Jt808RequestLifecycleListener requestLifecycleListener) {
        this.lifecycleListener = requestLifecycleListener;
    }
}
