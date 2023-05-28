package io.github.hylexus.jt.jt1078.spec;

import reactor.core.publisher.Flux;

public interface Jt1078Subscriber {

    String id();

    Flux<Jt1078Subscription> dataStream();

}
