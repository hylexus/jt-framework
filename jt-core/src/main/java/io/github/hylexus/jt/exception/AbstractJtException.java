package io.github.hylexus.jt.exception;

/**
 * @author hylexus
 * Created At 2019-08-24 16:20
 */
public abstract class AbstractJtException extends RuntimeException {

    public AbstractJtException() {
    }

    public AbstractJtException(String message) {
        super(message);
    }

    public AbstractJtException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractJtException(Throwable cause) {
        super(cause);
    }

    public AbstractJtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
