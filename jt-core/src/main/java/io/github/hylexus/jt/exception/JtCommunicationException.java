package io.github.hylexus.jt.exception;

/**
 * @author hylexus
 * Created At 2019-08-24 16:21
 */
public class JtCommunicationException extends AbstractJtException {
    public JtCommunicationException() {
    }

    public JtCommunicationException(String message) {
        super(message);
    }

    public JtCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JtCommunicationException(Throwable cause) {
        super(cause);
    }

    public JtCommunicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
