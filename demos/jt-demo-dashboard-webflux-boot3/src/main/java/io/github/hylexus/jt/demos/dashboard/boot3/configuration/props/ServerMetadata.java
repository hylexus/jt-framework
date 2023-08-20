package io.github.hylexus.jt.demos.dashboard.boot3.configuration.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties("jt-dashboard")
public class ServerMetadata {
    public List<Jt808ServerMetadata> jt808ServerMetadata = new ArrayList<>();
    public List<Jt1078ServerMetadata> jt1078ServerMetadata = new ArrayList<>();

    @Data
    public static class Jt808ServerMetadata {
        private String instanceId;
        private String host;
        private int httpPort;
        private boolean secure = false;
        private String notes;

        public String getBaseUrl() {
            return (this.isSecure() ? "https://" : "http://") + this.getHost() + ":" + this.getHttpPort();
        }
    }

    @Data
    public static class Jt1078ServerMetadata {
        private String instanceId;
        private String host;
        private String httpPort;
        private int tcpPort;
        private String notes;
    }
}
