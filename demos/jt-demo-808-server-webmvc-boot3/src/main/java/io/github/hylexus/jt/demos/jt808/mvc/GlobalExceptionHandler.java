package io.github.hylexus.jt.demos.jt808.mvc;

import io.github.hylexus.jt.core.model.value.DefaultRespCode;
import io.github.hylexus.jt.core.model.value.Resp;
import io.github.hylexus.jt.exception.JtSessionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

import static io.github.hylexus.jt.core.model.value.DefaultRespCode.SESSION_NOT_FOUND;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public Object processThrowable(Throwable e) {
        log.error(e.getMessage(), e);
        return Resp.failure(DefaultRespCode.SERVER_ERROR);
    }

    @ExceptionHandler(JtSessionNotFoundException.class)
    public Object processJtSessionNotFoundException(JtSessionNotFoundException exception) {
        return Resp.failure(SESSION_NOT_FOUND, "No session found with terminalId [" + exception.getTerminalId() + "]");
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Object processWebExchangeBindException(WebExchangeBindException exception) {

        final BindingResult bindingResult = exception.getBindingResult();
        final String errorMsg = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return Resp.parameterError(errorMsg);
    }
}
