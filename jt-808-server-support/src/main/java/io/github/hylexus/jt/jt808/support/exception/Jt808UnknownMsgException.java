package io.github.hylexus.jt.jt808.support.exception;

import io.github.hylexus.jt.exception.AbstractJtException;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

@Getter
public class Jt808UnknownMsgException extends AbstractJtException {
    private final int msgId;
    private final ByteBuf payload;

    public Jt808UnknownMsgException(int msgId, ByteBuf payload) {
        this.msgId = msgId;
        this.payload = payload;
    }

    public Jt808UnknownMsgException(String message, int msgId, ByteBuf payload) {
        super(message);
        this.msgId = msgId;
        this.payload = payload;
    }

    public Jt808UnknownMsgException(String message, Throwable cause, int msgId, ByteBuf payload) {
        super(message, cause);
        this.msgId = msgId;
        this.payload = payload;
    }

    public Jt808UnknownMsgException(Throwable cause, int msgId, ByteBuf payload) {
        super(cause);
        this.msgId = msgId;
        this.payload = payload;
    }

    public Jt808UnknownMsgException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int msgId, ByteBuf payload) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.msgId = msgId;
        this.payload = payload;
    }
}
