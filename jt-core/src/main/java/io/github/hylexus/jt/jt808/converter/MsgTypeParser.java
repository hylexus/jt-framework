package io.github.hylexus.jt.jt808.converter;

import io.github.hylexus.jt.jt808.msg.MsgType;

import java.util.Optional;

/**
 * @author hylexus
 * createdAt 2019/1/24
 **/
public interface MsgTypeParser {

    Optional<MsgType> parseMsgType(int msgType);
}
