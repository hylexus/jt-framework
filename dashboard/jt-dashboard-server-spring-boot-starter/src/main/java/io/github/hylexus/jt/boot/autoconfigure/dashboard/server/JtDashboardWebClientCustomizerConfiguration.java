package io.github.hylexus.jt.boot.autoconfigure.dashboard.server;

import io.github.hylexus.jt.dashboard.server.proxy.DashboardInstanceExchangeFilterFunction;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClientCustomizer;
import io.github.hylexus.jt.dashboard.server.proxy.impl.BuiltinDashboardInstanceExchangeFilterFunctions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@Slf4j
public class JtDashboardWebClientCustomizerConfiguration {

    // 这个应该尽量靠前(在这个之前的 filter 获取到的 URL 可能不是绝对路径)
    @Order(-10)
    @Bean("rewritePathDashboardInstanceExchangeFilterFunction")
    @ConditionalOnMissingBean(name = "rewritePathDashboardInstanceExchangeFilterFunction")
    public DashboardInstanceExchangeFilterFunction rewritePathFilter() {
        return BuiltinDashboardInstanceExchangeFilterFunctions.rewritePath();
    }

    @Bean
    @ConditionalOnBean(DashboardInstanceExchangeFilterFunction.class)
    public DashboardWebClientCustomizer dashboardWebClientCustomizer(ObjectProvider<DashboardInstanceExchangeFilterFunction> list) {
        return (builder) -> builder.filters((filters) -> filters.addAll(list.orderedStream().toList()));
    }

}
