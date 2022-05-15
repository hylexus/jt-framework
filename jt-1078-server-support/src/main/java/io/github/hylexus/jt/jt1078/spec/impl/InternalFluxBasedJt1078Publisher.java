package io.github.hylexus.jt.jt1078.spec.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078Sink;
import io.github.hylexus.jt.jt1078.support.codec.impl.FluxSinkJt1078Sink;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author hylexus
 */
@Slf4j
public class InternalFluxBasedJt1078Publisher implements InternalJt1078Publisher {
    private Jt1078Sink<Jt1078Subscription> delegate;
    private final Flux<Jt1078Subscription> flux;

    public InternalFluxBasedJt1078Publisher(Consumer<Jt1078Sink> onSubscribe, BiConsumer<SignalType, Jt1078Sink> onFinally) {
        this.flux = Flux.<Jt1078Subscription>create(
                        jt1078SubscriptionFluxSink -> {
                            this.delegate = new FluxSinkJt1078Sink<>(jt1078SubscriptionFluxSink);
                            onSubscribe.accept(this);
                        }
                )
                .doFinally(signalType -> {
                    final Jt1078Sink<Jt1078Subscription> instance = this;
                    onFinally.accept(signalType, instance);
                });
    }

    public void complete() {
        if (this.delegate != null) {
            this.delegate.complete();
        }
    }

    @Override
    public void next(Jt1078Subscription subscription) {
        if (this.delegate != null) {
            this.delegate.next(subscription);
        }
    }

    public void error(Throwable throwable) {
        if (this.delegate != null) {
            this.delegate.error(throwable);
        }
    }

    @Override
    public Publisher<Jt1078Subscription> asStreamPublisher() {
        return this.flux;
    }

    @Override
    public void destroy() {
        // TODO ...
    }

    public Jt1078Sink<Jt1078Subscription> getDelegate() {
        return delegate;
    }
}
