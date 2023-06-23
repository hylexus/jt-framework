package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.jt1078.spec.impl.subscription.RawDataJt1078Subscription;
import reactor.core.publisher.FluxSink;

import java.time.LocalDateTime;

public class RawDataSubscriber extends AbstractInternalSubscriber<RawDataJt1078Subscription> {
    public RawDataSubscriber(String id, String sim, short channel, String desc, LocalDateTime createdAt, FluxSink<RawDataJt1078Subscription> sink) {
        super(id, sim, channel, desc, createdAt, sink);
    }
}
