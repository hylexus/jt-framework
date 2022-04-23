package io.github.hylexus.jt.jt1078.support.exception;

/**
 * @author hylexus
 */
public class Jt1078SessionNotFoundException extends AbstractJt1078Exception {
    private final String sim;

    public Jt1078SessionNotFoundException(String sim) {
        this.sim = sim;
    }

    public Jt1078SessionNotFoundException(String message, String sim) {
        super(message);
        this.sim = sim;
    }

    public Jt1078SessionNotFoundException(String message, Throwable cause, String sim) {
        super(message, cause);
        this.sim = sim;
    }

    public Jt1078SessionNotFoundException(Throwable cause, String sim) {
        super(cause);
        this.sim = sim;
    }

    public Jt1078SessionNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String sim) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.sim = sim;
    }

    public String getSim() {
        return sim;
    }
}
