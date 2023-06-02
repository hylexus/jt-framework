package io.github.hylexus.jt.jt1078.spec.exception;

public abstract class Jt1078SubscriberCloseException extends RuntimeException {
    public Jt1078SubscriberCloseException() {
    }

    public Jt1078SubscriberCloseException(String message) {
        super(message);
    }

    public Jt1078SubscriberCloseException(String message, Throwable cause) {
        super(message, cause);
    }

    public Jt1078SubscriberCloseException(Throwable cause) {
        super(cause);
    }

    public Jt1078SubscriberCloseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
