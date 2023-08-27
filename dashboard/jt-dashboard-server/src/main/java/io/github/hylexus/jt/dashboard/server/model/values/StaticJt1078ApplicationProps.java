package io.github.hylexus.jt.dashboard.server.model.values;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaticJt1078ApplicationProps extends AbstractStaticJtApplicationProps {
    private String host;
    private int tcpPort;
    private int udpPort;
    private int httpPort;
}
