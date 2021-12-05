package io.github.hylexus.jt.jt808.support.dispatcher;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

/**
 * @author hylexus
 */
public interface Jt808ExceptionHandler extends OrderedComponent {

    default Set<Class<? extends Throwable>> getSupportedExceptionTypes() {
        return Collections.emptySet();
    }

    @Nonnull
    Jt808HandlerResult handleException(Object handler, ArgumentContext argumentContext) throws Throwable;

    @Override
    default int getOrder() {
        return EXCEPTION_HANDLER_CUSTOMER_INTERFACE_BASED;
    }
}
