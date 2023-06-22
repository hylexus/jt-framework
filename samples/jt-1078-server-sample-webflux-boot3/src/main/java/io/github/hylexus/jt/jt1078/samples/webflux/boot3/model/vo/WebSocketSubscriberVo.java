package io.github.hylexus.jt.jt1078.samples.webflux.boot3.model.vo;

import io.github.hylexus.oaks.utils.Numbers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record WebSocketSubscriberVo(String sim, short channel, long timeout, boolean byteArrayAsBase64) {

    public static WebSocketSubscriberVo of(WebSocketSession session, UriTemplate uriTemplate) {
        final URI uri = session.getHandshakeInfo().getUri();
        final Map<String, String> values = uriTemplate.match(uri.getPath());
        final String sim = values.getOrDefault("sim", "018930946552");
        final short channel = Numbers.parseInteger(values.getOrDefault("channel", "3")).orElseThrow().shortValue();
        final String query = uri.getQuery();
        if (StringUtils.isEmpty(query)) {
            return new WebSocketSubscriberVo(sim, channel, 10L, false);
        }

        final String[] arrays = query.split("&");
        final Map<String, String> params = new HashMap<>();
        for (final String item : arrays) {
            final String[] split = item.split("=");
            if (split.length == 2) {
                params.put(split[0], split[1]);
            }
        }
        final Long timeout = Numbers.parseLong(params.get("timeout")).orElse(10L);
        final Boolean arrayAsBase64 = Optional.ofNullable(params.get("byteArrayAsBase64")).map(it -> {
            try {
                return Boolean.parseBoolean(it);
            } catch (Exception e) {
                return false;
            }
        }).orElse(false);
        return new WebSocketSubscriberVo(sim, channel, timeout, arrayAsBase64);
    }
}
