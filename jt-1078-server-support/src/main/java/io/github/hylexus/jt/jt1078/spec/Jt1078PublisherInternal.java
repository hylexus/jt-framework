package io.github.hylexus.jt.jt1078.spec;

public interface Jt1078PublisherInternal extends Jt1078Publisher, Jt1078SubscriberManager {

    void publish(Jt1078Request request);

    default void close() {
    }

    @Override
    default void closeSubscriber(String id) {
        this.unsubscribe(id);
    }
}
