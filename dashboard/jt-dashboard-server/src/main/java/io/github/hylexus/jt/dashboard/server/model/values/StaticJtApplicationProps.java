package io.github.hylexus.jt.dashboard.server.model.values;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "jt-dashboard.server.static-application-registrator")
@Getter
@Setter
public class StaticJtApplicationProps {
    private boolean enabled = true;
    private List<StaticJt808ApplicationProps> jt808Applications = new ArrayList<>();
    private List<StaticJt1078ApplicationProps> jt1078Applications = new ArrayList<>();
}
