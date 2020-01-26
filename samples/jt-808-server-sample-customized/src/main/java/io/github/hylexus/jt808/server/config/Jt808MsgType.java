package io.github.hylexus.jt808.server.config;

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
    CLIENT_AUTH(0x0102, "终端鉴权"),
    CLIENT_LOCATION_INFO_UPLOAD(0x0200, "位置上报"),
    ;
    private int msgId;
    private String desc;

    Jt808MsgType(int msgId, String desc) {
        this.msgId = msgId;
        this.desc = desc;
    }

    private static Map<Integer, Jt808MsgType> mapping = new HashMap<>(values().length);

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
