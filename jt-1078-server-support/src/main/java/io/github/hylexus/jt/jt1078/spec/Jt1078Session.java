package io.github.hylexus.jt.jt1078.spec;

import io.netty.channel.Channel;

/**
 * @author hylexus
 */
public interface Jt1078Session {
    String sim();

    short channelNumber();

    default String terminalId() {
        return this.sim();
    }

    String sessionId();

    Channel channel();

    Jt1078Session channel(Channel channel);

    long lastCommunicateTimestamp();

    Jt1078Session lastCommunicateTimestamp(long time);

    long createdAt();

}
