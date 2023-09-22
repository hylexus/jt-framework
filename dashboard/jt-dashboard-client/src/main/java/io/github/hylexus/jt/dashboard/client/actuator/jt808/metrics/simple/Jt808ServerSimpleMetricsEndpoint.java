package io.github.hylexus.jt.dashboard.client.actuator.jt808.metrics.simple;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

@Endpoint(id = "jt808SimpleMetrics")
public class Jt808ServerSimpleMetricsEndpoint {
    private final Jt808ServerSimpleMetricsHolder collector;

    public Jt808ServerSimpleMetricsEndpoint(Jt808ServerSimpleMetricsHolder collector) {
        this.collector = collector;
    }

    @ReadOperation
    public Jt808ServerSimpleMetricsHolder all() {
        return collector;
    }
}
