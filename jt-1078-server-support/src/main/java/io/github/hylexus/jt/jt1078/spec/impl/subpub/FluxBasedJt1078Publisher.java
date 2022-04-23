package io.github.hylexus.jt.jt1078.spec.impl.subpub;

import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

/**
 * @author hylexus
 */
@Slf4j
public class FluxBasedJt1078Publisher implements Jt1078Publisher, Publisher<Jt1078Subscription> {
    private FluxSink<Jt1078Subscription> sink;
    private final Publisher<Jt1078Subscription> flux;

    public FluxBasedJt1078Publisher(Consumer<FluxSink<Jt1078Subscription>> consumer) {
        this.flux = Flux.<Jt1078Subscription>create(
                jt1078SubscriptionFluxSink -> {
                    this.sink = jt1078SubscriptionFluxSink;
                    consumer.accept(this.sink);
                }
        );
    }

    @Override
    public void publish(Jt1078Subscription subscription) {
        this.sink.next(subscription);
    }

    public void complete() {
        this.sink.complete();
    }

    public void error(Throwable throwable) {
        this.sink.error(throwable);
    }

    @Override
    public Publisher<Jt1078Subscription> subscribe() {
        return this.flux;
    }

    @Override
    public void destroy() {
        // TODO ...
    }

    @Override
    public void subscribe(Subscriber<? super Jt1078Subscription> s) {
        this.flux.subscribe(s);
    }

}
