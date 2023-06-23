package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.jt1078.spec.impl.subscription.ByteArrayJt1078Subscription;
import reactor.core.publisher.FluxSink;

import java.time.LocalDateTime;

public class DefaultByteArraySubscriber extends AbstractInternalSubscriber<ByteArrayJt1078Subscription> {

    public DefaultByteArraySubscriber(String id, String sim, short channel, String desc, LocalDateTime createdAt, FluxSink<ByteArrayJt1078Subscription> sink) {
        super(id, sim, channel, desc, createdAt, sink);
    }
}
