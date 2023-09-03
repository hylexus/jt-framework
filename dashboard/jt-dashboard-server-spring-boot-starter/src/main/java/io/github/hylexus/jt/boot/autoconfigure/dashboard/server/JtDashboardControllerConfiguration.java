package io.github.hylexus.jt.boot.autoconfigure.dashboard.server;

import io.github.hylexus.jt.dashboard.server.controller.BuiltinDashboardExceptionHandler;
import io.github.hylexus.jt.dashboard.server.controller.DashboardJt1078Controller;
import io.github.hylexus.jt.dashboard.server.controller.instance.Jt1078InstanceController;
import io.github.hylexus.jt.dashboard.server.controller.instance.Jt808InstanceController;
import io.github.hylexus.jt.dashboard.server.controller.instance.JtInstanceController;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClient;
import io.github.hylexus.jt.dashboard.server.registry.Jt1078InstanceRegistry;
import io.github.hylexus.jt.dashboard.server.registry.Jt808InstanceRegistry;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import org.springframework.context.annotation.Bean;

public class JtDashboardControllerConfiguration {

    @Bean
    public JtInstanceController jtInstanceController(ProxyInstanceProvider instanceProvider) {
        return new JtInstanceController(instanceProvider);
    }

    @Bean
    public Jt808InstanceController jt808InstanceController(Jt808InstanceRegistry registry) {
        return new Jt808InstanceController(registry);
    }

    @Bean
    public Jt1078InstanceController jt1078InstanceController(Jt1078InstanceRegistry registry) {
        return new Jt1078InstanceController(registry);
    }

    @Bean
    public DashboardJt1078Controller dashboardJt1078Controller(DashboardWebClient.Builder builder, ProxyInstanceProvider instanceProvider) {
        return new DashboardJt1078Controller(builder, instanceProvider);
    }

    @Bean
    public BuiltinDashboardExceptionHandler builtinDashboardExceptionHandler() {
        return new BuiltinDashboardExceptionHandler();
    }
}
