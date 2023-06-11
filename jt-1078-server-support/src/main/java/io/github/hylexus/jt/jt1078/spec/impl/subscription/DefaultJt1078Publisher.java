package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.*;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SubscriberCloseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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

        static String ofGlobalUuid() {
            return ofUuid("placeholder", (short) -1);
        }

        static String ofUuid(String sim, short channel) {
            return sim + "_" + channel + "_" + UUID.randomUUID().toString().replaceAll("-", "");
        }

        private Key(String sim, short channel) {
            this.sim = sim;
            this.channel = channel;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
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

    private static class InternalSubscriber {
        private final FluxSink<Jt1078Subscription> sink;
        private final LocalDateTime createdAt;
        private final Jt1078SubscriptionCollector<Jt1078Subscription> collector;
        private final String desc;

        private InternalSubscriber(FluxSink<Jt1078Subscription> sink, LocalDateTime createdAt, Jt1078SubscriptionCollector<Jt1078Subscription> collector, String desc) {
            this.sink = sink;
            this.createdAt = createdAt;
            this.collector = collector;
            this.desc = desc;
        }
    }

    // <uuid, Subscriber>
    private final ConcurrentMap<String, InternalSubscriber> staticSinks = new ConcurrentHashMap<>();

    // <sim_channel, <uuid, Subscriber>>
    private final Map<Key, ConcurrentMap<String, InternalSubscriber>> dynamicSinks = new ConcurrentHashMap<>();

    public DefaultJt1078Publisher() {
        this.dynamicSinks.put(Key.ofGlobal(), this.staticSinks);
    }

    @Override
    public <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Jt1078SubscriptionCollector<S> converter) {
        final String uuid = Key.ofGlobalUuid();
        final Flux<S> dataStream = Flux.<Jt1078Subscription>create(fluxSink -> {
            log.info("staticSinks add");
            synchronized (this.staticSinks) {
                @SuppressWarnings("unchecked") final Jt1078SubscriptionCollector<Jt1078Subscription> c = (Jt1078SubscriptionCollector<Jt1078Subscription>) converter;
                this.staticSinks.put(uuid, new InternalSubscriber(fluxSink, LocalDateTime.now(), c, "GlobalSubscriber"));
            }
        }).map(it -> {
            @SuppressWarnings("unchecked") S r = (S) it;
            return r;
        }).doFinally(signalType -> {
            log.info("Subscriber {} removed", uuid);
            synchronized (this.staticSinks) {
                this.staticSinks.remove(uuid);
            }
        });
        return new DefaultJt1078Subscriber<>(uuid, dataStream);
    }

    @Override
    public <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Jt1078SubscriptionCollector<S> converter, Duration timeout) {
        final String id = Key.ofGlobalUuid();
        final Flux<S> dataStream = Flux.<Jt1078Subscription>create(fluxSink -> {
            log.info("Subscribe ... {}", id);
            synchronized (this.staticSinks) {
                @SuppressWarnings("unchecked") final Jt1078SubscriptionCollector<Jt1078Subscription> c = (Jt1078SubscriptionCollector<Jt1078Subscription>) converter;
                this.staticSinks.put(id, new InternalSubscriber(fluxSink, LocalDateTime.now(), c, "GlobalSubscriber"));
            }
        }).map(it -> {
            @SuppressWarnings("unchecked") S r = (S) it;
            return r;
        }).timeout(timeout).doFinally(sig -> {
            log.info("Subscriber {} removed", id);
            synchronized (this.staticSinks) {
                this.staticSinks.remove(id);
            }
        });

        return new DefaultJt1078Subscriber<>(id, dataStream);
    }

    @Override
    public <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Jt1078SubscriptionCollector<S> converter, String sim, short channelNumber, Duration timeout) {
        final Key key = Key.of(sim, channelNumber);
        final String uuid = Key.ofUuid(sim, channelNumber);

        final Flux<S> dataStream = Flux.<Jt1078Subscription>create(fluxSink -> {
            log.info("Subscribe ... {}", key);
            synchronized (this.dynamicSinks) {
                @SuppressWarnings("unchecked") final Jt1078SubscriptionCollector<Jt1078Subscription> c = (Jt1078SubscriptionCollector<Jt1078Subscription>) converter;
                this.dynamicSinks.computeIfAbsent(key, k -> new ConcurrentHashMap<>())
                        .put(uuid, new InternalSubscriber(fluxSink, LocalDateTime.now(), c, "DynamicSubscriber(sim=" + sim + ",channel=" + channelNumber + ")"));
            }
        }).map(it -> {
            @SuppressWarnings("unchecked") S r = (S) it;
            return r;
        }).timeout(timeout).doFinally(signalType -> {
            log.info("Subscriber {} removed", uuid);
            synchronized (this.dynamicSinks) {
                final ConcurrentMap<String, InternalSubscriber> map = this.dynamicSinks.get(key);
                if (map != null) {
                    map.remove(uuid);
                }
            }
        });

        return new DefaultJt1078Subscriber<>(uuid, dataStream);
    }

    @Override
    public void unsubscribe(String id) {
        final InternalSubscriber subscriber = this.staticSinks.get(id);
        if (subscriber != null) {
            subscriber.sink.complete();
        }

        for (final ConcurrentMap<String, InternalSubscriber> map : this.dynamicSinks.values()) {
            final InternalSubscriber internalSubscriber = map.get(id);
            if (internalSubscriber != null) {
                internalSubscriber.sink.complete();
            }
        }
    }

    @Override
    public void unsubscribeWithSim(String sim, @Nullable Jt1078SubscriberCloseException reason) {
        this.dynamicSinks.forEach(((key, stringInternalSubscriberMap) -> {
            if (key.sim.equals(sim)) {
                stringInternalSubscriberMap.values().forEach((internalSubscriber -> {
                    try {
                        if (reason == null) {
                            internalSubscriber.sink.complete();
                        } else {
                            internalSubscriber.sink.error(reason);
                        }
                    } catch (Throwable e) {
                        log.error(e.getMessage(), e);
                    }
                }));
            }
        }));

    }

    @Override
    public void publish(Jt1078Request request) {
        try {
            for (final InternalSubscriber internalSubscriber : this.staticSinks.values()) {
                internalSubscriber.collector.collect(request).ifPresent(internalSubscriber.sink::next);
            }

            final Key key = Key.of(request.sim(), request.channelNumber());
            final ConcurrentMap<String, InternalSubscriber> sinkMap = this.dynamicSinks.get(key);
            if (sinkMap != null) {
                for (final InternalSubscriber subscriber : sinkMap.values()) {
                    subscriber.collector.collect(request).ifPresent(subscriber.sink::next);
                }
            }
        } finally {
            request.release();
        }
    }

    @Override
    public void close() {
        for (final InternalSubscriber internalSubscriber : this.staticSinks.values()) {
            internalSubscriber.sink.complete();
        }
        this.staticSinks.clear();

        for (final ConcurrentMap<String, InternalSubscriber> map : this.dynamicSinks.values()) {
            for (final InternalSubscriber subscriber : map.values()) {
                subscriber.sink.complete();
            }
        }
        this.dynamicSinks.clear();
    }

    @Override
    public Stream<Jt1078SubscriberDescriptor> list() {

        return this.dynamicSinks.entrySet().stream().flatMap(it -> {
            final Key key = it.getKey();
            final Map<String, InternalSubscriber> map = it.getValue();
            return map.entrySet().stream()
                    .map(id -> {
                        final InternalSubscriber value = id.getValue();
                        return new DefaultJt1078SubscriberDescriptor(id.getKey(), key.sim, key.channel, value.createdAt, value.desc);
                    });
        });
    }

    @Override
    public void closeSubscriber(String id) {
        this.unsubscribe(id);
    }

}
