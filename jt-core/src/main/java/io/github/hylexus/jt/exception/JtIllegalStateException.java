package io.github.hylexus.jt.exception;

/**
 * @author hylexus
 * Created At 2019-08-24 16:21
 */
public class JtIllegalStateException extends AbstractJtException {
    public JtIllegalStateException() {
    }

    public JtIllegalStateException(String message) {
        super(message);
    }

    public JtIllegalStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public JtIllegalStateException(Throwable cause) {
        super(cause);
    }

    public JtIllegalStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
