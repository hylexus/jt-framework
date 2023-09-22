package io.github.hylexus.jt.jt1078.spec;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Map;

public interface Jt1078SubscriberDescriptor {
    String getId();

    String getSim();

    short getChannel();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime getCreatedAt();

    default String getDesc() {
        return "Jt1078Subscriber";
    }

    Map<String, Object> getMetadata();
}
