package io.github.hylexus.jt.jt1078.boot.props.server;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Validated
public class Jt1078NettyTcpServerProps {
    private int port = 61078;

    private int bossThreadCount = 0;

    private int workerThreadCount = 0;

    @NestedConfigurationProperty
    private IdleStateHandlerProps idleStateHandler = new IdleStateHandlerProps();

    @Data
    public static class IdleStateHandlerProps {
        private boolean enabled = true;
        private Duration readerIdleTime = Duration.ofMinutes(20);
        private Duration writerIdleTime = Duration.ZERO;
        private Duration allIdleTime = Duration.ZERO;
    }
}
