package io.github.hylexus.jt.boot.autoconfigure.dashboard.server.reactive;

import io.github.hylexus.jt.dashboard.server.controller.DashboardJt1078Controller;
import io.github.hylexus.jt.dashboard.server.controller.proxy.reactive.DashboardInstanceProxyController;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClient;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import io.github.hylexus.jt.dashboard.server.service.reactive.RandomReactiveJt1078InstanceChooser;
import io.github.hylexus.jt.dashboard.server.service.reactive.ReactiveJt1078InstanceChooser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

@ConditionalOnWebApplication(type = REACTIVE)
public class JtDashboardReactiveAutoConfiguration {

    @Bean
    public DashboardInstanceProxyController dashboardInstanceProxyController(ProxyInstanceProvider instanceSupplier, DashboardWebClient.Builder builder) {
        return new DashboardInstanceProxyController(instanceSupplier, builder);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReactiveJt1078InstanceChooser reactiveJt1078InstanceChooser() {
        return new RandomReactiveJt1078InstanceChooser();
    }

    @Bean
    public DashboardJt1078Controller dashboardJt1078Controller(DashboardWebClient.Builder builder, ProxyInstanceProvider instanceProvider, ReactiveJt1078InstanceChooser instanceChooser) {
        return new DashboardJt1078Controller(builder, instanceProvider, instanceChooser);
    }
}
