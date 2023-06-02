package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.impl.session.DefaultJt1078Session;
import io.github.hylexus.jt.jt1078.support.exception.Jt1078SessionNotFoundException;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * @author hylexus
 */
public interface Jt1078SessionManager {
    Logger LOGGER = LoggerFactory.getLogger(Jt1078SessionManager.class);

    default Jt1078Session findBySimOrThrow(String sim) {
        return this.findBySim(sim, false).orElseThrow(() -> new Jt1078SessionNotFoundException(sim));
    }

    Optional<Jt1078Session> findBySessionId(String sessionId);

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
            final Jt1078Session oldSession = session.get();
            if (oldSession.channel() != channel) {
                LOGGER.warn("replace channel for sim({}), new:{}, old:{}", sim, channel.remoteAddress(), oldSession.channel().remoteAddress());
                oldSession.channel(channel);
            }
            return oldSession;
        }
        final Jt1078Session newSession = generateSession(sim, channel);
        this.persistence(newSession);
        return newSession;
    }

    void persistence(Jt1078Session session);

    Jt1078Session removeBySessionId(String sessionId);

    void removeBySessionIdAndClose(String sessionId, Jt1078SessionCloseReason reason);

    Jt1078SessionManager addListener(Jt1078SessionEventListener listener);

    List<Jt1078SessionEventListener> getListeners();
}
