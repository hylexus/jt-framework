package io.github.hylexus.jt.dashboard.server.common.execption;

public class JtInstanceNotFoundException extends BaseJtDashboardException {
    public JtInstanceNotFoundException() {
    }

    public JtInstanceNotFoundException(String message) {
        super(message);
    }

    public JtInstanceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public JtInstanceNotFoundException(Throwable cause) {
        super(cause);
    }

    public JtInstanceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
