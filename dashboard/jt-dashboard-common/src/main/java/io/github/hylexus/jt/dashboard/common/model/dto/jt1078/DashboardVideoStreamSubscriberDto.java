package io.github.hylexus.jt.dashboard.common.model.dto.jt1078;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class DashboardVideoStreamSubscriberDto {

    // 终端手机号
    private String sim;
    // 逻辑通道号
    private short channel;
    // 超时时间(秒)
    // 超过 timeout 秒之后依然没有收到终端的数据 ==> 自动关闭当前订阅(websocket)
    private int timeout = 10;

    // websocket/http 断开时自动关闭 1078 服务端和终端的连接?
    private boolean autoCloseJt1078SessionOnClientClosed = true;

    // 自动发送 0x9101 消息给终端(websocket/http 连接建立时)
    // private boolean autoSend9101Command = false;

    // 订阅的码流类型
    // 0:主码流; 1:子码流
    private int streamType = 0;
    // 0:音视频; 1:视频; 2: 双向对讲; ....
    private int dataType = 0;
    private String sourceAudioHints;
}
