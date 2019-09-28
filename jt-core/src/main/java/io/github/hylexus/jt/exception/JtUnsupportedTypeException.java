package io.github.hylexus.jt.exception;

/**
 * @author hylexus
 * Created At 2019-08-24 16:21
 */
public class JtUnsupportedTypeException extends AbstractJtException {
    public JtUnsupportedTypeException() {
    }

    public JtUnsupportedTypeException(String message) {
        super(message);
    }

    public JtUnsupportedTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JtUnsupportedTypeException(Throwable cause) {
        super(cause);
    }

    public JtUnsupportedTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
