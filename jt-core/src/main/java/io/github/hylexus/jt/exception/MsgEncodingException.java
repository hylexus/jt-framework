package io.github.hylexus.jt.exception;

public class MsgEncodingException extends AbstractJtException {
    public MsgEncodingException() {
    }

    public MsgEncodingException(String message) {
        super(message);
    }

    public MsgEncodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MsgEncodingException(Throwable cause) {
        super(cause);
    }

    public MsgEncodingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
