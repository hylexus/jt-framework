package io.github.hylexus.jt.boot.autoconfigure.dashboard.server.servlet;

import io.github.hylexus.jt.dashboard.server.controller.proxy.servlet.DashboardInstanceProxyController;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClient;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JtDashboardServletAutoConfiguration {

    @Bean
    public DashboardInstanceProxyController dashboardInstanceProxyController(ProxyInstanceProvider proxyInstanceProvider, DashboardWebClient.Builder builder) {
        return new DashboardInstanceProxyController(proxyInstanceProvider, builder);
    }
}
