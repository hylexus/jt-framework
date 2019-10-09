package io.github.hylexus.jt808.msg;

import io.github.hylexus.jt.data.msg.MsgType;

/**
 * @author hylexus
 * Created At 2019-08-20 22:01
 */
public interface RequestMsg {
    RequestMsgHeader getHeader();

    byte[] getBodyBytes();

    byte getCheckSum();

    MsgType getMsgType();

}
