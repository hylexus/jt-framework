package io.github.hylexus.jt.demos.dashboard.webmvc.boot3;

import io.github.hylexus.jt.core.model.value.DefaultRespCode;
import io.github.hylexus.jt.core.model.value.Resp;
import io.github.hylexus.jt.dashboard.server.controller.BuiltinDashboardExceptionHandler;
import io.github.hylexus.jt.exception.ReplayCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends BuiltinDashboardExceptionHandler {

    @Override
    @ExceptionHandler(Throwable.class)
    public Resp<Object> processThrowable(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        return Resp.failure(DefaultRespCode.SERVER_ERROR);
    }

    @Override
    @ExceptionHandler(ReplayCodeException.class)
    public Resp<?> handleReplayCodeException(ReplayCodeException exception) {
        return super.handleReplayCodeException(exception);
    }

    @Override
    @ExceptionHandler(WebExchangeBindException.class)
    public Resp<Object> processWebExchangeBindException(WebExchangeBindException exception) {
        return super.processWebExchangeBindException(exception);
    }
}
