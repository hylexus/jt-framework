package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.jt1078.spec.impl.subscription.ByteArrayJt1078Subscription;
import reactor.core.publisher.FluxSink;

import java.time.LocalDateTime;
import java.util.Map;

public class DefaultByteArraySubscriber extends AbstractInternalSubscriber<ByteArrayJt1078Subscription> {

    public DefaultByteArraySubscriber(String id, String sim, short channel, String desc, LocalDateTime createdAt, Map<String, Object> metadata, FluxSink<ByteArrayJt1078Subscription> sink) {
        super(id, sim, channel, desc, createdAt, sink, metadata);
    }
}
