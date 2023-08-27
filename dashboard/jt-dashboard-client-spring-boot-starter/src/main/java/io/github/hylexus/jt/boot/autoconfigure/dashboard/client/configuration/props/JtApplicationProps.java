package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties(prefix = "jt-dashboard.client")
public class JtApplicationProps {

    @NestedConfigurationProperty
    private Jt808ApplicationProps jt808 = new Jt808ApplicationProps();

    @NestedConfigurationProperty
    private Jt1078ApplicationProps jt1078 = new Jt1078ApplicationProps();
}
