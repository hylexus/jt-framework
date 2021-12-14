package io.github.hylexus.jt.jt808.samples.debug;

import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author hylexus
 */
@Slf4j
@Component
@Jt808RequestHandlerAdvice
public class DebugAnnotationBasedExceptionHandler {

    @Jt808ExceptionHandler(value = Throwable.class)
    public void processThrowable(Throwable throwable) {
        log.error("############################", throwable);
    }

    @Jt808ExceptionHandler(value = IllegalStateException.class)
    public void processIllegalStateException(IllegalStateException illegalStateException) {
        log.error("############################", illegalStateException);
    }
}
