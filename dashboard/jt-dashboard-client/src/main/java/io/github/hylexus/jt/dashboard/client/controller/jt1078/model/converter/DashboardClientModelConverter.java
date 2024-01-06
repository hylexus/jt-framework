package io.github.hylexus.jt.dashboard.client.controller.jt1078.model.converter;

import io.github.hylexus.jt.dashboard.common.model.dto.jt1078.DashboardVideoStreamSubscriberDto;
import io.github.hylexus.oaks.utils.Numbers;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DashboardClientModelConverter {
    public static DashboardVideoStreamSubscriberDto convert(WebSocketSession session, UriTemplate uriTemplate) {
        final URI uri = session.getHandshakeInfo().getUri();
        final Map<String, String> values = uriTemplate.match(uri.getPath());
        final String sim = values.getOrDefault("sim", "111111111111");
        final short channel = Numbers.parseInteger(values.getOrDefault("channel", "3")).orElseThrow().shortValue();
        final String query = uri.getQuery();
        if (!StringUtils.hasText(query)) {
            return new DashboardVideoStreamSubscriberDto().setSim(sim).setChannel(channel);
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
        return new DashboardVideoStreamSubscriberDto()
                .setSim(sim)
                .setChannel(channel)
                .setTimeout(timeout)
                .setAutoCloseJt1078SessionOnClientClosed(parseBoolean(params, "autoCloseJt1078SessionOnClientClosed", false))
                .setStreamType(Numbers.parseInteger(params.get("streamType")).orElse(0))
                .setDataType(Numbers.parseInteger(params.get("dataType")).orElse(0))
                .setSourceAudioHints(params.get("sourceAudioHints"))
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
