package io.github.hylexus.jt808.boot.props;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hylexus
 * Created At 2019-08-26 10:24 下午
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "jt808.server")
public class Jt808NettyTcpServerProps {
    private int port;
    private int bossThreadCount = 0;
    private int workerThreadCount = 0;
}
