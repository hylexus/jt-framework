package io.github.hylexus.jt808.msg;

import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;

/**
 * @author hylexus
 * createdAt 2018/12/29
 **/
public interface RespMsgBody {
    byte SUCCESS = 0;
    byte FAILURE = 1;
    byte AUTH_CODE_ERROR = 2;

    byte[] toBytes();

    default MsgType replyMsgType() {
        return BuiltinJt808MsgType.SERVER_COMMON_REPLY;
    }
}
