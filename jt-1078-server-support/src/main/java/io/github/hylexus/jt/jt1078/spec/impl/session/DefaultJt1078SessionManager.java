package io.github.hylexus.jt.jt1078.spec.impl.session;

import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionCloseReason;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionEventListener;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * @author hylexus
 */
public class DefaultJt1078SessionManager implements Jt1078SessionManager {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    // <sim, session>
    private final Map<String, Jt1078Session> sessionMap = new HashMap<>();

    // <sessionId, sim>
    private final Map<String, String> sessionIdSimMapping = new HashMap<>();
    private final List<Jt1078SessionEventListener> listeners = new ArrayList<>();

    private DefaultJt1078SessionManager() {
    }

    public DefaultJt1078SessionManager(Consumer<DefaultJt1078SessionManager> afterPropertySet) {
        this();
        afterPropertySet.accept(this);
    }

    @Override
    public Optional<Jt1078Session> findBySessionId(String sessionId) {
        String terminalId = sessionIdSimMapping.get(sessionId);
        if (StringUtils.isEmpty(terminalId)) {
            return Optional.empty();
        }

        return findBySim(terminalId, false);
    }

    @Override
    public Optional<Jt1078Session> findBySim(String sim, boolean updateLastCommunicateTime) {
        final Jt1078Session session = this.sessionMap.get(sim);
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
            this.removeBySessionIdAndClose(session.sessionId(), DefaultJt1078SessionCloseReason.CHANNEL_INACTIVE);
            return false;
        }
        return true;
    }

    @Override
    public void persistence(Jt1078Session session) {
        lock.writeLock().lock();
        try {
            sessionMap.put(session.sim(), session);
            sessionIdSimMapping.put(session.sessionId(), session.sim());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Jt1078Session removeBySessionId(String sessionId) {
        lock.writeLock().lock();
        try {
            final String sim = sessionIdSimMapping.remove(sessionId);
            if (sim != null) {
                final Jt1078Session session = sessionMap.remove(sim);
                this.invokeListeners(listener -> listener.onSessionRemove(session));
                return session;
            }
        } finally {
            lock.writeLock().unlock();
        }

        return null;
    }

    @Override
    public void removeBySessionIdAndClose(String sessionId, Jt1078SessionCloseReason reason) {
        final Jt1078Session session = this.removeBySessionId(sessionId);
        if (session != null) {
            invokeListeners(listener -> listener.onSessionClose(session, reason));
            try {
                session.channel().close().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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

}
