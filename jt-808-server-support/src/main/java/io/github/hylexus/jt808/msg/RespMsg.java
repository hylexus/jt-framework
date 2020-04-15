package io.github.hylexus.jt808.msg;

import io.github.hylexus.jt.data.msg.MsgType;

/**
 * @author hylexus
 * Created At 2020-03-25 6:47 下午
 */
public interface RespMsg {
    String getTerminalId();

    MsgType msgType();
}
