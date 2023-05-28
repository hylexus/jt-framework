package io.github.hylexus.jt.jt1078.spec;

public interface Jt1078PublisherInternal extends Jt1078Publisher, Jt1078PublisherManager {

    void publish(Jt1078Subscription subscription);

    void close();
}
