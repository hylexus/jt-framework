package io.github.hylexus.jt808.handler.impl.exception;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.hylexus.jt808.handler.ExceptionHandler;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.session.Session;
import io.github.hylexus.jt808.support.OrderedComponent;
import org.springframework.core.ExceptionDepthComparator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 * Created At 2020-02-08 8:47 下午
 */
public class DelegateExceptionHandler implements ExceptionHandler {

    private final List<ExceptionHandler> exceptionHandlers = Lists.newArrayList();
    private final Map<Class<? extends Throwable>, ExceptionHandler> mappingCache = new ConcurrentHashMap<>();

    public DelegateExceptionHandler() {
    }

    public DelegateExceptionHandler addExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandlers.add(exceptionHandler);
        return this;
    }

    private ExceptionHandler getExceptionHandler(Class<? extends Throwable> targetExceptionClass, boolean useDefaultIfNoMatched) {
        ExceptionHandler handler = this.mappingCache.get(targetExceptionClass);
        if (handler != null) {
            return handler;
        }

        // [for-each] to match a closest exception type and then use it's handler
        handler = doMatchException(targetExceptionClass);
        if (handler == null && useDefaultIfNoMatched) {
            handler = new BuiltinDefaultExceptionHandler();
        }
        this.mappingCache.put(targetExceptionClass, handler);
        return handler;
    }

    @Override
    public Object handleException(RequestMsgMetadata metadata, Session session, Object handlerMethod, RequestMsgBody msg, Throwable throwable) throws Throwable {
        final Class<? extends Throwable> targetExceptionClass = throwable.getClass();

        ExceptionHandler exceptionHandler = this.getExceptionHandler(targetExceptionClass, false);
        if (exceptionHandler == null && throwable.getCause() != null) {
            exceptionHandler = this.getExceptionHandler(throwable.getCause().getClass(), true);
        }

        assert exceptionHandler != null : "No default ExceptionHandler found !!!";

        return exceptionHandler.handleException(metadata, session, handlerMethod, msg, throwable);
    }

    private ExceptionHandler doMatchException(Class<? extends Throwable> targetExceptionClass) {
        final Set<Class<? extends Throwable>> result = Sets.newHashSet();
        final Map<Class<? extends Throwable>, ExceptionHandler> tempCache = new ConcurrentHashMap<>();

        // TODO once only
        this.exceptionHandlers.sort(Comparator.comparing(OrderedComponent::getOrder));

        for (ExceptionHandler handler : this.exceptionHandlers) {
            for (Class<? extends Throwable> exceptionType : handler.getSupportedExceptionTypes()) {
                if (exceptionType.isAssignableFrom(targetExceptionClass)) {
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
