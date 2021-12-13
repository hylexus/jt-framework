package io.github.hylexus.jt.jt808.support.exception;

import io.github.hylexus.jt.jt808.spec.Jt808Request;

/**
 * @author hylexus
 */
public class Jt808HandlerNotFoundException extends Jt808DispatcherException {

    public Jt808HandlerNotFoundException(Jt808Request request) {
        super(request);
    }

    public Jt808HandlerNotFoundException(String message, Jt808Request request) {
        super(message, request);
    }

    public Jt808HandlerNotFoundException(String message, Throwable cause, Jt808Request request) {
        super(message, cause, request);
    }

    public Jt808HandlerNotFoundException(Throwable cause, Jt808Request request) {
        super(cause, request);
    }

    public Jt808HandlerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Jt808Request request) {
        super(message, cause, enableSuppression, writableStackTrace, request);
    }
}
