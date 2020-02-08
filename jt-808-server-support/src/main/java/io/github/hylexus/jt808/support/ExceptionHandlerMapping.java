package io.github.hylexus.jt808.support;

import com.google.common.collect.Sets;
import io.github.hylexus.jt808.support.exception.scan.ExceptionHandlerMethod;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ExceptionDepthComparator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 * Created At 2020-02-08 6:57 下午
 */
@Slf4j
public class ExceptionHandlerMapping {

    private Map<Class<? extends Throwable>, ExceptionHandlerMethod> methodMapping = new HashMap<>();

    private Map<Class<? extends Throwable>, ExceptionHandlerMethod> mappingCache = new ConcurrentHashMap<>();

    public ExceptionHandlerMapping registerExceptionHandler(
            Class<? extends Throwable> exceptionClass,
            ExceptionHandlerMethod handlerMethod, boolean forceOverride) {

        final ExceptionHandlerMethod oldHandler = this.methodMapping.get(exceptionClass);
        if (oldHandler == null) {
            this.methodMapping.put(exceptionClass, handlerMethod);
            return this;
        }

        if (forceOverride || oldHandler.shouldBeReplacedBy(handlerMethod)) {
            log.warn("duplicate ExceptionHandlerMethod for exception class : {}, the ExceptionHandlerMethod {} was replaced by {}", exceptionClass, oldHandler.getClass(),
                    handlerMethod.getClass());
            this.methodMapping.put(exceptionClass, handlerMethod);
        }

        return this;
    }

    public ExceptionHandlerMapping registerExceptionHandler(ExceptionHandlerMethod handlerMethod, boolean forceOverride) {
        for (Class<? extends Throwable> exceptionType : handlerMethod.getSupportedExceptionTypes()) {
            this.registerExceptionHandler(exceptionType, handlerMethod, forceOverride);
        }
        return this;
    }

    public ExceptionHandlerMapping registerExceptionHandler(ExceptionHandlerMethod handlerMethod) {
        return this.registerExceptionHandler(handlerMethod, false);
    }

    @NonNull
    public ExceptionHandlerMethod resolveExceptionHandler(Throwable throwable) {
        final Class<? extends Throwable> targetExceptionClass = throwable.getClass();
        ExceptionHandlerMethod handlerMethod = resolveExceptionHandlerByType(targetExceptionClass);

        if (handlerMethod == null && throwable.getCause() != null) {
            handlerMethod = resolveExceptionHandlerByType(throwable.getCause().getClass());
        }

        if (handlerMethod == null) {
            // TODO default implementation
            return null;
        }

        return handlerMethod;
    }

    private ExceptionHandlerMethod resolveExceptionHandlerByType(Class<? extends Throwable> targetExceptionClass) {
        ExceptionHandlerMethod handlerMethod = this.mappingCache.get(targetExceptionClass);
        if (handlerMethod != null) {
            return handlerMethod;
        }

        // for-each
        handlerMethod = doMatchException(targetExceptionClass);

        // Found an ExceptionHandler or else null-value : <SomeException.class,null>
        this.mappingCache.put(targetExceptionClass, handlerMethod);
        return handlerMethod;
    }

    private ExceptionHandlerMethod doMatchException(Class<? extends Throwable> targetExceptionClass) {
        final Set<Class<? extends Throwable>> result = Sets.newHashSet();
        for (Class<? extends Throwable> cls : this.methodMapping.keySet()) {
            if (cls.isAssignableFrom(targetExceptionClass)) {
                result.add(cls);
            }
        }

        if (result.isEmpty()) {
            return null;
        }

        final Class<? extends Throwable> matchedExceptionClass = result.stream().min(new ExceptionDepthComparator(targetExceptionClass)).get();
        return this.methodMapping.get(matchedExceptionClass);
    }

}
