package io.github.hylexus.jt.jt808.samples.debug.filter;

import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.Jt808RequestFilter;
import io.github.hylexus.jt.jt808.spec.Jt808RequestFilterChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(200)
public class DemoFilter01 implements Jt808RequestFilter {
    @Override
    public void filter(Jt808ServerExchange exchange, Jt808RequestFilterChain chain) {
        log.info("~~~~~~~~~~~~~~~~ --- filter {}", this.getClass().getSimpleName());
        chain.filter(exchange);
    }
}
