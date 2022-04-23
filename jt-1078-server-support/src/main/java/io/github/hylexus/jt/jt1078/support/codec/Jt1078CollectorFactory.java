package io.github.hylexus.jt.jt1078.support.codec;

import io.github.hylexus.jt.jt1078.spec.Jt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriptionType;

import java.util.Optional;

/**
 * @author hylexus
 */
public interface Jt1078CollectorFactory {

    Optional<Jt1078Collector<Jt1078Subscription>> createCollector(Jt1078PayloadType payloadType, Jt1078SubscriptionType subscriptionType);

    default Optional<Jt1078Collector<Jt1078Subscription>> createCollector(Jt1078Collector.ConvertiblePair convertiblePair) {
        return this.createCollector(convertiblePair.getSourceType(), convertiblePair.getTargetType());
    }
}
