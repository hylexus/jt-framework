package io.github.hylexus.jt.demos.jt808.configuration.pros;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.jt808-server")
public class Jt808AppProps {
    private String serverIp;
    private Attachment attachment = new Attachment();

    @Data
    public static class Attachment {
        private String temporaryPath;
    }
}
