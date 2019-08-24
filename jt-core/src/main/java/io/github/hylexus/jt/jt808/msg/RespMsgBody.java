package io.github.hylexus.jt.jt808.msg;

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
        return BuiltinMsgType.RESP_SERVER_COMMON_REPLY;
    }
}
