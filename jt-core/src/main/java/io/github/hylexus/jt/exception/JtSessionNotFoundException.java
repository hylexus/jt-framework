package io.github.hylexus.jt.exception;

/**
 * @author hylexus
 **/
public class JtSessionNotFoundException extends AbstractJtException {
    private String terminalId;

    public JtSessionNotFoundException(String terminalId) {
        this.terminalId = terminalId;
    }

    public JtSessionNotFoundException(String message, String terminalId) {
        super(message);
        this.terminalId = terminalId;
    }

    public JtSessionNotFoundException(String message, Throwable cause, String terminalId) {
        super(message, cause);
        this.terminalId = terminalId;
    }

    public JtSessionNotFoundException(Throwable cause, String terminalId) {
        super(cause);
        this.terminalId = terminalId;
    }

    public JtSessionNotFoundException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String terminalId) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.terminalId = terminalId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public JtSessionNotFoundException setTerminalId(String terminalId) {
        this.terminalId = terminalId;
        return this;
    }
}
