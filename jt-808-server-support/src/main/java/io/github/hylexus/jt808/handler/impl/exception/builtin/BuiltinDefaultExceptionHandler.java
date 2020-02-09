package io.github.hylexus.jt808.handler.impl.exception.builtin;

import io.github.hylexus.jt.annotation.msg.handler.Jt808ExceptionHandler;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandlerAdvice;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * Created At 2020-02-09 3:11 下午
 */
@Slf4j
@Jt808RequestMsgHandlerAdvice
public class BuiltinDefaultExceptionHandler {

    @Jt808ExceptionHandler({Throwable.class})
    public void processThrowable(Throwable throwable) {
        log.info("BuiltinDefaultExceptionHandler :", throwable);
    }
}
