package io.github.hylexus.jt.dashboard.client.actuator.jt1078.metrics.simple;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

@Endpoint(id = "jt1078SimpleMetrics")
public class Jt1078ServerSimpleMetricsEndpoint {
    private final Jt1078ServerSimpleMetricsHolder collector;

    public Jt1078ServerSimpleMetricsEndpoint(Jt1078ServerSimpleMetricsHolder collector) {
        this.collector = collector;
    }

    @ReadOperation
    public Jt1078ServerSimpleMetricsHolder all() {
        return collector;
    }
}
