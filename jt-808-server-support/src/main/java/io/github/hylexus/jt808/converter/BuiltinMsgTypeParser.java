package io.github.hylexus.jt808.converter;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt808.msg.BuiltinMsgType;
import io.github.hylexus.jt808.msg.MsgType;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-08-21 23:16
 */
@DebugOnly
public class BuiltinMsgTypeParser implements MsgTypeParser {

    @Override
    public Optional<MsgType> parseMsgType(int msgType) {
        return BuiltinMsgType.CLIENT_AUTH.parseFromInt(msgType);
    }

}
