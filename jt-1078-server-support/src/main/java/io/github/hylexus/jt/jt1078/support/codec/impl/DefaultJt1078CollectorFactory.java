package io.github.hylexus.jt.jt1078.support.codec.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriptionType;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078Collector;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078CollectorFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hylexus
 */
public class DefaultJt1078CollectorFactory implements Jt1078CollectorFactory {

    private final Map<String, Jt1078Collector<Jt1078Subscription>> converters = new HashMap<>();

    // TODO create collector ...
    @Override
    public Optional<Jt1078Collector<Jt1078Subscription>> createCollector(Jt1078PayloadType payloadType, Jt1078SubscriptionType subscriptionType) {
        final Jt1078Collector.NoOpsJt1078Collector collector = new Jt1078Collector.NoOpsJt1078Collector();
        //return Optional.ofNullable(converters.get(generateConvertibleKey(payloadType, subscriptionType)));
        return Optional.of(collector);
    }

}
