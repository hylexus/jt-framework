package io.github.hylexus.jt.demos.dashboard.webmvc.boot3.configuration;

import io.github.hylexus.jt.dashboard.server.proxy.DashboardInstanceExchangeFilterFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DashboardFilterConfiguration {
    @Bean
    public DashboardInstanceExchangeFilterFunction demoFilter1() {
        return (instance, request, next) -> {
            log.info("instanceUrl: {}", instance.getRegistration().getBaseUrl());
            return next.exchange(request);
        };
    }
}
