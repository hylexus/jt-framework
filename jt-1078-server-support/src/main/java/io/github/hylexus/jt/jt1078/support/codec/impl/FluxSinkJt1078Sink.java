package io.github.hylexus.jt.jt1078.support.codec.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078Sink;
import reactor.core.publisher.FluxSink;

public class FluxSinkJt1078Sink<T extends Jt1078Subscription> implements Jt1078Sink<T> {
    private final FluxSink<T> fluxSink;

    public FluxSinkJt1078Sink(FluxSink<T> fluxSink) {
        this.fluxSink = fluxSink;
    }

    @Override
    public void next(T subscription) {
        this.fluxSink.next(subscription);
    }

    @Override
    public void error(Throwable error) {
        this.fluxSink.error(error);
    }

    @Override
    public void complete() {
        this.fluxSink.complete();
    }
}
