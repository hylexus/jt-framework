package io.github.hylexus.jt.jt808.spec;

import java.util.Optional;

/**
 * @author hylexus
 **/
public interface MsgTypeParser {

    Optional<MsgType> parseMsgType(int msgType);
}
