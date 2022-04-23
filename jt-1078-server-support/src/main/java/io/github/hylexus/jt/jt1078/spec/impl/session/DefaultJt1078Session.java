package io.github.hylexus.jt.jt1078.spec.impl.session;

import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078Collector;
import io.netty.channel.Channel;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hylexus
 */
@Accessors(chain = true, fluent = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"channel"})
public class DefaultJt1078Session implements Jt1078Session {
    @Builder.Default
    private Map<Short, Collection<Jt1078Collector>> collectorsMap = new HashMap<>();
    @Getter
    @Setter
    private String sim;

    @Getter
    @Setter
    private String sessionId;

    @Getter
    @Setter
    private Channel channel;

    @Getter
    @Setter
    @Builder.Default
    private long lastCommunicateTimestamp = 0L;

    @Override
    public Collection<Jt1078Collector> collectors(short channelNumber) {
        return collectorsMap.getOrDefault(channelNumber, Collections.EMPTY_LIST);
    }

    @Override
    public Jt1078Session collectors(short channelNumber, Collection<Jt1078Collector> collectors) {
        this.collectorsMap.put(channelNumber, collectors);
        return this;
    }
}
