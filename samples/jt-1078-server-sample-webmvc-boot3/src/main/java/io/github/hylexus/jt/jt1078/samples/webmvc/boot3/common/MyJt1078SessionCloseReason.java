package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.common;

import io.github.hylexus.jt.jt1078.spec.Jt1078SessionCloseReason;

public enum MyJt1078SessionCloseReason implements Jt1078SessionCloseReason {
    CLOSED_BY_WEB_SOCKET("WebSocket"),
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
