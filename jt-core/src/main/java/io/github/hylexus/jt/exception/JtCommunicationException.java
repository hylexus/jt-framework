package io.github.hylexus.jt.exception;

/**
 * @author hylexus
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
