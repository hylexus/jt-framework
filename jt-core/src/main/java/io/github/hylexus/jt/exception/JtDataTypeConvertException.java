package io.github.hylexus.jt.exception;

/**
 * @author hylexus
 * Created At 2019-08-24 16:21
 */
public class JtDataTypeConvertException extends AbstractJtException {
    public JtDataTypeConvertException() {
    }

    public JtDataTypeConvertException(String message) {
        super(message);
    }

    public JtDataTypeConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public JtDataTypeConvertException(Throwable cause) {
        super(cause);
    }

    public JtDataTypeConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
