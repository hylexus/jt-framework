package io.github.hylexus.jt808.samples.client.debug.client1;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hylexus
 */
@Data
@ConfigurationProperties(prefix = "client")
public class ClientProps {
    private String serverIp = "127.0.0.1";
    private int serverPort = 6808;
    private int clientCount = Runtime.getRuntime().availableProcessors() * 2;
}
