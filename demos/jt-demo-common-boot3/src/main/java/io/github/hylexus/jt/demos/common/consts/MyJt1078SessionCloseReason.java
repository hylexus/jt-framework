package io.github.hylexus.jt.demos.common.consts;

import io.github.hylexus.jt.jt1078.spec.Jt1078SessionCloseReason;

public enum MyJt1078SessionCloseReason implements Jt1078SessionCloseReason {
    CLOSED_BY_WEB_SOCKET("WebSocket"),
    CLOSED_BY_HTTP("Http"),
    ;

    private final String reason;

    MyJt1078SessionCloseReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String reason() {
        return reason;
    }
}
