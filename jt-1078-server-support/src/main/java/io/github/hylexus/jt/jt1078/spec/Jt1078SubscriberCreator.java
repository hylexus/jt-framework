package io.github.hylexus.jt.jt1078.spec;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Jt1078SubscriberCreator {
    private String sim;
    private short channelNumber;
    private Duration timeout;
    private String desc;
    private Map<String, Object> metadata;
}
