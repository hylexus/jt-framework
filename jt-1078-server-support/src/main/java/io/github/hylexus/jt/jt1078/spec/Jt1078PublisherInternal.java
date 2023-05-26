package io.github.hylexus.jt.jt1078.spec;

public interface Jt1078PublisherInternal extends Jt1078Publisher {

    void publish(Jt1078Subscription subscription);

    void close();
}
