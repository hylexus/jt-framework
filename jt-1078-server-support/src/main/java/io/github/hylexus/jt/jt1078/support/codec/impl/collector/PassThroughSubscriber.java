package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.jt1078.spec.impl.subscription.PassThroughJt1078Subscription;
import reactor.core.publisher.FluxSink;

public class PassThroughSubscriber extends AbstractInternalSubscriber<PassThroughJt1078Subscription> {
    public PassThroughSubscriber(String id, String desc, FluxSink<PassThroughJt1078Subscription> sink) {
        super(id, desc, sink);
    }
}
