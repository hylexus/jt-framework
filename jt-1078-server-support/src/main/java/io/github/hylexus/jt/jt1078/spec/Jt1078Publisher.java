package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SubscriberCloseException;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.RawJt1078Subscription;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author hylexus
 */
public interface Jt1078Publisher {

    /**
     * @see Jt1078SubscriptionCollector#RAW
     */
    default Flux<RawJt1078Subscription> subscribe() {
        return this.doSubscribe(Jt1078SubscriptionCollector.RAW).dataStream();
    }

    default <S extends Jt1078Subscription> Flux<S> subscribe(Jt1078SubscriptionCollector<S> collector) {
        return this.doSubscribe(collector).dataStream();
    }

    /**
     * @see Jt1078SubscriptionCollector#RAW
     */
    default Flux<RawJt1078Subscription> subscribe(Duration timeout) {
        return this.doSubscribe(Jt1078SubscriptionCollector.RAW, timeout).dataStream();
    }

    default <S extends Jt1078Subscription> Flux<S> subscribe(Jt1078SubscriptionCollector<S> collector, Duration timeout) {
        return this.doSubscribe(collector, timeout).dataStream();
    }

    /**
     * @see Jt1078SubscriptionCollector#RAW
     */
    default Flux<RawJt1078Subscription> subscribe(String sim, short channelNumber, Duration timeout) {
        return this.doSubscribe(Jt1078SubscriptionCollector.RAW, sim, channelNumber, timeout).dataStream();
    }

    default <S extends Jt1078Subscription> Flux<S> subscribe(Jt1078SubscriptionCollector<S> collector, String sim, short channelNumber, Duration timeout) {
        return this.doSubscribe(collector, sim, channelNumber, timeout).dataStream();
    }

    <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Jt1078SubscriptionCollector<S> collector);

    <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Jt1078SubscriptionCollector<S> collector, Duration timeout);

    <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Jt1078SubscriptionCollector<S> collector, String sim, short channelNumber, Duration timeout);

    void unsubscribe(String id);

    default void unsubscribeWithSim(String sim) {
        this.unsubscribeWithSim(sim, null);
    }

    void unsubscribeWithSim(String sim, @Nullable Jt1078SubscriberCloseException reason);
}
