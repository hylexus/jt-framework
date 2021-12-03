package io.github.hylexus.jt.jt808.support.exception;

import io.github.hylexus.jt.exception.AbstractJtException;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import lombok.Getter;

/**
 * @author hylexus
 */
@Getter
public class Jt808ArgumentResolveException extends AbstractJtException {
    private final ArgumentContext argumentContext;

    public Jt808ArgumentResolveException(ArgumentContext argumentContext) {
        this.argumentContext = argumentContext;
    }

    public Jt808ArgumentResolveException(String message, ArgumentContext argumentContext) {
        super(message);
        this.argumentContext = argumentContext;
    }

    public Jt808ArgumentResolveException(String message, Throwable cause, ArgumentContext argumentContext) {
        super(message, cause);
        this.argumentContext = argumentContext;
    }

    public Jt808ArgumentResolveException(Throwable cause, ArgumentContext argumentContext) {
        super(cause);
        this.argumentContext = argumentContext;
    }

    public Jt808ArgumentResolveException(
            String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace, ArgumentContext argumentContext) {

        super(message, cause, enableSuppression, writableStackTrace);
        this.argumentContext = argumentContext;
    }
}
