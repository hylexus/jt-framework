package io.github.hylexus.jt.jt808.spec.session;

/**
 * @author hylexus
 * Created At 2019-08-24 20:50
 */
public enum DefaultSessionCloseReason implements SessionCloseReason {
    IDLE_TIMEOUT("idle timeout"),
    CHANNEL_INACTIVE("channel inactive"),
    SERVER_EXCEPTION_OCCURRED("server error"),
    ;

    private final String reason;

    DefaultSessionCloseReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String reason() {
        return reason;
    }
}
