package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.actuactor;

import io.github.hylexus.jt.dashboard.client.actuator.jt808.metrics.simple.Jt808ServerSimpleMetricsEndpoint;
import io.github.hylexus.jt.dashboard.client.actuator.jt808.metrics.simple.Jt808ServerSimpleMetricsHolder;
import io.github.hylexus.jt.jt808.spec.Jt808MsgTypeParser;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


@ConditionalOnAvailableEndpoint(endpoint = Jt808ServerSimpleMetricsEndpoint.class)
public class Jt808ActuatorConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 100)
    public Jt808ServerSimpleMetricsEndpoint jt808ServerInfoContributor(Jt808ServerSimpleMetricsHolder jt808ServerSimpleMetricsHolder) {
        return new Jt808ServerSimpleMetricsEndpoint(jt808ServerSimpleMetricsHolder);
    }

    @Bean
    public Jt808ServerSimpleMetricsHolder.RequestJt808ServerInfoCollector requestJt808ServerInfoCollector(Jt808ServerSimpleMetricsHolder collector, Jt808MsgTypeParser msgTypeParser) {
        return new Jt808ServerSimpleMetricsHolder.RequestJt808ServerInfoCollector(collector, msgTypeParser);
    }

    @Bean
    public Jt808ServerSimpleMetricsHolder jt808ServerInfoCollector() {
        return new Jt808ServerSimpleMetricsHolder();
    }

    @Bean
    public Jt808ServerSimpleMetricsHolder.SessionJt808ServerInfoCollector sessionJt808ServerInfoCollector(Jt808ServerSimpleMetricsHolder collector) {
        return new Jt808ServerSimpleMetricsHolder.SessionJt808ServerInfoCollector(collector);
    }

}
