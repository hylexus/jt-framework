package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.*;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SubscriberCloseException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 默认实现: 将收到的请求直接转发给对应的 {@link Jt1078ChannelCollector}
 *
 * @author hylexus
 * @see Jt1078ChannelCollector
 */
public class DefaultJt1078Publisher implements Jt1078PublisherInternal {

    private final Map<Jt1078Subscriber.SubscriberKey, Map<Class<? extends Jt1078ChannelCollector<? extends Jt1078Subscription>>, Jt1078ChannelCollector<? extends Jt1078Subscription>>> collectors = new ConcurrentHashMap<>();

    private final Jt1078ChannelCollectorFactory collectorFactory;
    private final Jt1078TerminalIdConverter jt1078TerminalIdConverter;

    public DefaultJt1078Publisher(Jt1078ChannelCollectorFactory collectorFactory, Jt1078TerminalIdConverter jt1078TerminalIdConverter) {
        this.collectorFactory = collectorFactory;
        this.jt1078TerminalIdConverter = jt1078TerminalIdConverter;
    }

    @Override
    public Jt1078TerminalIdConverter terminalIdConverter() {
        return this.jt1078TerminalIdConverter;
    }

    @Override
    public <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Class<? extends Jt1078ChannelCollector<S>> cls, String sim, short channelNumber, Duration timeout) {
        final Jt1078Subscriber.SubscriberKey subscriberKey = Jt1078Subscriber.SubscriberKey.of(sim, channelNumber);
        final Jt1078ChannelCollector<? extends Jt1078Subscription> channelCollector = getOrCreate(subscriberKey, cls);
        @SuppressWarnings("unchecked") final Jt1078Subscriber<S> subscriber = (Jt1078Subscriber<S>) channelCollector.doSubscribe(sim, channelNumber, timeout);
        return subscriber;
    }

    private <S extends Jt1078Subscription> Jt1078ChannelCollector<? extends Jt1078Subscription> getOrCreate(Jt1078Subscriber.SubscriberKey key, Class<? extends Jt1078ChannelCollector<S>> cls) {
        final Map<Class<? extends Jt1078ChannelCollector<? extends Jt1078Subscription>>, Jt1078ChannelCollector<? extends Jt1078Subscription>> map = this.collectors.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
        return map.computeIfAbsent(cls, k -> collectorFactory.create(cls));
    }

    @Override
    public void unsubscribe(String id, @Nullable Jt1078SubscriberCloseException reason) {
        this.collectors.forEach((subscriberKey, map) -> {
            final String prefix = Jt1078Subscriber.SubscriberKey.prefix(subscriberKey.getSim(), subscriberKey.getChannel());
            map.values().forEach(jt1078ChannelCollector -> {
                if (id.startsWith(prefix)) {
                    jt1078ChannelCollector.unsubscribe(id, reason);
                }
            });
        });
    }

    @Override
    public void unsubscribeWithSim(String sim, Jt1078SubscriberCloseException reason) {
        this.collectors.forEach((subscriberKey, channelCollector) -> {
            if (subscriberKey.getSim().equals(sim)) {
                channelCollector.values().forEach(col -> {
                    col.unsubscribe(reason);
                });
            }
        });
    }

    @Override
    public void publish(Jt1078Request request) {
        final Jt1078Subscriber.SubscriberKey subscriberKey = Jt1078Subscriber.SubscriberKey.of(request);
        final Map<Class<? extends Jt1078ChannelCollector<? extends Jt1078Subscription>>, Jt1078ChannelCollector<? extends Jt1078Subscription>> map = this.collectors.get(subscriberKey);

        if (map != null) {
            map.values().forEach(c -> {
                c.collect(request);
            });
        }
    }

    private Jt1078ChannelCollector<? extends Jt1078Subscription> defaultCollector(Jt1078Request request) {
        return collectorFactory.defaultCollector(request);
    }

    @Override
    public Stream<Jt1078SubscriberDescriptor> list() {
        return this.collectors.values().stream()
                .flatMap(map -> map.values().stream().flatMap(Jt1078ChannelCollector::list));
    }
}
