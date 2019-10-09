package io.github.hylexus.jt.data.msg;

import java.util.Optional;

/**
 * @author hylexus
 * createdAt 2019/1/24
 **/
public interface MsgType {
    int getMsgId();

    String getDesc();

    default Optional<MsgType> parseFromInt(int msgId) {
        throw new UnsupportedOperationException("this method should be override in sub class");
    }

    String toString();
}
