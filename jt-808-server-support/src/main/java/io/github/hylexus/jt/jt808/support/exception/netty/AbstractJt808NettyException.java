package io.github.hylexus.jt.jt808.support.exception.netty;

import io.github.hylexus.jt.exception.AbstractJtException;

public abstract class AbstractJt808NettyException extends AbstractJtException {
    public AbstractJt808NettyException() {
    }

    public AbstractJt808NettyException(String message) {
        super(message);
    }

    public AbstractJt808NettyException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractJt808NettyException(Throwable cause) {
        super(cause);
    }

    public AbstractJt808NettyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
