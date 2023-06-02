package io.github.hylexus.jt.jt1078.spec.exception;

public class Jt1078SessionDestroyException extends Jt1078SubscriberCloseException {
    public Jt1078SessionDestroyException() {
    }

    public Jt1078SessionDestroyException(String message) {
        super(message);
    }

    public Jt1078SessionDestroyException(String message, Throwable cause) {
        super(message, cause);
    }

    public Jt1078SessionDestroyException(Throwable cause) {
        super(cause);
    }

    public Jt1078SessionDestroyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
