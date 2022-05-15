package io.github.hylexus.jt.jt1078.spec;

import org.reactivestreams.Publisher;

/**
 * @author hylexus
 */
public interface Jt1078Publisher {

    Publisher<Jt1078Subscription> asStreamPublisher();

    void destroy();
}
