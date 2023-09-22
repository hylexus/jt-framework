package io.github.hylexus.jt.demos.dashboard.webflux.boot3;

import io.github.hylexus.jt.core.model.value.DefaultRespCode;
import io.github.hylexus.jt.core.model.value.Resp;
import io.github.hylexus.jt.exception.ReplayCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public Resp<Object> processThrowable(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        return Resp.failure(DefaultRespCode.SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(ReplayCodeException.class)
    public Resp<?> handleReplayCodeException(ReplayCodeException exception) {
        return exception.getResp();
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Resp<Object> processWebExchangeBindException(WebExchangeBindException exception) {

        final BindingResult bindingResult = exception.getBindingResult();
        final String errorMsg = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return Resp.parameterError(errorMsg);
    }
}
