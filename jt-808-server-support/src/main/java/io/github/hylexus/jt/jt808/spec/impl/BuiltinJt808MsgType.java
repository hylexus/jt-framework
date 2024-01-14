package io.github.hylexus.jt.jt808.spec.impl;


import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hylexus
 * @author puruidong
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

    CLIENT_PARAM_QUERY_RESULT(0x0104, "终端参数查询结果"),

    CLIENT_MULTI_MEDIA_MSG_UPLOAD(0x0800, "多媒体事件信息上传"),

    SERVER_SET_CIRCLE_AREA(0x8600, "设置圆形区域"),
    SERVER_DELETE_CIRCLE_AREA(0x8601, "删除圆形区域"),

    SERVER_SET_RECTANGLE_AREA(0x8602, "设置矩形区域"),
    SERVER_DELETE_RECTANGLE_AREA(0x8603, "删除矩形区域"),

    SERVER_SET_POLYGON_AREA(0x8604, "设置多边形区域"),
    SERVER_DELETE_POLYGON_AREA(0x8605, "删除多边形区域"),

    SERVER_TEXT_MESSAGE_DISTRIBUTION(0x8300, "文本信息下发"),
    SERVER_SET_TERMINAL_PARAM(0x8103, "设置终端参数"),

    // 1078
    SERVER_RTM_REQUEST(0x9101, "实时音视频传输请求"),
    SERVER_RTM_CONTROL(0x9102, "实时音视频传输控制"),

    // 苏标
    CLIENT_MSG_1210(0x1210, "报警附件信息消息"),
    CLIENT_MSG_1211(0x1211, "文件信息上传"),
    CLIENT_MSG_1212(0x1212, "信令数据报文"),
    CLIENT_MSG_30316364(0x30316364, "附件上传消息"),
    SERVER_MSG_9208(0x9208, "报警附件上传指令"),
    SERVER_MSG_9212(0x9212, "文件上传完成消息应答"),
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
