package io.github.hylexus.jt808.msg;


import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hylexus
 * createdAt 2019/1/24
 **/
@Getter
public enum BuiltinMsgType implements MsgType {

    CLIENT_LOG_OUT(0x0003, "终端注销"),
    CLIENT_HEART_BEAT(0x0002, "终端心跳"),

    CLIENT_COMMON_REPLY(0x0001, "终端通用应答"),
    SERVER_COMMON_REPLY(0x8001, "平台通用应答"),

    CLIENT_REGISTER(0x0100, "终端注册"),
    CLIENT_REGISTER_REPLY(0x8100, "平台注册应答"),
    CLIENT_AUTH(0x0102, "终端鉴权"),
    ;

    BuiltinMsgType(int msgId, String desc) {
        this.msgId = msgId;
        this.desc = desc;
    }

    private int msgId;
    private String desc;

    private static Map<Integer, BuiltinMsgType> mapping = new HashMap<>(BuiltinMsgType.values().length);

    static {
        for (BuiltinMsgType builtinMsgType : values()) {
            mapping.put(builtinMsgType.msgId, builtinMsgType);
        }
    }

    @Override
    public Optional<MsgType> parseFromInt(int msgId) {
        return Optional.ofNullable(mapping.get(msgId));
    }

    @Override
    public String toString() {
        return "BuiltInMsgType{"
                + "msgId=" + msgId
                + "(" + HexStringUtils.int2HexString(msgId, 4, true) + "), desc='" + desc + '\''
                + '}';
    }
}
