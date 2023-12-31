package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.*;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SubscriberCloseException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 默认实现: 将收到的请求直接转发给对应的 {@link Jt1078ChannelCollector}
 *
 * @author hylexus
 * @see Jt1078ChannelCollector
 */
@Slf4j
public class DefaultJt1078Publisher implements Jt1078PublisherInternal {

    private final ReentrantReadWriteLock.ReadLock readLock;
    private final ReentrantReadWriteLock.WriteLock writeLock;

    // <key,<class,classInstance>>
    // <sim_channel,<class,classInstance>>
    private final Map<Jt1078Subscriber.SubscriberKey, Map<Class<? extends Jt1078ChannelCollector<? extends Jt1078Subscription>>, Jt1078ChannelCollector<? extends Jt1078Subscription>>> collectors = new ConcurrentHashMap<>();

    private final Jt1078ChannelCollectorFactory collectorFactory;
    private final Jt1078TerminalIdConverter jt1078TerminalIdConverter;

    public DefaultJt1078Publisher(Jt1078ChannelCollectorFactory collectorFactory, Jt1078TerminalIdConverter jt1078TerminalIdConverter) {
        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
        this.collectorFactory = collectorFactory;
        this.jt1078TerminalIdConverter = jt1078TerminalIdConverter;
    }

    @Override
    public Jt1078TerminalIdConverter terminalIdConverter() {
        return this.jt1078TerminalIdConverter;
    }

    @Override
    public <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Class<? extends Jt1078ChannelCollector<S>> cls, Jt1078SubscriberCreator creator) {
        creator.sim(this.terminalIdConverter().convert(creator.sim()));
        final Jt1078Subscriber.SubscriberKey subscriberKey = Jt1078Subscriber.SubscriberKey.of(creator.sim(), creator.channelNumber());
        final Jt1078ChannelCollector<? extends Jt1078Subscription> channelCollector = this.getOrCreate(subscriberKey, cls, creator);
        @SuppressWarnings("unchecked") final Jt1078Subscriber<S> subscriber = (Jt1078Subscriber<S>) channelCollector.doSubscribe(creator);
        return subscriber;
    }

    private <S extends Jt1078Subscription> Jt1078ChannelCollector<? extends Jt1078Subscription> getOrCreate(Jt1078Subscriber.SubscriberKey key, Class<? extends Jt1078ChannelCollector<S>> cls, Jt1078SubscriberCreator creator) {
        this.writeLock.lock();
        try {
            final Map<Class<? extends Jt1078ChannelCollector<? extends Jt1078Subscription>>, Jt1078ChannelCollector<? extends Jt1078Subscription>> map = this.collectors.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
            return map.computeIfAbsent(cls, k -> collectorFactory.create(cls, creator));
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public void unsubscribe(String id, @Nullable Jt1078SubscriberCloseException reason) {
        this.doUnsubscribe(reason, entry -> {
            final Jt1078Subscriber.SubscriberKey subscriberKey = entry.getKey();
            final String prefix = Jt1078Subscriber.SubscriberKey.prefix(subscriberKey.getSim(), subscriberKey.getChannel());
            return id.startsWith(prefix);
        });
    }

    @Override
    public void unsubscribeWithSim(String sim, Jt1078SubscriberCloseException reason) {
        this.doUnsubscribe(reason, entry -> entry.getKey().getSim().equals(sim));
    }

    @Override
    public void unsubscribeWithSimAndChannelNumber(String sim, short channelNumber, Jt1078SubscriberCloseException reason) {
        this.doUnsubscribe(reason, entry -> {
            final Jt1078Subscriber.SubscriberKey subscriberKey = entry.getKey();
            return subscriberKey.getSim().equals(sim) && subscriberKey.getChannel() == channelNumber;
        });
    }

    private void doUnsubscribe(Jt1078SubscriberCloseException reason, Predicate<Map.Entry<Jt1078Subscriber.SubscriberKey, Map<Class<? extends Jt1078ChannelCollector<? extends Jt1078Subscription>>, Jt1078ChannelCollector<? extends Jt1078Subscription>>>> predicate) {
        this.writeLock.lock();
        try {
            for (final Iterator<Map.Entry<Jt1078Subscriber.SubscriberKey, Map<Class<? extends Jt1078ChannelCollector<? extends Jt1078Subscription>>, Jt1078ChannelCollector<? extends Jt1078Subscription>>>> iterator = this.collectors.entrySet().iterator(); iterator.hasNext(); ) {
                final Map.Entry<Jt1078Subscriber.SubscriberKey, Map<Class<? extends Jt1078ChannelCollector<? extends Jt1078Subscription>>, Jt1078ChannelCollector<? extends Jt1078Subscription>>> entry = iterator.next();
                if (predicate.test(entry)) {
                    iterator.remove();
                    entry.getValue().values().forEach(col -> {
                        try {
                            col.unsubscribe(reason);
                        } catch (Throwable e) {
                            log.error(e.getMessage(), e);
                        } finally {
                            col.close();
                        }
                    });
                }
            }
            // this.collectors.forEach((subscriberKey, channelCollector) -> {
            //     if (subscriberKey.getSim().equals(sim)) {
            //         channelCollector.values().forEach(col -> {
            //             col.unsubscribe(reason);
            //         });
            //     }
            // });
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public void publish(Jt1078Request request) {
        final Jt1078Subscriber.SubscriberKey subscriberKey = Jt1078Subscriber.SubscriberKey.of(request);

        final Map<Class<? extends Jt1078ChannelCollector<? extends Jt1078Subscription>>, Jt1078ChannelCollector<? extends Jt1078Subscription>> map = this.getCollectorsForKey(subscriberKey);

        if (map != null) {
            map.values().forEach(c -> {
                // ...
                c.collect(request);
            });
        }
    }

    private Map<Class<? extends Jt1078ChannelCollector<? extends Jt1078Subscription>>, Jt1078ChannelCollector<? extends Jt1078Subscription>> getCollectorsForKey(Jt1078Subscriber.SubscriberKey subscriberKey) {
        this.readLock.lock();
        try {
            return this.collectors.get(subscriberKey);
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public Stream<Jt1078SubscriberDescriptor> list() {
        this.readLock.lock();
        try {
            return this.collectors.values().stream()
                    .flatMap(map -> map.values().stream().flatMap(Jt1078ChannelCollector::list));
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public long count(Predicate<Jt1078SubscriberDescriptor> predicate) {
        this.readLock.lock();
        try {
            return this.collectors.values().stream()
                    .flatMap(map -> map.values().stream().flatMap(Jt1078ChannelCollector::list))
                    .filter(predicate).count();
        } finally {
            this.readLock.unlock();
        }
    }
}
