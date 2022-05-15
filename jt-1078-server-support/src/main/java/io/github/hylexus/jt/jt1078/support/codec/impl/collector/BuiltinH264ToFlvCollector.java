package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriptionSelector;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078SubscriptionType;

/**
 * @author hylexus
 */
public class BuiltinH264ToFlvCollector extends AbstractJt1078Collector {

    @Override
    public boolean support(final Jt1078Request request) {
        return request.payloadType() == DefaultJt1078PayloadType.H264;
    }

    @Override
    public boolean support(final Jt1078SubscriptionSelector selector) {
        return selector.sourceType() == DefaultJt1078PayloadType.H264
                && selector.targetType() == DefaultJt1078SubscriptionType.FLV;
    }

    @Override
    public void collect(final Jt1078Request request) {
        // todo ...
    }
}
