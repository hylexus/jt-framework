package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.impl.session.DefaultJt1078Session;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author hylexus
 */
public interface Jt1078SessionManager {

    Jt1078TerminalIdConverter terminalIdConverter();

    AttributeKey<Jt1078Session> ATTR_KEY_SESSION = AttributeKey.newInstance("jt1078.session");
    Logger LOGGER = LoggerFactory.getLogger(Jt1078SessionManager.class);

    Optional<Jt1078Session> findBySessionId(String sessionId);

    default Optional<Jt1078Session> findBySimAndChannel(String sim, short channelNumber) {
        return this.findBySimAndChannel(sim, channelNumber, false);
    }

    Optional<Jt1078Session> findBySimAndChannel(String sim, short channelNumber, boolean updateLastCommunicateTime);

    default String generateSessionId(String sim, short channelNumber) {
        return sim + "_" + channelNumber;
    }

    default Jt1078Session generateSession(Jt1078Request request, Channel channel) {

        final String sessionId = this.generateSessionId(request.sim(), request.channelNumber());

        return DefaultJt1078Session.builder()
                .sessionId(sessionId)
                .sim(request.sim())
                .channelNumber(request.channelNumber())
                .channel(channel)
                .lastCommunicateTimestamp(System.currentTimeMillis())
                .build();
    }

    default Jt1078Session persistenceIfNecessary(Jt1078Request request, Channel channel) {
        return this.persistenceIfNecessary(request, channel, true);
    }

    default Jt1078Session persistenceIfNecessary(Jt1078Request request, Channel channel, boolean updateLastCommunicateTime) {
        final Optional<Jt1078Session> session = this.findBySimAndChannel(request.sim(), request.channelNumber(), updateLastCommunicateTime);
        if (session.isPresent()) {
            final Jt1078Session oldSession = session.get();
            if (oldSession.channel() != channel) {
                LOGGER.warn("replace channel for sim({}), channelNumber:{}, new:{}, old:{}", request.sim(), request.channelNumber(), channel.remoteAddress(), oldSession.channel().remoteAddress());
                try {
                    oldSession.channel().close().sync();
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                oldSession.channel(channel);
            }
            return oldSession;
        }
        final Jt1078Session newSession = generateSession(request, channel);
        if (!channel.hasAttr(ATTR_KEY_SESSION)) {
            channel.attr(ATTR_KEY_SESSION).set(newSession);
        }
        this.persistence(newSession);
        return newSession;
    }

    void persistence(Jt1078Session session);

    Optional<Jt1078Session> removeBySessionId(String sessionId);

    void removeBySimAndThenClose(String sim, Jt1078SessionCloseReason reason);

    Optional<Jt1078Session> removeBySessionIdAndThenClose(String sessionId, Jt1078SessionCloseReason reason);

    default Optional<Jt1078Session> removeBySimAndChannelAndThenClose(String sim, short channelNumber, Jt1078SessionCloseReason closeReason) {
        final Optional<Jt1078Session> optionalSession = this.findBySimAndChannel(sim, channelNumber, false);
        optionalSession.ifPresent(session -> {
            //
            this.removeBySessionIdAndThenClose(session.sessionId(), closeReason);
        });
        return optionalSession;
    }

    Jt1078SessionManager addListener(Jt1078SessionEventListener listener);

    List<Jt1078SessionEventListener> getListeners();

    Stream<Jt1078Session> list();

    default long count() {
        return this.count(session -> true);
    }

    long count(Predicate<Jt1078Session> filter);

}
