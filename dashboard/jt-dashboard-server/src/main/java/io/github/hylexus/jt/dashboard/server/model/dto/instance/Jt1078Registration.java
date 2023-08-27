package io.github.hylexus.jt.dashboard.server.model.dto.instance;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Jt1078Registration extends JtRegistration {
    private String host;
    private int httpPort;
    private int tcpPort;
    private int udpPort;
}
