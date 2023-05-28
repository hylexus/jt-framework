package io.github.hylexus.jt.jt1078.spec;

import java.util.stream.Stream;

public interface Jt1078PublisherManager {

    Stream<Jt1078SubscriberDescriptor> list();

    default Stream<Jt1078SubscriberDescriptor> list(String sim) {
        return list().filter(it -> it.sim().equals(sim));
    }

    default Stream<Jt1078SubscriberDescriptor> list(String sim, short channel) {
        return this.list(sim).filter(it -> it.channel() == channel);
    }

    void closeSubscriber(String id);
}
