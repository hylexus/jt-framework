package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.Jt1078Subscriber;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import reactor.core.publisher.Flux;

public class DefaultJt1078Subscriber implements Jt1078Subscriber {
    private final String id;
    private final Flux<Jt1078Subscription> dataStream;

    public DefaultJt1078Subscriber(String id, Flux<Jt1078Subscription> dataStream) {
        this.id = id;
        this.dataStream = dataStream;
    }


    @Override
    public String id() {
        return this.id;
    }

    @Override
    public Flux<Jt1078Subscription> dataStream() {
        return this.dataStream;
    }

}
