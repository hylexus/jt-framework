package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.jt1078.spec.impl.subscription.ByteArrayJt1078Subscription;
import reactor.core.publisher.FluxSink;

public class DefaultByteArraySubscriber extends AbstractInternalSubscriber<ByteArrayJt1078Subscription> {

    public DefaultByteArraySubscriber(String id, String desc, FluxSink<ByteArrayJt1078Subscription> sink) {
        super(id, desc, sink);
    }
}
