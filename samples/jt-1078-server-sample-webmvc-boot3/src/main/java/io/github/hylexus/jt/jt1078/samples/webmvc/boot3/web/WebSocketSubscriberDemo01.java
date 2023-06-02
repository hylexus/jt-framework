package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.web;

import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.oaks.utils.Numbers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WebSocketSubscriberDemo01 extends AbstractWebSocketHandler {
    public static final String PATH_PATTERN = "/jt1078/subscription/{sim}/{channel}";
    private final Map<String, WebSocketSession> sessionMap = new HashMap<>();
    private final UriTemplate uriTemplate;
    private final Jt1078Publisher publisher;

    public WebSocketSubscriberDemo01(Jt1078Publisher publisher) {
        this.publisher = publisher;
        this.uriTemplate = new UriTemplate(PATH_PATTERN);
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        final Params params = this.parseParams(session);
        log.info("{}", params);
        sessionMap.put(session.getId(), session);
        log.info("session add : {}", session);

        this.publisher.subscribe(params.sim(), params.channel(), Duration.ofSeconds(params.timeout()))
                .filter(it -> it.getRequest().payloadType() == DefaultJt1078PayloadType.H264)
                .doOnNext(subscription -> {
                    final byte[] data = new byte[subscription.getRequest().body().readableBytes()];
                    subscription.getRequest().body().getBytes(0, data);
                    log.info("WebSocket outbound: {}", HexStringUtils.bytes2HexString(data));
                    try {
                        session.sendMessage(new BinaryMessage(data));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .doFinally(signalType -> {
                    final WebSocketSession remove = sessionMap.remove(session.getId());
                    if (remove != null) {
                        try {
                            remove.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .subscribe();
        log.info("session add : {}", session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        this.sessionMap.remove(session.getId());
        log.info("session {} closed with status  {}", session, closeStatus);
    }


    @SuppressWarnings("checkstyle:methodname")
    record Params(String sim, short channel, long timeout) {
    }

    private Params parseParams(WebSocketSession session) {
        final URI uri = session.getUri();
        if (uri == null) {
            throw new IllegalArgumentException();
        }
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
