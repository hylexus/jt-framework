package io.github.hylexus.jt.jt808.spec;

import java.util.Optional;

/**
 * @author hylexus
 **/
public interface MsgType {

    /**
     * @return 消息ID(消息头中的前两个字节)
     */
    int getMsgId();

    /**
     * @param msgId 消息ID
     * @return 转换之后的 {@link MsgType} 实例
     */
    Optional<MsgType> parseFromInt(int msgId);

    String getDesc();

    String toString();
}
