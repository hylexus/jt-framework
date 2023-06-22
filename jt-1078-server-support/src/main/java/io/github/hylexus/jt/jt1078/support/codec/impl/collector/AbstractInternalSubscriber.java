package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import reactor.core.publisher.FluxSink;

public class AbstractInternalSubscriber<S extends Jt1078Subscription>
        implements InternalSubscriber<S> {

    private final String id;

    private final String desc;

    private final FluxSink<S> sink;

    public AbstractInternalSubscriber(String id, String desc, FluxSink<S> sink) {
        this.id = id;
        this.desc = desc;
        this.sink = sink;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public String desc() {
        return this.desc;
    }

    @Override
    public FluxSink<S> sink() {
        return this.sink;
    }
}
