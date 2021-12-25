package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt.jt808.spec.Jt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.MsgType;

import java.util.Optional;

/**
 * @author hylexus
 */
@DebugOnly
public class BuiltinJt808MsgTypeParser implements Jt808MsgTypeParser {

    @Override
    public Optional<MsgType> parseMsgType(int msgId) {
        return BuiltinJt808MsgType.CLIENT_AUTH.parseFromInt(msgId);
    }

}
