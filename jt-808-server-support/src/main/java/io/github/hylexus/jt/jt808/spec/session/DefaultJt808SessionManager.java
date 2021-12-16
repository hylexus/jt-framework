package io.github.hylexus.jt.jt808.spec.session;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

/**
 * Created At 2019-08-24 16:50
 *
 * @author hylexus
 */
@Slf4j
@BuiltinComponent
public class DefaultJt808SessionManager implements Jt808SessionManager {
    private static final Jt808SessionManager instance = new DefaultJt808SessionManager();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    // <terminalId,Session>
    private final Map<String, Jt808Session> sessionMap = new ConcurrentHashMap<>();
    // <sessionId,terminalId>
    private final Map<String, String> sessionIdTerminalIdMapping = new ConcurrentHashMap<>();
    private Jt808SessionManagerEventListener listener = new DefaultJt808SessionManagerEventListener();

    private DefaultJt808SessionManager() {
    }

    public static Jt808SessionManager getInstance() {
        return instance;
    }

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
    public Jt808Session generateSession(String terminalId, Jt808ProtocolVersion version, Channel channel) {
        return buildSession(terminalId, version, channel);
    }

    protected Session buildSession(String terminalId, Jt808ProtocolVersion version, Channel channel) {
        final Session session = new Session();
        session.setChannel(channel);
        session.setId(generateSessionId(channel));
        session.setTerminalId(terminalId);
        session.setLastCommunicateTimestamp(System.currentTimeMillis());
        session.setProtocolVersion(version);
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
    public Jt808Session persistenceIfNecessary(String terminalId, Jt808ProtocolVersion version, Channel channel, boolean updateLastCommunicateTime) {
        final Optional<Jt808Session> session = findByTerminalId(terminalId, updateLastCommunicateTime);
        if (session.isPresent()) {
            Jt808Session oldSession = session.get();
            if (oldSession.getChannel() != channel) {
                log.warn("replace channel for terminal({}), new:{}, old:{}", terminalId, channel.remoteAddress(), oldSession.getChannel().remoteAddress());
                // 单个终端一般来说不会有高并发的问题，这里就不加锁了
                // 有必要的话，可以自己复写该方法或实现自己的SessionManager
                oldSession.setChannel(channel);
            }
            return oldSession;
        }
        Jt808Session newSession = generateSession(terminalId, version, channel);
        persistence(newSession);
        return newSession;
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
        listener.onSessionAdd(session);
    }

    @Override
    public Jt808Session removeBySessionId(String sessionId) {
        lock.writeLock().lock();
        try {
            final String terminalId = sessionIdTerminalIdMapping.remove(sessionId);
            if (terminalId != null) {
                final Jt808Session session = sessionMap.remove(terminalId);
                listener.onSessionRemove(session);
                return session;
            }
        } finally {
            lock.writeLock().unlock();
        }

        return null;
    }

    @Override
    public void removeBySessionIdAndClose(String sessionId, ISessionCloseReason reason) {
        lock.writeLock().lock();
        try {
            final Jt808Session session = this.removeBySessionId(sessionId);
            if (session != null) {
                listener.onSessionClose(session, reason);
                session.getChannel().close();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<Jt808Session> findByTerminalId(String terminalId, boolean updateLastCommunicateTime) {
        Jt808Session session = sessionMap.get(terminalId);
        if (session == null) {
            return Optional.empty();
        }

        if (updateLastCommunicateTime) {
            session.setLastCommunicateTimestamp(System.currentTimeMillis());
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

    @Override
    public Jt808SessionManagerEventListener getEventListener() {
        return this.listener;
    }

    @Override
    public void setEventListener(Jt808SessionManagerEventListener listener) {
        this.listener = listener;
    }
}
