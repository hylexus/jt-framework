package io.github.hylexus.jt.dashboard.server.common.execption;

public abstract class BaseJtDashboardException extends RuntimeException {
    public BaseJtDashboardException() {
    }

    public BaseJtDashboardException(String message) {
        super(message);
    }

    public BaseJtDashboardException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseJtDashboardException(Throwable cause) {
        super(cause);
    }

    public BaseJtDashboardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
