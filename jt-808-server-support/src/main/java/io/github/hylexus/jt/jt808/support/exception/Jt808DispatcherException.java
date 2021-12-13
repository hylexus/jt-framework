package io.github.hylexus.jt.jt808.support.exception;

import io.github.hylexus.jt.exception.AbstractJtException;
import io.github.hylexus.jt.jt808.spec.Jt808Request;

/**
 * @author hylexus
 */
public class Jt808DispatcherException extends AbstractJtException {

    protected final Jt808Request request;

    public Jt808DispatcherException(Jt808Request request) {
        this.request = request;
    }

    public Jt808DispatcherException(String message, Jt808Request request) {
        super(message);
        this.request = request;
    }

    public Jt808DispatcherException(String message, Throwable cause, Jt808Request request) {
        super(message, cause);
        this.request = request;
    }

    public Jt808DispatcherException(Throwable cause, Jt808Request request) {
        super(cause);
        this.request = request;
    }

    public Jt808DispatcherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Jt808Request request) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.request = request;
    }

    public Jt808Request getRequest() {
        return request;
    }
}
