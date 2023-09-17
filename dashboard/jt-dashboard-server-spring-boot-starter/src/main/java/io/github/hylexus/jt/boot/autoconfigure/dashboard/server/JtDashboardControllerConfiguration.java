package io.github.hylexus.jt.boot.autoconfigure.dashboard.server;

import io.github.hylexus.jt.dashboard.server.controller.instance.Jt1078InstanceController;
import io.github.hylexus.jt.dashboard.server.controller.instance.Jt808InstanceController;
import io.github.hylexus.jt.dashboard.server.controller.instance.JtInstanceController;
import io.github.hylexus.jt.dashboard.server.registry.Jt1078InstanceRegistry;
import io.github.hylexus.jt.dashboard.server.registry.Jt808InstanceRegistry;
import io.github.hylexus.jt.dashboard.server.service.IntervalCheck;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class JtDashboardControllerConfiguration {

    @Bean
    public JtInstanceController jtInstanceController(ProxyInstanceProvider instanceProvider) {
        return new JtInstanceController(instanceProvider);
    }

    @Bean
    public Jt808InstanceController jt808InstanceController(Jt808InstanceRegistry registry, @Qualifier("jt808ServerSimpleMetricsIntervalCheck") IntervalCheck intervalCheck) {
        return new Jt808InstanceController(registry, intervalCheck);
    }

    @Bean
    public Jt1078InstanceController jt1078InstanceController(Jt1078InstanceRegistry registry, @Qualifier("jt1078ServerSimpleMetricsIntervalCheck") IntervalCheck intervalCheck) {
        return new Jt1078InstanceController(registry, intervalCheck);
    }

}
