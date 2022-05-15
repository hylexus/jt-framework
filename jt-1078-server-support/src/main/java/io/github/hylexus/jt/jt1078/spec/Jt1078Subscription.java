package io.github.hylexus.jt.jt1078.spec;

/**
 * @author hylexus
 */
public interface Jt1078Subscription<T> {

    Jt1078SubscriptionType type();

    byte[] payload();

}
