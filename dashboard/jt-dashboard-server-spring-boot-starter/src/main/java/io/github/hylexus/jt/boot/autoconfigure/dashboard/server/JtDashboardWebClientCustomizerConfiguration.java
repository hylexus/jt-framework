package io.github.hylexus.jt.boot.autoconfigure.dashboard.server;

import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClientCustomizer;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardInstanceExchangeFilterFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Slf4j
public class JtDashboardWebClientCustomizerConfiguration {

    @Bean
    @ConditionalOnBean(DashboardInstanceExchangeFilterFunction.class)
    public DashboardWebClientCustomizer dashboardWebClientCustomizer(List<DashboardInstanceExchangeFilterFunction> filters) {
        return (builder) -> builder.filters((f) -> f.addAll(filters));
    }

}
