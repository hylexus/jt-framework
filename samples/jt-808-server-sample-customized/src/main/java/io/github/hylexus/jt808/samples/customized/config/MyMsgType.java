package io.github.hylexus.jt808.samples.customized.config;

import io.github.hylexus.jt.jt808.spec.MsgType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hylexus
 */
public enum MyMsgType implements MsgType {
    CLIENT_REGISTER(0x0100, "终端注册"),
    CLIENT_AUTH(0x0102, "终端鉴权"),
    ;

    private final int msgId;
    private final String desc;

    MyMsgType(int msgId, String desc) {
        this.msgId = msgId;
        this.desc = desc;
    }

    private static final Map<Integer, MsgType> mappings = new HashMap<>(values().length);

    static {
        for (MyMsgType value : values()) {
            mappings.put(value.msgId, value);
        }
    }

    @Override
    public Optional<MsgType> parseFromInt(int msgId) {
        return Optional.ofNullable(mappings.get(msgId));
    }

    @Override
    public int getMsgId() {
        return msgId;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
