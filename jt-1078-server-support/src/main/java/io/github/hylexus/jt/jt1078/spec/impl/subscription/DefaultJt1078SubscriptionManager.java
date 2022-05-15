package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.*;
import io.github.hylexus.jt.jt1078.spec.impl.InternalFluxBasedJt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.impl.InternalJt1078Publisher;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078Collector;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078CollectorFactory;
import io.github.hylexus.jt.jt1078.support.exception.Jt1078CollectorNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * @author hylexus
 */
@Slf4j
public class DefaultJt1078SubscriptionManager implements Jt1078SubscriptionManager {
    public static final String LOG_PREFIX = "==> Jt1078PublisherManager.";
    private final Jt1078SessionManager sessionManager;
    private final Jt1078CollectorFactory collectorFactory;

    public DefaultJt1078SubscriptionManager(Jt1078SessionManager sessionManager, Jt1078CollectorFactory collectorFactory) {
        this.sessionManager = sessionManager;
        this.collectorFactory = collectorFactory;
    }

    @Override
    public synchronized Jt1078Publisher subscribe(Jt1078SubscriptionSelector selector) {
        final Jt1078Collector collector = this.getOrCreateCollector(selector);

        final InternalJt1078Publisher publisher = new InternalFluxBasedJt1078Publisher(
                jt1078Sink -> {
                    log.info("{} Sink [Initialized]. instance={}", LOG_PREFIX, jt1078Sink);
                },
                (signalType, jt1078Sink) -> {
                    log.info("{} Sink [Removed]. signalType={}, instance={}", LOG_PREFIX, signalType, jt1078Sink);
                    collector.removeSink(jt1078Sink);
                });
        collector.addSink(publisher);
        log.info("{} Sink [Added]. instance={}", LOG_PREFIX, publisher);
        return publisher;
    }

    private Jt1078Collector getOrCreateCollector(Jt1078SubscriptionSelector selector) {
        final Jt1078Session session = this.sessionManager.findBySimOrThrow(selector.sim());

        final Collection<Jt1078Collector> converters = session.getChannelConverters(selector.channelNumber());
        for (Jt1078Collector converter : converters) {
            if (converter.support(selector)) {
                return converter;
            }
        }

        final Jt1078Collector jt1078Collector = this.collectorFactory.createCollector(selector)
                .orElseThrow(() -> new Jt1078CollectorNotFoundException(selector.sourceType(), selector.targetType()));

        session.addChannelConverter(selector.channelNumber(), jt1078Collector);
        return jt1078Collector;
    }

}
