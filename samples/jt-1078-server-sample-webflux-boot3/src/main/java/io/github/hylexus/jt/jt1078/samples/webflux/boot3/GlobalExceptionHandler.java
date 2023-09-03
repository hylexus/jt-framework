package io.github.hylexus.jt.jt1078.samples.webflux.boot3;

import io.github.hylexus.jt.core.model.value.DefaultRespCode;
import io.github.hylexus.jt.core.model.value.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public Object processThrowable(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        return Resp.failure(DefaultRespCode.SERVER_ERROR);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Object processWebExchangeBindException(WebExchangeBindException exception) {

        final BindingResult bindingResult = exception.getBindingResult();
        final String errorMsg = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return Resp.failure(DefaultRespCode.PARAMETER_ERROR, errorMsg);
    }


}
