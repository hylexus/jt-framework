package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt808;

import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.actuactor.Jt808ActuatorConfiguration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.condition.ConditionalOnJt808ServerPresent;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt808.blocking.BlockingJtDashboard808Configuration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt808.reactive.ReactiveJtDashboard808Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnJt808ServerPresent
@Import({
        ReactiveJtDashboard808Configuration.class,
        BlockingJtDashboard808Configuration.class,
        BuiltinJt808ControllerConfiguration.class,
        Jt808ActuatorConfiguration.class,
})
public class JtDashboard808AutoConfiguration {
}
