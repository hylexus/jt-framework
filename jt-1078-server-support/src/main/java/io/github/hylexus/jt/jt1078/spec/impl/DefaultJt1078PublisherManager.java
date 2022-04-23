package io.github.hylexus.jt.jt1078.spec.impl;

import io.github.hylexus.jt.jt1078.spec.*;
import io.github.hylexus.jt.jt1078.spec.impl.subpub.FluxBasedJt1078Publisher;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078Collector;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078CollectorFactory;
import io.github.hylexus.jt.jt1078.support.exception.Jt1078CollectorNotFoundException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.FluxSink;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hylexus
 */
@Slf4j
public class DefaultJt1078PublisherManager implements Jt1078PublisherManager {

    private final Jt1078SessionManager sessionManager;
    private final Jt1078CollectorFactory collectorFactory;

    private final ConcurrentMap<Jt1078SubscriptionSelector, Jt1078Publisher> publishers = new ConcurrentHashMap<>();

    public DefaultJt1078PublisherManager(Jt1078SessionManager sessionManager, Jt1078CollectorFactory collectorFactory) {
        this.sessionManager = sessionManager;
        this.collectorFactory = collectorFactory;
    }

    @Override
    public Optional<Jt1078Publisher> getPublisher(Jt1078SubscriptionSelector identifier) {
        return Optional.ofNullable(this.publishers.get(identifier));
    }

    @Override
    public Jt1078Publisher getOrCreatePublisher(Jt1078SubscriptionSelector selector) {
        final Jt1078Publisher publisher = this.publishers.get(selector);
        if (publisher != null) {
            return publisher;
        }
        final Jt1078Collector collector = this.getOrCreateCollector(selector, null);
        final FluxBasedJt1078Publisher newPublisher = new FluxBasedJt1078Publisher(sink -> {
            collector.setSink(sink);
        });

        this.publishers.put(selector, newPublisher);
        return newPublisher;
        //return this.publishers.computeIfAbsent(selector, sel -> {
        //    return new FluxBasedJt1078Publisher(sink -> {
        //        final Jt1078Collector collector = this.getOrCreateCollector(sel, sink);
        //    });
        //});
    }

    private Jt1078Collector getOrCreateCollector(Jt1078SubscriptionSelector selector, FluxSink<Jt1078Subscription> sink) {
        final Jt1078Session session = this.sessionManager.findBySimOrThrow(selector.sim());

        final Collection<Jt1078Collector> existedCollectors = session.collectors(selector.channelNumber());

        for (Jt1078Collector collector : existedCollectors) {
            if (collector.support(selector)) {
                return collector;
            }
        }

        final Jt1078Collector<Jt1078Subscription> collector = this.collectorFactory.createCollector(selector.sourceType(), selector.targetType())
                .orElseThrow(() -> new Jt1078CollectorNotFoundException(selector.sourceType(), selector.targetType()));

        collector.setSink(sink);

        final Collection<Jt1078Collector> newCollectors = new LinkedList<>(existedCollectors);
        newCollectors.add(collector);
        session.collectors(selector.channelNumber(), newCollectors);
        return collector;
    }

    @Override
    public void removePublisher(Jt1078SubscriptionSelector identifier) {
        final Jt1078Publisher publisher = this.publishers.remove(identifier);
        if (publisher != null) {
            publisher.destroy();
        }
    }

}
