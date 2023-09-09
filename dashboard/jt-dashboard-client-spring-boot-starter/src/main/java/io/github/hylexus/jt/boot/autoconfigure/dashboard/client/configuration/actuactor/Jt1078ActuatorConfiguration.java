package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.actuactor;

import io.github.hylexus.jt.dashboard.client.actuator.jt1078.metrics.simple.Jt1078ServerSimpleMetricsEndpoint;
import io.github.hylexus.jt.dashboard.client.actuator.jt1078.metrics.simple.Jt1078ServerSimpleMetricsHolder;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@ConditionalOnAvailableEndpoint(endpoint = Jt1078ServerSimpleMetricsEndpoint.class)
public class Jt1078ActuatorConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 100)
    public Jt1078ServerSimpleMetricsEndpoint jt1078ServerInfoContributor(Jt1078ServerSimpleMetricsHolder jt1078ServerSimpleMetricsHolder) {
        return new Jt1078ServerSimpleMetricsEndpoint(jt1078ServerSimpleMetricsHolder);
    }

    @Bean
    public Jt1078ServerSimpleMetricsHolder jt1078ServerInfoCollector() {
        return new Jt1078ServerSimpleMetricsHolder();
    }

    @Bean
    public Jt1078ServerSimpleMetricsHolder.SessionJt1078ServerInfoCollector sessionJt1078ServerInfoCollector(Jt1078ServerSimpleMetricsHolder collector) {
        return new Jt1078ServerSimpleMetricsHolder.SessionJt1078ServerInfoCollector(collector);
    }
}
