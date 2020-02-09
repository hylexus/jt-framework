package io.github.hylexus.jt808.handler;

import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.impl.ArgumentContext;
import io.github.hylexus.jt808.support.OrderedComponent;

import java.util.Collections;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2020-02-08 5:39 下午
 */
public interface ExceptionHandler extends OrderedComponent {

    default Set<Class<? extends Throwable>> getSupportedExceptionTypes() {
        return Collections.emptySet();
    }

    Object handleException(Object handler, ArgumentContext argumentContext) throws Throwable;

}
