package io.github.hylexus.jt.demos.dashboard.boot3;

import io.github.hylexus.jt.demos.common.model.Resp;
import io.github.hylexus.jt.demos.common.exception.ReplayCodeException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReplayCodeException.class)
    public Resp<?> handleReplayCodeException(ReplayCodeException exception) {
        return exception.getResp();
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Object processWebExchangeBindException(WebExchangeBindException exception) {

        final BindingResult bindingResult = exception.getBindingResult();
        final String errorMsg = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return Resp.paramError(errorMsg);
    }
}
