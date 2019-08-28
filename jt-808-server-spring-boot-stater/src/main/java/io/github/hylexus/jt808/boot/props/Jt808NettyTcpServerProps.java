package io.github.hylexus.jt808.boot.props;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author hylexus
 * Created At 2019-08-26 10:24 下午
 */
@Getter
@Setter
@ToString
public class Jt808NettyTcpServerProps {
    private int port;
    private int bossThreadCount = 0;
    private int workerThreadCount = 0;
}
