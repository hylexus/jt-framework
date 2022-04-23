package io.github.hylexus.jt.jt1078.spec;

/**
 * @author hylexus
 */
public interface Jt1078Subscription<T> {

    Jt1078SubscriptionType type();

    byte[] payload();

    default T content(){
        return null;
    }

    class RawSubscription implements Jt1078Subscription<byte[]>{

        @Override
        public Jt1078SubscriptionType type() {
            return null;
        }

        @Override
        public byte[] payload() {
            return new byte[0];
        }

        @Override
        public byte[] content() {
            return Jt1078Subscription.super.content();
        }
    }
}
