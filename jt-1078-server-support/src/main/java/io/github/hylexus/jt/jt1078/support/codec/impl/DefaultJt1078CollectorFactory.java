package io.github.hylexus.jt.jt1078.support.codec.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriptionSelector;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriptionType;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078SubscriptionType;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078Collector;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078CollectorFactory;
import io.github.hylexus.jt.jt1078.support.codec.impl.collector.BuiltinH264ToFlvCollector;
import io.github.hylexus.jt.jt1078.support.codec.impl.collector.BuiltinRawBytesJt1078Collector;

import java.util.Optional;

/**
 * @author hylexus
 */
public class DefaultJt1078CollectorFactory implements Jt1078CollectorFactory {

    @Override
    public Optional<Jt1078Collector> createCollector(Jt1078SubscriptionSelector selector) {
        final Jt1078SubscriptionType targetType = selector.targetType();
        final Jt1078PayloadType sourceType = selector.sourceType();
        // todo dynamic config ...

        if (sourceType == DefaultJt1078PayloadType.H264 && targetType == DefaultJt1078SubscriptionType.FLV) {
            return Optional.of(new BuiltinH264ToFlvCollector());
        }
        if (targetType == DefaultJt1078SubscriptionType.RAW) {
            return Optional.of(new BuiltinRawBytesJt1078Collector());
        }
        return Optional.empty();
    }

}
