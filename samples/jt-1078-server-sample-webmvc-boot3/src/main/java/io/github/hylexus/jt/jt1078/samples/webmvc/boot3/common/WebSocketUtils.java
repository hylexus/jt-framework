package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.common;

import io.github.hylexus.jt808.samples.common.dto.DemoVideoStreamSubscriberDto;
import io.github.hylexus.oaks.utils.Numbers;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static io.github.hylexus.jt808.samples.common.dto.DemoVideoStreamSubscriberDto.parseBoolean;

public class WebSocketUtils {

    public static DemoVideoStreamSubscriberDto createForBlockingSession(org.springframework.web.socket.WebSocketSession session, UriTemplate uriTemplate) {
        final URI uri = session.getUri();
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
                .setSourceAudioHints(params.get("sourceAudioHints"))
                ;
    }

}
