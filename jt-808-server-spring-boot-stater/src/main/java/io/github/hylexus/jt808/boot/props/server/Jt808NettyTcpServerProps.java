package io.github.hylexus.jt808.boot.props.server;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import java.time.Duration;

/**
 * @author hylexus
 * Created At 2019-08-26 10:24 下午
 */
@Getter
@Setter
@ToString
@Validated
public class Jt808NettyTcpServerProps {
    private int port = 6808;

    @Min(value = 0, message = "bossThreadCount >= 0, 0 means that Netty's default logical")
    private int bossThreadCount = 0;

    @Min(value = 0, message = "workerThreadCount >= 0, 0 means that Netty's default logical")
    private int workerThreadCount = 0;

    @NestedConfigurationProperty
    private IdleStateHandlerProps idleStateHandler = new IdleStateHandlerProps();

    @Data
    public static class IdleStateHandlerProps {
        private boolean enabled = true;
        private Duration readerIdleTime = Duration.ofMinutes(20);
        private Duration writerIdleTime = Duration.ofMinutes(20);
        private Duration allIdleTime = Duration.ofMinutes(20);
    }
}
