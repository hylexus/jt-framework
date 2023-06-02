package io.github.hylexus.jt.jt1078.samples.webflux.boot3.web;

import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.oaks.utils.Numbers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriTemplate;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WebSocketSubscriberDemo01 implements WebSocketHandler {
    public static final String PATH_PATTERN = "/jt1078/subscription/{sim}/{channel}";
    private final Jt1078Publisher jt1078Publisher;
    private final UriTemplate uriTemplate;

    public WebSocketSubscriberDemo01(Jt1078Publisher jt1078Publisher) {
        this.jt1078Publisher = jt1078Publisher;
        this.uriTemplate = new UriTemplate(PATH_PATTERN);
    }

    @NonNull
    @Override
    public Mono<Void> handle(@NonNull WebSocketSession session) {

        final Params params = this.parseParams(session);

        log.info("New publisher: {}", params);
        return this.jt1078Publisher.subscribe(params.sim(), params.channel(), Duration.ofSeconds(params.timeoutInSeconds()))
                .filter(it -> it.getRequest().payloadType() == DefaultJt1078PayloadType.H264)
                .flatMap(subscription -> {
                    final byte[] data = new byte[subscription.getRequest().body().readableBytes()];
                    subscription.getRequest().body().getBytes(0, data);

                    log.info("WebSocket outbound: {}", HexStringUtils.bytes2HexString(data));
                    final WebSocketMessage webSocketMessage = session.binaryMessage(factory -> factory.wrap(data));
                    return Mono.just(webSocketMessage);
                })
                .flatMap(it -> session.send(Mono.just(it)))
                .then(session.close());
    }

    @SuppressWarnings("checkstyle:methodname")
    record Params(String sim, short channel, long timeoutInSeconds) {
    }

    private Params parseParams(WebSocketSession session) {
        final URI uri = session.getHandshakeInfo().getUri();
        final Map<String, String> values = this.uriTemplate.match(uri.getPath());
        final String sim = values.getOrDefault("sim", "018930946552");
        final short channel = Numbers.parseInteger(values.getOrDefault("channel", "3")).orElseThrow().shortValue();
        final String query = uri.getQuery();
        if (StringUtils.isEmpty(query)) {
            return new Params(sim, channel, 10L);
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
        return new Params(sim, channel, timeout);
    }
}