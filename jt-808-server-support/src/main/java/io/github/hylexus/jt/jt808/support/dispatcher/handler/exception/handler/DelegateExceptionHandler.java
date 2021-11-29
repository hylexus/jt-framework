package io.github.hylexus.jt.jt808.support.dispatcher.handler.exception.handler;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import org.springframework.core.ExceptionDepthComparator;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 */
@NotThreadSafe
public class DelegateExceptionHandler implements Jt808ExceptionHandler {

    private final List<Jt808ExceptionHandler> exceptionHandlers = Lists.newArrayList();
    private final Map<Class<? extends Throwable>, Jt808ExceptionHandler> mappingCache = new ConcurrentHashMap<>();
    private volatile boolean sorted = false;

    public DelegateExceptionHandler() {
    }

    public DelegateExceptionHandler addExceptionHandler(Jt808ExceptionHandler exceptionHandler) {
        this.exceptionHandlers.add(exceptionHandler);
        this.sorted = false;
        return this;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Jt808HandlerResult handleException(Object handler, ArgumentContext argumentContext) throws Throwable {
        final Throwable throwable = argumentContext.getThrowable();
        assert throwable != null;
        final Class<? extends Throwable> targetExceptionClass = throwable.getClass();

        Jt808ExceptionHandler exceptionHandler = this.getExceptionHandler(targetExceptionClass, false);
        if (exceptionHandler == null && throwable.getCause() != null) {
            exceptionHandler = this.getExceptionHandler(throwable.getCause().getClass(), true);
        }

        assert exceptionHandler != null : "No default ExceptionHandler found !!!";

        return exceptionHandler.handleException(handler, argumentContext);
    }

    private Jt808ExceptionHandler getExceptionHandler(Class<? extends Throwable> targetExceptionClass, boolean useDefaultIfNoMatched) {
        Jt808ExceptionHandler handler = this.mappingCache.get(targetExceptionClass);
        if (handler != null) {
            return handler;
        }

        // [for-each] to match a closest exception type and then use it's handler
        handler = doMatchException(targetExceptionClass);
        if (handler == null && useDefaultIfNoMatched) {
            handler = new BuiltinLoggingOnlyExceptionHandler();
        }
        this.mappingCache.put(targetExceptionClass, handler);
        return handler;
    }

    private Jt808ExceptionHandler doMatchException(Class<? extends Throwable> targetExceptionClass) {
        final Set<Class<? extends Throwable>> result = Sets.newHashSet();
        final Map<Class<? extends Throwable>, Jt808ExceptionHandler> tempCache = new ConcurrentHashMap<>();

        if (!this.sorted) {
            this.exceptionHandlers.sort(Comparator.comparing(OrderedComponent::getOrder));
            this.sorted = true;
        }

        for (Jt808ExceptionHandler handler : this.exceptionHandlers) {
            for (Class<? extends Throwable> exceptionType : handler.getSupportedExceptionTypes()) {
                if (exceptionType.isAssignableFrom(targetExceptionClass)) {
                    if (tempCache.containsKey(exceptionType)) {
                        continue;
                    }
                    result.add(exceptionType);
                    tempCache.put(exceptionType, handler);
                }
            }
        }

        if (result.isEmpty()) {
            return null;
        }

        final Class<? extends Throwable> matchedExceptionClass = result.stream().min(new ExceptionDepthComparator(targetExceptionClass)).get();
        return tempCache.get(matchedExceptionClass);
    }
}
