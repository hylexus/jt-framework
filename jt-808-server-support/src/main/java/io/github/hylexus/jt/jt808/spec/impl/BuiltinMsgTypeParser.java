package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.spec.MsgTypeParser;

import java.util.Optional;

/**
 * @author hylexus
 */
@DebugOnly
public class BuiltinMsgTypeParser implements MsgTypeParser {

    @Override
    public Optional<MsgType> parseMsgType(int msgType) {
        return BuiltinJt808MsgType.CLIENT_AUTH.parseFromInt(msgType);
    }

}
