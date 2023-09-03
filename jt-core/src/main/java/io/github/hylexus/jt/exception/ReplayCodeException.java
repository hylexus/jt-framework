package io.github.hylexus.jt.exception;

import io.github.hylexus.jt.core.model.value.Resp;
import lombok.Getter;

@Getter
public class ReplayCodeException extends RuntimeException {
    private final Resp<?> resp;

    public ReplayCodeException(Resp<?> resp) {
        this.resp = resp;
    }

    public ReplayCodeException(String message, Resp<?> resp) {
        super(message);
        this.resp = resp;
    }

    public ReplayCodeException(String message, Throwable cause, Resp<?> resp) {
        super(message, cause);
        this.resp = resp;
    }

    public ReplayCodeException(Throwable cause, Resp<?> resp) {
        super(cause);
        this.resp = resp;
    }

    public ReplayCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Resp<?> resp) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.resp = resp;
    }

}
