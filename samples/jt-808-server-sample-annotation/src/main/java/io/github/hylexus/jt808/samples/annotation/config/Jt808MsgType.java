package io.github.hylexus.jt808.samples.annotation.config;

import io.github.hylexus.jt.data.msg.MsgType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2020-02-02 1:24 下午
 */
@Getter
public enum Jt808MsgType implements MsgType {
    CLIENT_AUTH(0x0102, "终端鉴权"),
    CLIENT_LOCATION_INFO_UPLOAD(0x0200, "位置上报"),
    REQ_QUERY_LOCK_PARAM_REPLY(0x0104, "查询锁参数应答"),
    // bug-fix --> https://github.com/hylexus/jt-framework/issues/8
    CLIENT_DATA_UP_TRANSPARENT(0x0900, "数据上行透传"),
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
