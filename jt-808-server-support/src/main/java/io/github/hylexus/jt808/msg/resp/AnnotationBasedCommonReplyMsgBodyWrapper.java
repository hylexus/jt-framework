package io.github.hylexus.jt808.msg.resp;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.msg.RespMsgBody;
import lombok.Data;
import lombok.ToString;

/**
 * @author hylexus
 * Created At 2020-02-02 3:41 下午
 */
@Data
@ToString(of = {"msgType"})
public class AnnotationBasedCommonReplyMsgBodyWrapper implements RespMsgBody {

    private final MsgType msgType;
    private final byte[] bodyBytes;

    public AnnotationBasedCommonReplyMsgBodyWrapper(MsgType msgType, byte[] bodyBytes) {
        this.msgType = msgType;
        this.bodyBytes = bodyBytes;
    }

    @Override
    public byte[] toBytes() {
        return this.bodyBytes;
    }

    @Override
    public MsgType replyMsgType() {
        return this.msgType;
    }
}
