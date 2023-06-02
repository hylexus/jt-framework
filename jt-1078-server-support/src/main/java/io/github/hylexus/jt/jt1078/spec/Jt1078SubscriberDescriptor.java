package io.github.hylexus.jt.jt1078.spec;

import java.time.LocalDateTime;

public interface Jt1078SubscriberDescriptor {
    String getId();

    String getSim();

    short getChannel();

    LocalDateTime getCreatedAt();

    default String getDesc() {
        return "Jt1078Subscriber";
    }
}
