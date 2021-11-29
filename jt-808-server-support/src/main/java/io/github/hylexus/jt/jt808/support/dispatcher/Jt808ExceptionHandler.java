package io.github.hylexus.jt.jt808.support.dispatcher;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;

import java.util.Collections;
import java.util.Set;

/**
 * @author hylexus
 */
public interface Jt808ExceptionHandler extends OrderedComponent {

    default Set<Class<? extends Throwable>> getSupportedExceptionTypes() {
        return Collections.emptySet();
    }

    Jt808HandlerResult handleException(Object handler, ArgumentContext argumentContext) throws Throwable;

}
