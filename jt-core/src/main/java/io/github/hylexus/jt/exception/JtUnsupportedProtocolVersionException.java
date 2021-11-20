package io.github.hylexus.jt.exception;

/**
 * @author hylexus
 * Created At 2019-08-24 16:21
 */
public class JtUnsupportedProtocolVersionException extends AbstractJtException {
    private final byte[] originalBytes;

    public JtUnsupportedProtocolVersionException(byte[] originalBytes) {
        this.originalBytes = originalBytes;
    }

    public JtUnsupportedProtocolVersionException(String message, byte[] originalBytes) {
        super(message);
        this.originalBytes = originalBytes;
    }

    public JtUnsupportedProtocolVersionException(String message, Throwable cause, byte[] originalBytes) {
        super(message, cause);
        this.originalBytes = originalBytes;
    }

    public JtUnsupportedProtocolVersionException(Throwable cause, byte[] originalBytes) {
        super(cause);
        this.originalBytes = originalBytes;
    }

    public JtUnsupportedProtocolVersionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, byte[] originalBytes) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.originalBytes = originalBytes;
    }
}