package io.github.hylexus.jt.jt808.msg;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * createdAt 2019/1/24
 **/
@Data
@Accessors(chain = true)
public class AbstractRequestMsg implements RequestMsg {
    protected RequestMsgHeader header;
    protected byte[] bodyBytes;
    protected byte checkSum;
    protected MsgType msgType;

    public AbstractRequestMsg() {
    }

    public AbstractRequestMsg(AbstractRequestMsg another) {
        fromAnother(another);
    }

    public AbstractRequestMsg fromAnother(RequestMsg other) {
        this.header = other.getHeader();
        this.bodyBytes = other.getBodyBytes();
        this.checkSum = other.getCheckSum();
        this.msgType = other.getMsgType();
        return this;
    }
}
