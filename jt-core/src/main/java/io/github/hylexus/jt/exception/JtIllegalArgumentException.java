package io.github.hylexus.jt.exception;

/**
 * Created At 2019-08-24 16:21
 *
 * @author hylexus
 */
public class JtIllegalArgumentException extends AbstractJtException {
    public JtIllegalArgumentException() {
    }

    public JtIllegalArgumentException(String message) {
        super(message);
    }

    public JtIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public JtIllegalArgumentException(Throwable cause) {
        super(cause);
    }

    public JtIllegalArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
