package io.github.hylexus.jt.jt1078.spec.impl.session;

import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionCloseReason;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionEventListener;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author hylexus
 */
public class DefaultJt1078SessionManager implements Jt1078SessionManager {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    // <sim_channelNumber, session>
    private final Map<String, Jt1078Session> sessionMap = new HashMap<>();

    private final List<Jt1078SessionEventListener> listeners = new ArrayList<>();

    public DefaultJt1078SessionManager() {
    }

    @Override
    public Optional<Jt1078Session> findBySessionId(String sessionId) {
        return Optional.ofNullable(sessionMap.get(sessionId));
    }

    @Override
    public Optional<Jt1078Session> findBySimAndChannel(String sim, short channelNumber, boolean updateLastCommunicateTime) {
        final Jt1078Session session = this.sessionMap.get(generateSessionId(sim, channelNumber));
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
}
