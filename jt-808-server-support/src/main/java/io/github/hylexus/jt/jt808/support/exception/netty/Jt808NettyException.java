package io.github.hylexus.jt.jt808.support.exception.netty;

public class Jt808NettyException extends AbstractJt808NettyException {
    public Jt808NettyException() {
    }

    public Jt808NettyException(String message) {
        super(message);
    }

    public Jt808NettyException(String message, Throwable cause) {
        super(message, cause);
    }

    public Jt808NettyException(Throwable cause) {
        super(cause);
    }

    public Jt808NettyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
