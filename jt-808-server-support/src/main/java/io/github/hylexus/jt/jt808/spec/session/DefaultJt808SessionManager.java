package io.github.hylexus.jt.jt808.spec.session;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created At 2019-08-24 16:50
 *
 * @author hylexus
 */
@Slf4j
@BuiltinComponent
public class DefaultJt808SessionManager implements Jt808SessionManager {
    private final Jt808FlowIdGeneratorFactory flowIdGeneratorFactory;

    private static volatile Jt808SessionManager INSTANCE;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    // <terminalId,Session>
    private final Map<String, Jt808Session> sessionMap = new ConcurrentHashMap<>();
    // <sessionId,terminalId>
    private final Map<String, String> sessionIdTerminalIdMapping = new ConcurrentHashMap<>();

    private final List<Jt808SessionEventListener> listeners = new ArrayList<>();

    protected DefaultJt808SessionManager(Jt808FlowIdGeneratorFactory flowIdGeneratorFactory) {
        this.flowIdGeneratorFactory = flowIdGeneratorFactory;
    }

    public static Jt808SessionManager getInstance(Jt808FlowIdGeneratorFactory factory) {
        if (INSTANCE == null) {
            synchronized (DefaultJt808SessionManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DefaultJt808SessionManager(factory);
                }
            }
        }
        return INSTANCE;
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
    public long count(Predicate<Jt808Session> filter) {
        return this.sessionMap.values().stream().filter(filter).count();
    }

    @Override
    public Jt808Session generateSession(String terminalId, Jt808ProtocolVersion version, Channel channel) {
        final Jt808FlowIdGenerator flowIdGenerator = this.flowIdGeneratorFactory.create();
        return buildSession(terminalId, version, channel, flowIdGenerator);
    }

    @Override
    public Jt808Session generateSession(String terminalId, Jt808ProtocolVersion version, Channel channel, Jt808Session.Role role) {
        return ((DefaultJt808Session) this.generateSession(terminalId, version, channel)).role(role);
    }

    protected DefaultJt808Session buildSession(String terminalId, Jt808ProtocolVersion version, Channel channel, Jt808FlowIdGenerator flowIdGenerator) {
        final DefaultJt808Session session = new DefaultJt808Session(flowIdGenerator);
        session.channel(channel);
        session.id(generateSessionId(channel));
        session.terminalId(terminalId);
        session.lastCommunicateTimestamp(System.currentTimeMillis());
        session.protocolVersion(version);
        session.role(Jt808Session.Role.INSTRUCTION);
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
            if (oldSession.channel() != channel) {
                log.warn("replace channel for terminal({}), new:{}, old:{}", terminalId, channel.remoteAddress(), oldSession.channel().remoteAddress());
                // 单个终端一般来说不会有高并发的问题，这里就不加锁了
                // 有必要的话，可以自己复写该方法或实现自己的SessionManager
                oldSession.channel(channel);
            }
            return oldSession;
        }
        final Jt808Session newSession = generateSession(terminalId, version, channel);
        persistence(newSession);
        return newSession;
    }

    @Override
    public void persistence(Jt808Session session) {
        lock.writeLock().lock();
        try {
            this.sessionMap.put(session.terminalId(), session);
            sessionIdTerminalIdMapping.put(session.id(), session.terminalId());
        } finally {
            lock.writeLock().unlock();
        }
        this.invokeListeners(listener -> listener.onSessionAdd(session));
    }

    private void invokeListeners(Consumer<Jt808SessionEventListener> consumer) {
        for (Jt808SessionEventListener listener : this.listeners) {
            try {
                consumer.accept(listener);
            } catch (Throwable e) {
                log.error("An error occurred while invoke Jt808SessionManagerEventListener", e);
            }
        }
    }

    @Override
    public Jt808Session removeBySessionId(String sessionId) {
        lock.writeLock().lock();
        try {
            final String terminalId = sessionIdTerminalIdMapping.remove(sessionId);
            if (terminalId != null) {
                final Jt808Session session = sessionMap.remove(terminalId);
                this.invokeListeners(listener -> listener.onSessionRemove(session));
                return session;
            }
        } finally {
            lock.writeLock().unlock();
        }

        return null;
    }

    @Override
    public void removeBySessionIdAndClose(String sessionId, SessionCloseReason reason) {
        lock.writeLock().lock();
        try {
            final Jt808Session session = this.removeBySessionId(sessionId);
            if (session != null) {
                invokeListeners(listener -> listener.onSessionClose(session, reason));
                session.channel().close();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<Jt808Session> findByTerminalId(String terminalId, boolean updateLastCommunicateTime) {
        final Jt808Session session = sessionMap.get(terminalId);
        if (session == null) {
            return Optional.empty();
        }

        if (updateLastCommunicateTime) {
            session.lastCommunicateTimestamp(System.currentTimeMillis());
        }

        if (!this.checkStatus(session)) {
            return Optional.empty();
        }

        return Optional.of(session);
    }

    private boolean checkStatus(Jt808Session session) {
        // if (!session.getChannel().isOpen()) {
        if (!session.channel().isActive()) {
            if (log.isDebugEnabled()) {
                log.debug("Remove session [{}], because channel !isActive() ", session.terminalId());
            }
            this.removeBySessionIdAndClose(session.id(), DefaultSessionCloseReason.CHANNEL_INACTIVE);
            return false;
        }
        return true;
    }

    @Override
    public synchronized Jt808SessionManager addListener(Jt808SessionEventListener listener) {
        this.listeners.add(listener);
        return this;
    }

    @Override
    public List<Jt808SessionEventListener> getListeners() {
        return listeners;
    }
}
