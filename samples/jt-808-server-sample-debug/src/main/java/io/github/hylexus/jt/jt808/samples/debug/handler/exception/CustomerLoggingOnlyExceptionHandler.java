package io.github.hylexus.jt.jt808.samples.debug.handler.exception;

import io.github.hylexus.jt.jt808.support.dispatcher.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * @author hylexus
 */
@Slf4j
@Component
public class CustomerLoggingOnlyExceptionHandler implements Jt808ExceptionHandler {

    private final Set<Class<? extends Throwable>> supportedExceptionTypes = Jdk8Adapter.setOf(Throwable.class);

    @Override
    public Set<Class<? extends Throwable>> getSupportedExceptionTypes() {
        return supportedExceptionTypes;
    }

    @Override
    @Nonnull
    public Jt808HandlerResult handleException(Object handler, ArgumentContext argumentContext) throws Throwable {
        final Throwable throwable = argumentContext.getThrowable();
        if (throwable != null) {
            log.error(throwable.getMessage(), throwable);
        } else {
            log.error("{}", argumentContext);
        }
        return Jt808HandlerResult.empty();
    }
}
