package io.github.hylexus.jt.jt808.support;

import io.github.hylexus.jt.data.msg.MsgType;

import java.util.Optional;

/**
 * @author hylexus
 **/
public interface MsgTypeParser {

    Optional<MsgType> parseMsgType(int msgType);
}
