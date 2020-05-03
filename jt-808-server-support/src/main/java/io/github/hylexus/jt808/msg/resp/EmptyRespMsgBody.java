package io.github.hylexus.jt808.msg.resp;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.msg.RespMsgBody;

/**
 * @author hylexus
 * Created At 2020-03-11 10:15 下午
 */
public class EmptyRespMsgBody implements RespMsgBody {

    private final MsgType msgType;

    public EmptyRespMsgBody(MsgType msgType) {
        this.msgType = msgType;
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public MsgType replyMsgType() {
        return msgType;
    }
}
