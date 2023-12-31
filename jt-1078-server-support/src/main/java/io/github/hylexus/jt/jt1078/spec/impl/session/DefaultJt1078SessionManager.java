package io.github.hylexus.jt.jt1078.spec.impl.session;

import io.github.hylexus.jt.jt1078.spec.*;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author hylexus
 */
public class DefaultJt1078SessionManager implements Jt1078SessionManager {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    // <sim_channelNumber, session>
    private final Map<String, Jt1078Session> sessionMap = new HashMap<>();

    private final List<Jt1078SessionEventListener> listeners = new ArrayList<>();
    private final Jt1078TerminalIdConverter jt1078TerminalIdConverter;

    public DefaultJt1078SessionManager(Jt1078TerminalIdConverter jt1078TerminalIdConverter) {
        this.jt1078TerminalIdConverter = jt1078TerminalIdConverter;
    }

    @Override
    public Jt1078TerminalIdConverter terminalIdConverter() {
        return this.jt1078TerminalIdConverter;
    }

    @Override
    public Optional<Jt1078Session> findBySessionId(String sessionId) {
        return Optional.ofNullable(sessionMap.get(sessionId));
    }

    @Override
    public Optional<Jt1078Session> findBySimAndChannel(String sim, short channelNumber, boolean updateLastCommunicateTime) {
        final Jt1078Session session = this.sessionMap.get(generateSessionId(this.terminalIdConverter().convert(sim), channelNumber));
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

    private boolean checkStatus(Jt1078Session session) {
        // if (!session.getChannel().isOpen()) {
        if (!session.channel().isActive()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Remove session [{}], because channel !isActive() ", session.terminalId());
            }
            this.removeBySessionIdAndThenClose(session.sessionId(), DefaultJt1078SessionCloseReason.CHANNEL_INACTIVE);
            return false;
        }
        return true;
    }

    @Override
    public void persistence(Jt1078Session session) {
        lock.writeLock().lock();
        try {
            sessionMap.put(session.sessionId(), session);
        } finally {
            lock.writeLock().unlock();
        }

        invokeListeners(listener -> listener.onSessionAdd(session));
    }

    @Override
    public Optional<Jt1078Session> removeBySessionId(String sessionId) {
        lock.writeLock().lock();
        try {
            final Jt1078Session session = sessionMap.remove(sessionId);
            if (session != null) {
                this.invokeListeners(listener -> listener.onSessionRemove(session));
                return Optional.of(session);
            }
        } finally {
            lock.writeLock().unlock();
        }

        return Optional.empty();
    }

    @Override
    public void removeBySimAndThenClose(String sim, Jt1078SessionCloseReason reason) {
        lock.writeLock().lock();
        try {
            final String prefix = this.terminalIdConverter().convert(sim) + "_";
            for (Iterator<Map.Entry<String, Jt1078Session>> iterator = this.sessionMap.entrySet().iterator(); iterator.hasNext(); ) {
                final Map.Entry<String, Jt1078Session> entry = iterator.next();
                // <sim_channelNumber, session>
                final String key = entry.getKey();
                if (!key.startsWith(prefix)) {
                    continue;
                }

                iterator.remove();

                final Jt1078Session session = entry.getValue();
                this.invokeListeners(listener -> listener.onSessionRemove(session));
                this.invokeListeners(listener -> listener.onSessionClose(session, reason));
                session.channel().close();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<Jt1078Session> removeBySessionIdAndThenClose(String sessionId, Jt1078SessionCloseReason reason) {
        final Optional<Jt1078Session> optionalSession = this.removeBySessionId(sessionId);
        optionalSession.ifPresent(session -> {
            invokeListeners(listener -> listener.onSessionClose(session, reason));
            session.channel().close();
        });
        return optionalSession;
    }

    @Override
    public synchronized Jt1078SessionManager addListener(Jt1078SessionEventListener listener) {
        this.listeners.add(listener);
        return this;
    }

    @Override
    public List<Jt1078SessionEventListener> getListeners() {
        return listeners;
    }

    private void invokeListeners(Consumer<Jt1078SessionEventListener> consumer) {
        for (Jt1078SessionEventListener listener : this.listeners) {
            try {
                consumer.accept(listener);
            } catch (Throwable e) {
                LOGGER.error("An error occurred while invoke Jt808SessionManagerEventListener", e);
            }
        }
    }

    @Override
    public Stream<Jt1078Session> list() {
        this.lock.readLock().lock();
        try {
            return sessionMap.values().stream();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public long count() {
        this.lock.readLock().lock();
        try {
            return sessionMap.values().size();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public long count(Predicate<Jt1078Session> filter) {
        this.lock.readLock().lock();
        try {
            return this.sessionMap.values().stream().filter(filter).count();
        } finally {
            this.lock.readLock().unlock();
        }
    }
}
