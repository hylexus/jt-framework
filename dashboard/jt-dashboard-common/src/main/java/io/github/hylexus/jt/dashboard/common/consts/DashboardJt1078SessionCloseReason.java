package io.github.hylexus.jt.dashboard.common.consts;

import io.github.hylexus.jt.jt1078.spec.Jt1078SessionCloseReason;

public enum DashboardJt1078SessionCloseReason implements Jt1078SessionCloseReason {
    CLOSED_BY_WEB_SOCKET("WebSocket"),
    CLOSED_BY_HTTP("Http"),
    CLOSED_BY_DASHBOARD_HTTP_API("DashboardHttpApi"),
    ;

    private final String reason;

    DashboardJt1078SessionCloseReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String reason() {
        return reason;
    }
}
