package io.github.hylexus.jt.dashboard.server.common.execption;

public class JtEndpointResolveException extends BaseJtDashboardException {
    public JtEndpointResolveException() {
    }

    public JtEndpointResolveException(String message) {
        super(message);
    }

    public JtEndpointResolveException(String message, Throwable cause) {
        super(message, cause);
    }

    public JtEndpointResolveException(Throwable cause) {
        super(cause);
    }

    public JtEndpointResolveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
