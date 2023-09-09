package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078;

import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.actuactor.Jt1078ActuatorConfiguration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.condition.ConditionalOnJt1078ServerPresent;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078.blocking.BlockingJtDashboard1078Configuration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078.reactive.ReactiveJtDashboard1078Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnJt1078ServerPresent
@Import({
        ReactiveJtDashboard1078Configuration.class,
        BlockingJtDashboard1078Configuration.class,
        BuiltinJt1078ControllerConfiguration.class,
        Jt1078ActuatorConfiguration.class,
})
public class JtDashboard1078AutoConfiguration {
}
