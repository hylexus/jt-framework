package io.github.hylexus.jt808.samples.common.dto;

import io.github.hylexus.oaks.utils.Numbers;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.HashMap;
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
    private boolean autoCloseJt1078SessionOnClientClosed = false;

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

    public static DemoVideoStreamSubscriberDto of(WebSocketSession session, UriTemplate uriTemplate) {
        final URI uri = session.getHandshakeInfo().getUri();
        final Map<String, String> values = uriTemplate.match(uri.getPath());
        final String sim = values.getOrDefault("sim", "018930946552");
        final short channel = Numbers.parseInteger(values.getOrDefault("channel", "3")).orElseThrow().shortValue();
        final String query = uri.getQuery();
        if (!StringUtils.hasText(query)) {
            return new DemoVideoStreamSubscriberDto().setSim(sim).setChannel(channel);
        }

        final String[] arrays = query.split("&");
        final Map<String, String> params = new HashMap<>();
        for (final String item : arrays) {
            final String[] split = item.split("=");
            if (split.length == 2) {
                params.put(split[0], split[1]);
            }
        }

        final int timeout = Numbers.parseInteger(params.get("timeout")).orElse(10);
        return new DemoVideoStreamSubscriberDto()
                .setSim(sim)
                .setChannel(channel)
                .setTimeout(timeout)
                .setByteArrayAsBase64(parseBoolean(params, "byteArrayAsBase64", false))
                .setAutoCloseJt1078SessionOnClientClosed(parseBoolean(params, "autoCloseJt1078SessionOnClientClosed", false))
                .setAutoSend9101Command(parseBoolean(params, "autoSend9101Command", false))
                .setJt808ServerIp(params.get("jt808ServerIp"))
                .setJt808ServerPortHttp(Numbers.parseInteger(params.get("jt808ServerPortHttp")).orElse(null))
                .setJt1078ServerIp(params.get("jt1078ServerIp"))
                .setJt1078ServerPortTcp(Numbers.parseInteger(params.get("jt1078ServerPortTcp")).orElse(null))
                .setJt1078ServerPortUdp(Numbers.parseInteger(params.get("jt1078ServerPortUdp")).orElse(null))
                .setStreamType(Numbers.parseInteger(params.get("streamType")).orElse(0))
                .setDataType(Numbers.parseInteger(params.get("dataType")).orElse(0))
                ;

    }

    static boolean parseBoolean(Map<String, String> params, String key, boolean def) {
        return Optional.ofNullable(params.get(key)).map(it -> {
            try {
                return Boolean.parseBoolean(it);
            } catch (Exception e) {
                return def;
            }
        }).orElse(def);
    }
}
