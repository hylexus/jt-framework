package io.github.hylexus.jt.jt1078.spec;

public interface Jt1078SubscriberDescriptor {
    String id();

    String sim();

    short channel();

    default String desc() {
        return "Jt1078Subscriber";
    }
}
