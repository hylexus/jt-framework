package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;

@Setter
@Getter
@ToString
public class Jt1078ApplicationProps extends BaseJtApplicationProps {
    private String host;
    private int httpPort;
    private int tcpPort;
    private int udpPort;

    @NestedConfigurationProperty
    private BuiltinFlvPlayerProps builtinFlvPlayer = new BuiltinFlvPlayerProps();

    @Setter
    @Getter
    public static class BuiltinFlvPlayerProps {
        @NestedConfigurationProperty
        private SchedulerProps scheduler = new SchedulerProps();
    }

    @Setter
    @Getter
    public static class SchedulerProps {
        private String name = "flvPlayer";
        private int threadCapacity = Runtime.getRuntime().availableProcessors() * 2;
        private int queuedTaskCapacity = 256;
        private Duration ttl = Duration.ofMinutes(1);
        private boolean daemon = true;
    }
}
