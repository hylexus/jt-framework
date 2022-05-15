package io.github.hylexus.jt.jt1078.spec.impl.session;

import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078Collector;
import io.netty.channel.Channel;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.*;

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
    private Map<Short, Collection<Jt1078Collector>> converters = new HashMap<>();

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
    public Collection<Jt1078Collector> getChannelConverters(short channelNumber) {
        return this.converters.getOrDefault(channelNumber, Collections.emptyList());
    }

    @Override
    public synchronized Jt1078Session addChannelConverter(short channelNumber, Jt1078Collector collector) {
        final Collection<Jt1078Collector> collection = this.converters.computeIfAbsent(channelNumber, cn -> new ArrayList<>());
        collection.add(collector);
        return this;
    }
}
