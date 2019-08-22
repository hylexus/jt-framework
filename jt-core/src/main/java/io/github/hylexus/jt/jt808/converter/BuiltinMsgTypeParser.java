package io.github.hylexus.jt.jt808.converter;

import io.github.hylexus.jt.jt808.msg.BuiltinMsgType;
import io.github.hylexus.jt.jt808.msg.MsgType;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-08-21 23:16
 */
public class BuiltinMsgTypeParser implements MsgTypeParser {

    @Override
    public Optional<MsgType> parseMsgType(int msgType) {
        return BuiltinMsgType.REQ_CLIENT_AUTH.parseFromInt(msgType);
    }

}
