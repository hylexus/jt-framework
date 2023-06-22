package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SubscriberCloseException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author hylexus
 */
public interface Jt1078Publisher {

    default <S extends Jt1078Subscription> Flux<S> subscribe(Class<? extends Jt1078ChannelCollector<S>> cls, String sim, short channelNumber, Duration timeout) {
        return this.doSubscribe(cls, sim, channelNumber, timeout).dataStream();
    }

    <S extends Jt1078Subscription> Jt1078Subscriber<S> doSubscribe(Class<? extends Jt1078ChannelCollector<S>> cls, String sim, short channelNumber, Duration timeout);

    void unsubscribe(String id);

    default void unsubscribeWithSim(String sim) {
        this.unsubscribeWithSim(sim, null);
    }

    void unsubscribeWithSim(String sim, @Nullable Jt1078SubscriberCloseException reason);
}
