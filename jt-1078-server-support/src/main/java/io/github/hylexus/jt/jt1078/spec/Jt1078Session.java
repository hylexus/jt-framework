package io.github.hylexus.jt.jt1078.spec;

import io.netty.channel.Channel;

/**
 * @author hylexus
 */
public interface Jt1078Session {
    String sim();

    String sessionId();

    Channel channel();

    long lastCommunicateTimestamp();

    Jt1078Session lastCommunicateTimestamp(long time);


}
