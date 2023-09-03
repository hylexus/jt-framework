package io.github.hylexus.jt.boot.autoconfigure.dashboard.server.reactive;

import io.github.hylexus.jt.dashboard.server.controller.proxy.reactive.DashboardInstanceProxyController;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClient;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

@ConditionalOnWebApplication(type = REACTIVE)
public class JtDashboardReactiveAutoConfiguration {

    @Bean
    public DashboardInstanceProxyController dashboardInstanceProxyController(ProxyInstanceProvider instanceSupplier, DashboardWebClient.Builder builder) {
        return new DashboardInstanceProxyController(instanceSupplier, builder);
    }

}
