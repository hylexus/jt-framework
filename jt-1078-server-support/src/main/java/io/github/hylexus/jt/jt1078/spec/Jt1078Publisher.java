package io.github.hylexus.jt.jt1078.spec;

import org.reactivestreams.Publisher;

/**
 * @author hylexus
 */
public interface Jt1078Publisher {

    void publish(Jt1078Subscription subscription);

    Publisher<Jt1078Subscription> subscribe();

    void destroy();
}
