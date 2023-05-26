package io.github.hylexus.jt808.samples.annotation;

import io.github.hylexus.jt.exception.JtSessionNotFoundException;
import io.github.hylexus.jt808.samples.annotation.model.vo.DefaultRespCode;
import io.github.hylexus.jt808.samples.annotation.model.vo.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static io.github.hylexus.jt808.samples.annotation.model.vo.DefaultRespCode.SESSION_NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public Object processThrowable(Throwable e) {
        log.error(e.getMessage(), e);
        return Resp.failure(DefaultRespCode.SERVER_ERROR);
    }

    @ExceptionHandler(JtSessionNotFoundException.class)
    public Object processJtSessionNotFoundException(JtSessionNotFoundException exception) {
        return Resp.failure(SESSION_NOT_FOUND, "No session found with terminalId [" + exception.getTerminalId() + "]");
    }
}
