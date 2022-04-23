package io.github.hylexus.jt.jt1078.spec.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestMsgDispatcher;
import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078Collector;
import io.github.hylexus.jt.jt1078.support.exception.Jt1078SessionNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * @author hylexus
 */
@Slf4j
public class DefaultJt1078RequestMsgDispatcher implements Jt1078RequestMsgDispatcher {

    private final Jt1078SessionManager sessionManager;

    public DefaultJt1078RequestMsgDispatcher(Jt1078SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void doDispatch(Jt1078Request request) throws Throwable {
        try {
            log.info("{}", request);
            this.doDispatchInternal(request);
        } finally {
            request.release();
        }
    }

    private void doDispatchInternal(Jt1078Request request) throws Throwable {

        // TODO sub-package && media-format-converter

        final Jt1078Session session = sessionManager.findBySim(request.sim())
                .orElseThrow(() -> new Jt1078SessionNotFoundException(request.sim()));

        final Collection<Jt1078Collector> collectors = session.collectors(request.channelNumber());

        if (collectors.isEmpty()) {
            log.error("No collector found for request {}", request);
            return;
        }

        for (Jt1078Collector collector : collectors) {
            collector.collect(request);
        }

    }

}
