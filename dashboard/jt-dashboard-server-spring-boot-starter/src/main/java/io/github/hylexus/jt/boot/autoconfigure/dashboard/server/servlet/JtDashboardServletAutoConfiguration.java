package io.github.hylexus.jt.boot.autoconfigure.dashboard.server.servlet;

import io.github.hylexus.jt.dashboard.server.controller.BlockingDashboardJt1078Controller;
import io.github.hylexus.jt.dashboard.server.controller.proxy.servlet.DashboardInstanceProxyController;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClient;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import io.github.hylexus.jt.dashboard.server.service.servlet.BlockingJt1078InstanceChooser;
import io.github.hylexus.jt.dashboard.server.service.servlet.RandomBlockingJt1078InstanceChooser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JtDashboardServletAutoConfiguration {

    @Bean
    public DashboardInstanceProxyController dashboardInstanceProxyController(ProxyInstanceProvider proxyInstanceProvider, DashboardWebClient.Builder builder) {
        return new DashboardInstanceProxyController(proxyInstanceProvider, builder);
    }

    @Bean
    @ConditionalOnMissingBean
    public BlockingJt1078InstanceChooser reactiveJt1078InstanceChooser() {
        return new RandomBlockingJt1078InstanceChooser();
    }

    @Bean
    public BlockingDashboardJt1078Controller dashboardJt1078Controller(DashboardWebClient.Builder builder, ProxyInstanceProvider instanceProvider, BlockingJt1078InstanceChooser instanceChooser) {
        return new BlockingDashboardJt1078Controller(builder, instanceProvider, instanceChooser);
    }
}
