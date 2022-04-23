package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.impl.session.DefaultJt1078Session;
import io.github.hylexus.jt.jt1078.support.exception.Jt1078SessionNotFoundException;
import io.netty.channel.Channel;

import java.util.Optional;

/**
 * @author hylexus
 */
public interface Jt1078SessionManager {

    default Jt1078Session findBySimOrThrow(String sim) {
        return this.findBySim(sim, false).orElseThrow(() -> new Jt1078SessionNotFoundException(sim));
    }

    default Optional<Jt1078Session> findBySim(String sim) {
        return this.findBySim(sim, false);
    }

    Optional<Jt1078Session> findBySim(String sim, boolean updateLastCommunicateTime);

    default String generateSessionId(Channel channel) {
        return channel.id().asLongText();
    }

    default Jt1078Session generateSession(String sim, Channel channel) {

        final String sessionId = this.generateSessionId(channel);

        return DefaultJt1078Session.builder()
                .sessionId(sessionId)
                .sim(sim)
                .channel(channel)
                .lastCommunicateTimestamp(System.currentTimeMillis())
                .build();
    }

    default Jt1078Session persistenceIfNecessary(String sim, Channel channel) {
        return this.persistenceIfNecessary(sim, channel, true);
    }

    default Jt1078Session persistenceIfNecessary(String sim, Channel channel, boolean updateLastCommunicateTime) {
        final Optional<Jt1078Session> session = this.findBySim(sim, updateLastCommunicateTime);
        if (session.isPresent()) {
            return session.get();
        }
        final Jt1078Session newSession = generateSession(sim, channel);
        this.persistence(newSession);
        return newSession;
    }

    void persistence(Jt1078Session session);
}
