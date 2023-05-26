package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.Jt1078PublisherInternal;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.util.*;

@Slf4j
public class DefaultJt1078Publisher implements Jt1078PublisherInternal {

    // <uuid, Subscriber>
    private final List<FluxSink<Jt1078Subscription>> staticSinks = new ArrayList<>();

    private final Map<String, FluxSink<Jt1078Subscription>> channelSinks = new HashMap<>();

    // <sim, <uid, Subscriber>>
    private final Map<String, Map<String, FluxSink<Jt1078Subscription>>> dynamicSinks = new HashMap<>();

    @Override
    public Flux<Jt1078Subscription> subscribeForEver() {
        return Flux.<Jt1078Subscription>create(fluxSink -> {
            log.info("staticSinks add");
            this.staticSinks.add(fluxSink);
        });
    }

    @Override
    public Flux<Jt1078Subscription> subscribe(Duration timeout) {
        final String id = randomKey();
        return Flux.<Jt1078Subscription>create(fluxSink -> {
            log.info("Subscribe ... {}", id);
            this.channelSinks.put(id, fluxSink);
        }).timeout(timeout).doFinally(sig -> {
            log.info("Subscriber {} removed", id);
            this.channelSinks.remove(id);
        });
    }

    @Override
    public Flux<Jt1078Subscription> subscribe(String sim, short channelNumber, Duration timeout) {
        final String key = key(sim, channelNumber);
        final String uuid = randomKey();

        return Flux.<Jt1078Subscription>create(fluxSink -> {
            log.info("Subscribe ... {}", key);
            dynamicSinks.computeIfAbsent(key, k -> new HashMap<>())
                    .put(uuid, fluxSink);
        }).timeout(timeout).doFinally(signalType -> {
            log.info("Subscriber {} removed", uuid);
            final Map<String, FluxSink<Jt1078Subscription>> map = this.dynamicSinks.get(uuid);
            if (map != null) {
                map.remove(uuid);
            }
        });
    }

    private String key(String sim, short channelNumber) {
        return sim + "_" + channelNumber;
    }

    private String randomKey() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    public void publish(Jt1078Subscription subscription) {
        for (final FluxSink<Jt1078Subscription> staticSink : this.staticSinks) {
            staticSink.next(subscription);
        }

        for (final FluxSink<Jt1078Subscription> sink : this.channelSinks.values()) {
            sink.next(subscription);
        }

        final Jt1078Request request = subscription.getRequest();
        final String key = key(request.sim(), request.channelNumber());
        final Map<String, FluxSink<Jt1078Subscription>> sinkMap = this.dynamicSinks.get(key);
        if (sinkMap != null) {
            for (final FluxSink<Jt1078Subscription> sink : sinkMap.values()) {
                sink.next(subscription);
            }
        }
    }

    @Override
    public void close() {
        for (final FluxSink<Jt1078Subscription> sink : this.channelSinks.values()) {
            sink.complete();
        }
        this.channelSinks.clear();
        for (final Map<String, FluxSink<Jt1078Subscription>> map : this.dynamicSinks.values()) {
            for (final FluxSink<Jt1078Subscription> sink : map.values()) {
                sink.complete();
            }
        }
        this.dynamicSinks.clear();
    }
}
