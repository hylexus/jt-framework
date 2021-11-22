package io.github.hylexus.jt808.samples.customized.session;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt808.session.*;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created At 2020-06-24 15:09
 *
 * @author hylexus
 */
public class MySessionManager implements Jt808SessionManager {

    private static final MySessionManager instance = new MySessionManager();
    private final Jt808SessionManager sessionManager = SessionManager.getInstance();

    private MySessionManager() {
    }

    public static Jt808SessionManager getInstance() {
        return instance;
    }

    @Override
    public Stream<Jt808Session> list() {
        return sessionManager.list();
    }

    @Override
    public List<Jt808Session> list(int page, int pageSize) {
        return sessionManager.list(page, pageSize);
    }

    @Override
    public List<Jt808Session> list(int page, int pageSize, Predicate<Jt808Session> filter) {
        return sessionManager.list(page, pageSize, filter);
    }

    @Override
    public <T> List<T> list(int page, int pageSize, Predicate<Jt808Session> filter, Comparator<Jt808Session> sorter, Function<Jt808Session, T> converter) {
        return sessionManager.list(page, pageSize, filter, sorter, converter);
    }

    @Override
    public long count() {
        return sessionManager.count();
    }

    @Override
    public String generateSessionId(Channel channel) {
        return sessionManager.generateSessionId(channel);
    }

    @Override
    public Jt808Session generateSession(String terminalId, Jt808ProtocolVersion version, Channel channel) {
        return buildSession(terminalId, version, channel);
    }

    protected MySession buildSession(String terminalId, Jt808ProtocolVersion version, Channel channel) {
        MySession session = new MySession();
        session.setChannel(channel);
        session.setId(generateSessionId(channel));
        session.setTerminalId(terminalId);
        session.setLastCommunicateTimestamp(System.currentTimeMillis());
        session.setProtocolVersion(version);
        Object key = channel.attr(AttributeKey.valueOf("key")).get();
        session.setSomeField(key == null ? "" : key.toString());
        return session;
    }

    @Override
    public Jt808Session persistenceIfNecessary(String terminalId, Jt808ProtocolVersion version, Channel channel) {
        return persistenceIfNecessary(terminalId, version, channel, true);
    }

    @Override
    public Jt808Session persistenceIfNecessary(String terminalId, Jt808ProtocolVersion version, Channel channel, boolean updateLastCommunicateTime) {
        final Optional<Jt808Session> session = findByTerminalId(terminalId, updateLastCommunicateTime);
        if (session.isPresent()) {
            return session.get();
        }
        Jt808Session newSession = generateSession(terminalId, version, channel);
        persistence(newSession);
        return newSession;
    }

    @Override
    public void persistence(Jt808Session session) {
        sessionManager.persistence(session);
    }

    @Override
    @Nullable
    public Jt808Session removeBySessionId(String sessionId) {
        return sessionManager.removeBySessionId(sessionId);
    }

    @Override
    public void removeBySessionIdAndClose(String sessionId, ISessionCloseReason reason) {
        sessionManager.removeBySessionIdAndClose(sessionId, reason);
    }

    @Override
    public Optional<Jt808Session> findBySessionId(String sessionId) {
        return sessionManager.findBySessionId(sessionId);
    }

    @Override
    public Optional<Jt808Session> findByTerminalId(String terminalId, boolean updateLastCommunicateTime) {
        return sessionManager.findByTerminalId(terminalId, updateLastCommunicateTime);
    }

    @Override
    public Optional<Jt808Session> findByTerminalId(String terminalId) {
        return findByTerminalId(terminalId, false);
    }

    @Override
    public void sendBytesToClient(Jt808Session session, byte[] bytes) throws InterruptedException {
        sessionManager.sendBytesToClient(session, bytes);
    }

    @Override
    public Jt808SessionManagerEventListener getEventListener() {
        return sessionManager.getEventListener();
    }

    @Override
    public void setEventListener(Jt808SessionManagerEventListener listener) {
        sessionManager.setEventListener(listener);
    }
}
