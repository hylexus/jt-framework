package io.github.hylexus.jt808.session;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

/**
 * @author hylexus
 * Created At 2019-08-24 16:50
 */
@Slf4j
@BuiltinComponent
public class SessionManager implements Jt808SessionManager {
    private static final Jt808SessionManager instance = new SessionManager();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private SessionManager() {
    }

    public static Jt808SessionManager getInstance() {
        return instance;
    }

    // <terminalId,Session>
    private final Map<String, Jt808Session> sessionMap = new ConcurrentHashMap<>();
    // <sessionId,terminalId>
    private final Map<String, String> sessionIdTerminalIdMapping = new ConcurrentHashMap<>();

    public Stream<Jt808Session> list() {
        this.lock.readLock().lock();
        try {
            return sessionMap.values().stream();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public long count() {
        return sessionMap.size();
    }

    @Override
    public Session generateSession(Channel channel, String terminalId) {
        return buildSession(channel, terminalId);
    }

    protected Session buildSession(Channel channel, String terminalId) {
        Session session = new Session();
        session.setChannel(channel);
        session.setId(generateSessionId(channel));
        session.setTerminalId(terminalId);
        session.setLastCommunicateTimeStamp(System.currentTimeMillis());
        return session;
    }

    @Override
    public Optional<Jt808Session> findBySessionId(String sessionId) {
        String terminalId = sessionIdTerminalIdMapping.get(sessionId);
        if (StringUtils.isEmpty(terminalId)) {
            return Optional.empty();
        }
        return findByTerminalId(terminalId, false);
    }

    @Override
    public void persistence(Jt808Session session) {
        lock.writeLock().lock();
        try {
            this.sessionMap.put(session.getTerminalId(), session);
            sessionIdTerminalIdMapping.put(session.getId(), session.getTerminalId());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeBySessionIdAndClose(String sessionId, ISessionCloseReason reason) {
        lock.writeLock().lock();
        try {
            final String terminalId = sessionIdTerminalIdMapping.remove(sessionId);
            if (terminalId != null) {
                sessionMap.remove(terminalId);
            }
        } finally {
            lock.writeLock().unlock();
        }
        log.info("session removed [{}] , sessionId = {}", reason, sessionId);
    }

    @Override
    public Optional<Jt808Session> findByTerminalId(String terminalId, boolean updateLastCommunicateTime) {
        Jt808Session session = sessionMap.get(terminalId);
        if (session == null) {
            return Optional.empty();
        }

        if (updateLastCommunicateTime) {
            session.setLastCommunicateTimeStamp(System.currentTimeMillis());
        }

        if (!this.checkStatus(session)) {
            return Optional.empty();
        }

        return Optional.of(session);
    }

    private boolean checkStatus(Jt808Session session) {
        //if (!session.getChannel().isOpen()) {
        if (!session.getChannel().isActive()) {
            this.removeBySessionIdAndClose(session.getId(), SessionCloseReason.CHANNEL_INACTIVE);
            return false;
        }
        return true;
    }

}
