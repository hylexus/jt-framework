package io.github.hylexus.jt.jt1078.support.exception;

import io.github.hylexus.jt.exception.AbstractJtException;

/**
 * @author hylexus
 */
public abstract class AbstractJt1078Exception extends AbstractJtException {
    public AbstractJt1078Exception() {
    }

    public AbstractJt1078Exception(String message) {
        super(message);
    }

    public AbstractJt1078Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractJt1078Exception(Throwable cause) {
        super(cause);
    }

    public AbstractJt1078Exception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
