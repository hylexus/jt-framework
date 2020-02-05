package io.github.hylexus.jt808.msg.resp;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.handler.impl.reflection.CustomReflectionBasedRequestMsgHandler;
import io.github.hylexus.jt808.msg.RespMsgBody;

/**
 * 不回复消息给客户端
 * <p>
 * 该类型仅仅适用于 {@link CustomReflectionBasedRequestMsgHandler}。
 *
 * @author hylexus
 * Created At 2020-02-02 3:01 下午
 */
public enum VoidRespMsgBody implements RespMsgBody {

    NO_DATA_WILL_BE_SENT_TO_CLIENT;

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public MsgType replyMsgType() {
        return null;
    }
}
