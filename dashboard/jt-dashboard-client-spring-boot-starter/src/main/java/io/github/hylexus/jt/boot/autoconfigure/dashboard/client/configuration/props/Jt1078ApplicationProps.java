package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Jt1078ApplicationProps extends BaseJtApplicationProps {
    private String host;
    private int httpPort;
    private int tcpPort;
    private int udpPort;
}
