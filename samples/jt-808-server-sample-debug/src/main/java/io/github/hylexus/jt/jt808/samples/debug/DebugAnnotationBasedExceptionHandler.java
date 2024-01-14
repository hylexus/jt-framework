package io.github.hylexus.jt.jt808.samples.debug;

import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerAdvice;
import io.github.hylexus.jt.jt808.support.exception.Jt808HandlerNotFoundException;
import io.github.hylexus.jt.utils.FormatUtils;
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

    @Jt808ExceptionHandler(value = Jt808HandlerNotFoundException.class)
    public void processJt808HandlerNotFoundException(Jt808HandlerNotFoundException exception) {
        log.error("没有处理器来处理 msgId={}(0x{}) 的消息", exception.getRequest().header().msgId(), FormatUtils.toHexString(exception.getRequest().header().msgId()));
    }
}
