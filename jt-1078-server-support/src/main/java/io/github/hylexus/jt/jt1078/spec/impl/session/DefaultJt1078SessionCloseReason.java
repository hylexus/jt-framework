package io.github.hylexus.jt.jt1078.spec.impl.session;

import io.github.hylexus.jt.jt1078.spec.Jt1078SessionCloseReason;

public enum DefaultJt1078SessionCloseReason implements Jt1078SessionCloseReason {
    IDLE_TIMEOUT("idle timeout"),
    CHANNEL_INACTIVE("channel inactive"),
    SERVER_EXCEPTION_OCCURRED("server error"),
    ;

    private final String reason;

    DefaultJt1078SessionCloseReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String reason() {
        return reason;
    }
}
