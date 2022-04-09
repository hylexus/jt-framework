package io.github.hylexus.jt.jt808.samples.debug.listener;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListener;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

/**
 * @author hylexus
 */
@Component
public class PrometheusMetricsExporter implements Jt808RequestLifecycleListener {

    private final MeterRegistry meterRegistry;

    public PrometheusMetricsExporter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public boolean beforeDispatch(Jt808Request request) {
        Counter.builder("jt808_request_total_count")
                .description("A counter for JT/T 808 request processed by current server")
                .tags(
                        "terminal_id", request.terminalId(),
                        "version", request.version().name().toLowerCase()
                )
                .register(this.meterRegistry)
                .increment();

        return true;
    }
}
