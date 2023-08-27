package io.github.hylexus.jt.dashboard.client.registration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Jt1078Application extends JtApplication {
    private String host;
    private int httpPort;
    private int tcpPort;
    private int udpPort;

    @Builder
    public Jt1078Application(String name, String type, String baseUrl, String source, Map<String, String> metadata, String host, int httpPort, int tcpPort, int udpPort) {
        super(name, type, baseUrl, metadata);
        this.host = host;
        this.httpPort = httpPort;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
    }
}
