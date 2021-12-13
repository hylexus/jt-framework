package io.github.hylexus.jt.jt808.support.exception;

import io.github.hylexus.jt.jt808.spec.Jt808Request;

/**
 * @author hylexus
 */
public class Jt808EmptyResultException extends Jt808DispatcherException {

    public Jt808EmptyResultException(Jt808Request request) {
        super(request);
    }

    public Jt808EmptyResultException(String message, Jt808Request request) {
        super(message, request);
    }

    public Jt808EmptyResultException(String message, Throwable cause, Jt808Request request) {
        super(message, cause, request);
    }

    public Jt808EmptyResultException(Throwable cause, Jt808Request request) {
        super(cause, request);
    }

    public Jt808EmptyResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Jt808Request request) {
        super(message, cause, enableSuppression, writableStackTrace, request);
    }
}
