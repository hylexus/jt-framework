package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.annotation.Internal;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import reactor.core.publisher.FluxSink;

@Internal
public interface InternalSubscriber<S extends Jt1078Subscription> {
    String id();

    String desc();

    FluxSink<S> sink();

}
