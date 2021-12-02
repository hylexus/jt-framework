package io.github.hylexus.jt.jt808.support.exception;

import io.github.hylexus.jt.exception.AbstractJtException;

/**
 * @author hylexus
 */
public class Jt808EncodeException extends AbstractJtException {
    public Jt808EncodeException() {
    }

    public Jt808EncodeException(String message) {
        super(message);
    }

    public Jt808EncodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public Jt808EncodeException(Throwable cause) {
        super(cause);
    }

    public Jt808EncodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
