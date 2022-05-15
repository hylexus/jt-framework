package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.support.codec.Jt1078Collector;
import io.netty.channel.Channel;

import java.util.Collection;

/**
 * @author hylexus
 */
public interface Jt1078Session {
    String sim();

    String sessionId();

    Channel channel();

    long lastCommunicateTimestamp();

    Jt1078Session lastCommunicateTimestamp(long time);

    Collection<Jt1078Collector> getChannelConverters(short channelNumber);

    Jt1078Session addChannelConverter(short channelNumber, Jt1078Collector collector);
}
