package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.Jt1078Subscriber;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import reactor.core.publisher.Flux;

public class DefaultJt1078Subscriber<S extends Jt1078Subscription> implements Jt1078Subscriber<S> {
    private final String id;
    private final Flux<S> dataStream;

    public DefaultJt1078Subscriber(String id, Flux<S> dataStream) {
        this.id = id;
        this.dataStream = dataStream;
    }


    @Override
    public String id() {
        return this.id;
    }

    @Override
    public Flux<S> dataStream() {
        return this.dataStream;
    }

}
