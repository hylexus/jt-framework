package io.github.hylexus.jt.jt1078.support.extension.flatform.process.exception;

public class ProcessNotFoundException extends RuntimeException {
    public ProcessNotFoundException() {
    }

    public ProcessNotFoundException(String message) {
        super(message);
    }

    public ProcessNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessNotFoundException(Throwable cause) {
        super(cause);
    }

    public ProcessNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
