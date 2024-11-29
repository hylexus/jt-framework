package io.github.hylexus.jt.jt808.samples.debug.filter;

import io.github.hylexus.jt.jt808.spec.Jt808RequestFilter;
import io.github.hylexus.jt.jt808.spec.Jt808RequestFilterChain;
import io.github.hylexus.jt.jt808.spec.Jt808ServerExchange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
// @Component
@Order(200)
public class DemoFilter01 implements Jt808RequestFilter {

    // 看你需要修改
    ExecutorService executorService = Executors.newFixedThreadPool(2, new ThreadFactory() {
        final AtomicInteger threadCount = new AtomicInteger();

        @Override
        public Thread newThread(@NonNull Runnable r) {
            final Thread thread = new Thread(r);
            thread.setName("mythread-" + threadCount.incrementAndGet());
            return thread;
        }
    });

    @Override
    public void filter(Jt808ServerExchange exchange, Jt808RequestFilterChain chain) {
        log.info("~~~~~~~~~~~~~~~~ --- filter {}", this.getClass().getSimpleName());
        exchange.request().body().retain();
        try {
            executorService.submit(() -> {
                try {
                    chain.filter(exchange);
                } finally {
                    exchange.request().body().release();
                }
            });
        } catch (Throwable e) {
            exchange.request().body().release();
        }
    }
}
