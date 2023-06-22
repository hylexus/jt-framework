package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.*;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SubscriberCloseException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import org.apache.commons.lang3.NotImplementedException;

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

    private final Map<Jt1078Subscriber.SubscriberKey, Jt1078ChannelCollector<? extends Jt1078Subscription>> channelCollectorMap = new ConcurrentHashMap<>();

    @Override
    public <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Class<? extends Jt1078ChannelCollector<S>> cls, String sim, short channelNumber, Duration timeout) {
        final Jt1078Subscriber.SubscriberKey subscriberKey = Jt1078Subscriber.SubscriberKey.of(sim, channelNumber);
        final Jt1078ChannelCollector<? extends Jt1078Subscription> channelCollector = this.channelCollectorMap.computeIfAbsent(subscriberKey, k -> createCollector(cls));
        @SuppressWarnings("unchecked") final Jt1078Subscriber<S> subscriber = (Jt1078Subscriber<S>) channelCollector.doSubscribe(sim, channelNumber, timeout);
        return subscriber;
    }

    private final DefaultCollectorFactory collectorFactory = new DefaultCollectorFactory();

    private <S extends Jt1078Subscription> Jt1078ChannelCollector<? extends Jt1078Subscription> createCollector(Class<? extends Jt1078ChannelCollector<S>> cls) {
        return collectorFactory.create(cls);
    }

    @Override
    public void unsubscribe(String id) {
        this.channelCollectorMap.forEach((subscriberKey, channelCollector) -> {
            final String prefix = Jt1078Subscriber.SubscriberKey.prefix(subscriberKey.getSim(), subscriberKey.getChannel());
            if (id.startsWith(prefix)) {
                channelCollector.unsubscribe(id, null);
            }
        });
    }

    @Override
    public void unsubscribeWithSim(String sim, Jt1078SubscriberCloseException reason) {
        this.channelCollectorMap.forEach((subscriberKey, channelCollector) -> {
            if (subscriberKey.getSim().equals(sim)) {
                channelCollector.unsubscribe(reason);
            }
        });
    }

    @Override
    public void publish(Jt1078Request request) {
        final Jt1078Subscriber.SubscriberKey subscriberKey = Jt1078Subscriber.SubscriberKey.of(request);
        final Jt1078ChannelCollector<? extends Jt1078Subscription> channelCollector = this.channelCollectorMap.computeIfAbsent(subscriberKey, k -> collectorFactory.defaultCollector());
        channelCollector.collect(request);
    }

    @Override
    public void close() {
        throw new NotImplementedException();
    }

    @Override
    public Stream<Jt1078SubscriberDescriptor> list() {
        throw new NotImplementedException();
    }
}
