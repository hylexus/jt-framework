package io.github.hylexus.jt.jt808.spec.impl;


import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hylexus
 **/
@Getter
public enum BuiltinJt808MsgType implements MsgType {

    CLIENT_LOG_OUT(0x0003, "终端注销"),
    CLIENT_HEART_BEAT(0x0002, "终端心跳"),

    CLIENT_COMMON_REPLY(0x0001, "终端通用应答"),
    SERVER_COMMON_REPLY(0x8001, "平台通用应答"),

    SERVER_RETRANSMISSION(0x8003, "服务器端补传分包请求"),
    CLIENT_RETRANSMISSION(0x0005, "终端补传分包请求"),

    CLIENT_REGISTER(0x0100, "终端注册"),
    CLIENT_REGISTER_REPLY(0x8100, "终端注册应答"),

    CLIENT_AUTH(0x0102, "终端鉴权"),
    CLIENT_LOCATION_INFO_UPLOAD(0x0200, "定位数据上报"),
    CLIENT_LOCATION_INFO_BATCH_UPLOAD(0x0704, "定位数据批量上报"),
    ;

    private static final Map<Integer, BuiltinJt808MsgType> mapping = new HashMap<>(BuiltinJt808MsgType.values().length);

    static {
        for (BuiltinJt808MsgType builtinMsgType : values()) {
            mapping.put(builtinMsgType.msgId, builtinMsgType);
        }
    }

    private final int msgId;
    private final String desc;

    BuiltinJt808MsgType(int msgId, String desc) {
        this.msgId = msgId;
        this.desc = desc;
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
