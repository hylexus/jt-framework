package io.github.hylexus.jt808.samples.customized.config;

import io.github.hylexus.jt.data.msg.MsgType;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-09-22 3:37 下午
 */
@Getter
@ToString
public enum Jt808MsgType implements MsgType {
    CLIENT_COMMON_REPLY(0x0001, "终端通用应答"),
    CLIENT_AUTH(0x0102, "终端鉴权"),
    CLIENT_LOCATION_INFO_UPLOAD(0x0200, "位置上报"),

    RESP_TERMINAL_PARAM_SETTINGS(0x8103, "设置终端参数"),
    RESP_QUERY_TERMINAL_PROPERTIES(0x8107, "查询终端属性(EMPTY)"),
    CLIENT_QUERY_TERMINAL_PROPERTIES_REPLY(0x0107, "查询终端属性应答"),
    ;
    private int msgId;
    private String desc;

    Jt808MsgType(int msgId, String desc) {
        this.msgId = msgId;
        this.desc = desc;
    }

    private static final Map<Integer, Jt808MsgType> mapping = new HashMap<>(values().length);

    static {
        for (Jt808MsgType type : values()) {
            mapping.put(type.msgId, type);
        }
    }

    @Override
    public Optional<MsgType> parseFromInt(int msgId) {
        return Optional.ofNullable(mapping.get(msgId));
    }
}
