package io.github.hylexus.jt.exception;

/**
 * @author hylexus
 * Created At 2020-01-28 7:05 下午
 */
public class MsgEscapeException extends AbstractJtException {
    public MsgEscapeException() {
    }

    public MsgEscapeException(String message) {
        super(message);
    }

    public MsgEscapeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MsgEscapeException(Throwable cause) {
        super(cause);
    }

    public MsgEscapeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
