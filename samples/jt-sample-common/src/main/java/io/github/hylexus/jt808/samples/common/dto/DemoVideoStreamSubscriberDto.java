package io.github.hylexus.jt808.samples.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Optional;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class DemoVideoStreamSubscriberDto {

    // 终端手机号
    private String sim;
    // 逻辑通道号
    private short channel;
    // 超时时间(秒)
    // 超过 timeout 秒之后依然没有收到终端的数据 ==> 自动关闭当前订阅(websocket)
    private int timeout = 10;
    // 将 byte[] 以 base64 编码后再发送给客户端(jackson内置默认逻辑)?
    private boolean byteArrayAsBase64 = false;

    // websocket/http 断开时自动关闭 1078 服务端和终端的连接?
    private boolean autoCloseJt1078SessionOnClientClosed = true;

    // 自动发送 0x9101 消息给终端(websocket/http 连接建立时)
    private boolean autoSend9101Command = false;

    private String jt808ServerIp;
    private Integer jt808ServerPortHttp = 8808;

    private String jt1078ServerIp;
    private Integer jt1078ServerPortTcp = 61078;
    private Integer jt1078ServerPortUdp = 0;
    // 订阅的码流类型
    // 0:主码流; 1:子码流
    private int streamType = 0;
    // 音视频
    private int dataType = 0;

    private String sourceAudioHints;

    public static boolean parseBoolean(Map<String, String> params, String key, boolean def) {
        return Optional.ofNullable(params.get(key)).map(it -> {
            try {
                return Boolean.parseBoolean(it);
            } catch (Exception e) {
                return def;
            }
        }).orElse(def);
    }
}
