package io.github.hylexus.jt808.converter;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-08-21 23:16
 */
@DebugOnly
public class BuiltinMsgTypeParser implements MsgTypeParser {

    @Override
    public Optional<MsgType> parseMsgType(int msgType) {
        return BuiltinJt808MsgType.CLIENT_AUTH.parseFromInt(msgType);
    }

}
