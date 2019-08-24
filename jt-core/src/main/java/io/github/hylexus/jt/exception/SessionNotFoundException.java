package io.github.hylexus.jt.exception;

/**
 * @author hylexus
 * createdAt 2019/2/2
 **/
public class SessionNotFoundException extends AbstractJtException {
    private String terminalId;

    public SessionNotFoundException(String terminalId) {
        this.terminalId = terminalId;
    }

    public SessionNotFoundException(String message, String terminalId) {
        super(message);
        this.terminalId = terminalId;
    }

    public SessionNotFoundException(String message, Throwable cause, String terminalId) {
        super(message, cause);
        this.terminalId = terminalId;
    }

    public SessionNotFoundException(Throwable cause, String terminalId) {
        super(cause);
        this.terminalId = terminalId;
    }

    public SessionNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String terminalId) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.terminalId = terminalId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public SessionNotFoundException setTerminalId(String terminalId) {
        this.terminalId = terminalId;
        return this;
    }
}
