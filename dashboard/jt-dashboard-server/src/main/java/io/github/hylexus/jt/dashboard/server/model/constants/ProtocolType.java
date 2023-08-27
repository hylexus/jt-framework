package io.github.hylexus.jt.dashboard.server.model.constants;

import lombok.Getter;

@Getter
public enum ProtocolType {
    HTTP("http"),
    HTTPS("https"),
    WEBSOCKET("ws"),
    ;
    private final String scheme;

    ProtocolType(String scheme) {
        this.scheme = scheme;
    }
}
