package io.github.hylexus.jt.jt1078.support.codec;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriptionSelector;

import java.util.Collection;

/**
 * @author hylexus
 */
public interface Jt1078Collector {

    boolean support(Jt1078Request request);

    boolean support(Jt1078SubscriptionSelector selector);

    void collect(Jt1078Request request);

    void addSink(Jt1078Sink<Jt1078Subscription> sink);

    Collection<Jt1078Sink<Jt1078Subscription>> getSinks();

    void removeSink(Jt1078Sink<Jt1078Subscription> sink);
}
