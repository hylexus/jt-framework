package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
public class DefaultJt1078Publisher implements Jt1078PublisherInternal {

    private static class Key {
        private final String sim;
        private final short channel;

        static Key of(String sim, short channel) {
            return new Key(sim, channel);
        }

        static Key ofGlobal() {
            return of("placeholder", (short) -1);
        }

        private Key(String sim, short channel) {
            this.sim = sim;
            this.channel = channel;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Key key = (Key) o;
            return channel == key.channel && Objects.equals(sim, key.sim);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sim, channel);
        }

        @Override
        public String toString() {
            return "Key{"
                    + "sim='" + sim + '\''
                    + ", channel=" + channel
                    + '}';
        }
    }

    // <uuid, Subscriber>
    private final Map<String, FluxSink<Jt1078Subscription>> staticSinks = new HashMap<>();

    // <sim_channel, <uuid, Subscriber>>
    private final Map<Key, Map<String, FluxSink<Jt1078Subscription>>> dynamicSinks = new HashMap<>();

    public DefaultJt1078Publisher() {
        this.dynamicSinks.put(Key.ofGlobal(), this.staticSinks);
    }

    @Override
    public Jt1078Subscriber doSubscribe() {
        final String uuid = randomKey();
        final Flux<Jt1078Subscription> dataStream = Flux.<Jt1078Subscription>create(fluxSink -> {
            log.info("staticSinks add");
            this.staticSinks.put(uuid, fluxSink);
        }).doFinally(signalType -> {
            this.staticSinks.remove(uuid);
        });
        return new DefaultJt1078Subscriber(uuid, dataStream);
    }

    @Override
    public Jt1078Subscriber doSubscribe(Duration timeout) {
        final String id = randomKey();
        final Flux<Jt1078Subscription> dataStream = Flux.<Jt1078Subscription>create(fluxSink -> {
            log.info("Subscribe ... {}", id);
            this.staticSinks.put(id, fluxSink);
        }).timeout(timeout).doFinally(sig -> {
            log.info("Subscriber {} removed", id);
            this.staticSinks.remove(id);
        });

        return new DefaultJt1078Subscriber(id, dataStream);
    }

    @Override
    public Jt1078Subscriber doSubscribe(String sim, short channelNumber, Duration timeout) {
        final Key key = Key.of(sim, channelNumber);
        final String uuid = randomKey();

        final Flux<Jt1078Subscription> dataStream = Flux.<Jt1078Subscription>create(fluxSink -> {
            log.info("Subscribe ... {}", key);
            dynamicSinks.computeIfAbsent(key, k -> new HashMap<>())
                    .put(uuid, fluxSink);
        }).timeout(timeout).doFinally(signalType -> {
            log.info("Subscriber {} removed", uuid);
            final Map<String, FluxSink<Jt1078Subscription>> map = this.dynamicSinks.get(key);
            if (map != null) {
                map.remove(uuid);
            }
        });

        return new DefaultJt1078Subscriber(uuid, dataStream);
    }

    @Override
    public void unsubscribe(String id) {
        final FluxSink<Jt1078Subscription> subscriber = this.staticSinks.remove(id);
        if (subscriber != null) {
            subscriber.complete();
        }

        for (final Map<String, FluxSink<Jt1078Subscription>> map : this.dynamicSinks.values()) {
            final FluxSink<Jt1078Subscription> sink = map.remove(id);
            if (sink != null) {
                sink.complete();
            }
        }
    }

    @Override
    public void publish(Jt1078Subscription subscription) {

        for (final FluxSink<Jt1078Subscription> sink : this.staticSinks.values()) {
            sink.next(subscription);
        }

        final Jt1078Request request = subscription.getRequest();
        final Key key = Key.of(request.sim(), request.channelNumber());
        final Map<String, FluxSink<Jt1078Subscription>> sinkMap = this.dynamicSinks.get(key);
        if (sinkMap != null) {
            for (final FluxSink<Jt1078Subscription> sink : sinkMap.values()) {
                sink.next(subscription);
            }
        }
    }

    @Override
    public void close() {
        for (final FluxSink<Jt1078Subscription> sink : this.staticSinks.values()) {
            sink.complete();
        }
        this.staticSinks.clear();

        for (final Map<String, FluxSink<Jt1078Subscription>> map : this.dynamicSinks.values()) {
            for (final FluxSink<Jt1078Subscription> sink : map.values()) {
                sink.complete();
            }
        }
        this.dynamicSinks.clear();
    }

    @Override
    public Stream<Jt1078SubscriberDescriptor> list() {

        return this.dynamicSinks.entrySet().stream().flatMap(it -> {
            final Key key = it.getKey();
            final Map<String, FluxSink<Jt1078Subscription>> map = it.getValue();
            return map.keySet().stream()
                    .map(id -> new DefaultJt1078SubscriberDescriptor(id, key.sim, key.channel, null));
        });
    }

    @Override
    public void closeSubscriber(String id) {
        this.unsubscribe(id);
    }

    private String randomKey() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
