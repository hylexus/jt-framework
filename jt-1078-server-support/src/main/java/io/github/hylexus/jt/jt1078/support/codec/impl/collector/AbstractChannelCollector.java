package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.jt1078.spec.*;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SubscriberCloseException;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.DefaultJt1078Subscriber;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.DefaultJt1078SubscriberDescriptor;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @param <PT>  PayloadType
 * @param <IST> InternalSubscriber
 * @author hylexus
 */
@Slf4j
public abstract class AbstractChannelCollector<PT extends Jt1078Subscription, IST extends InternalSubscriber<PT>>
        implements Jt1078ChannelCollector<PT> {

    protected final ConcurrentMap<String, IST> subscriberMapping = new ConcurrentHashMap<>();

    public AbstractChannelCollector() {
    }

    @Override
    public void collect(Jt1078Request request) {
        this.doCollect(request);
    }

    protected abstract void doCollect(Jt1078Request request);

    protected void forEachSubscriber(Consumer<IST> consumer) {
        for (final IST subscriber : this.subscriberMapping.values()) {
            consumer.accept(subscriber);
        }
    }

    protected abstract IST createSubscribe(String uuid, Jt1078SubscriberCreator creator, FluxSink<PT> fluxSink);

    @Override
    public Jt1078Subscriber<PT> doSubscribe(Jt1078SubscriberCreator creator) {
        final String uuid = Jt1078Subscriber.SubscriberKey.ofUuid(creator.sim(), creator.channelNumber());

        final Flux<PT> dataStream = Flux.<PT>create(fluxSink -> {
            log.info("new subscriber created with id: {}", uuid);
            final IST subscriber = createSubscribe(uuid, creator, fluxSink);
            synchronized (this.subscriberMapping) {
                this.subscriberMapping.put(uuid, subscriber);
            }
        }).timeout(creator.timeout()).doFinally(signalType -> {
            log.info("Subscriber {} removed", uuid);
            synchronized (this.subscriberMapping) {
                this.subscriberMapping.remove(uuid);
            }
        });
        return new DefaultJt1078Subscriber<>(uuid, dataStream);
    }

    @Override
    public void unsubscribe(String id, @Nullable Jt1078SubscriberCloseException reason) {
        final IST subscriber = this.subscriberMapping.get(id);
        if (subscriber != null) {
            if (reason == null) {
                subscriber.sink().complete();
            } else {
                subscriber.sink().error(reason);
            }
        }
    }

    @Override
    public void unsubscribe(@Nullable Jt1078SubscriberCloseException reason) {
        this.subscriberMapping.values().forEach(internalSubscriber -> {
            try {
                if (reason == null) {
                    internalSubscriber.sink().complete();
                } else {
                    internalSubscriber.sink().error(reason);
                }
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    @Override
    public Stream<Jt1078SubscriberDescriptor> list() {
        return this.subscriberMapping.values().stream()
                .map(it -> new DefaultJt1078SubscriberDescriptor(it.id(), it.sim(), it.channel(), it.createdAt(), it.desc(), it.metadata()));
    }

}
