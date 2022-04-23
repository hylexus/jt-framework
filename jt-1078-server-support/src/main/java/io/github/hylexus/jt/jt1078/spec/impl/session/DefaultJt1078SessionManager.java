package io.github.hylexus.jt.jt1078.spec.impl.session;

import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 */
public class DefaultJt1078SessionManager implements Jt1078SessionManager {

    private final Map<String, Jt1078Session> sessionMap = new ConcurrentHashMap<>();

    @Override
    public Optional<Jt1078Session> findBySim(String sim, boolean updateLastCommunicateTime) {
        final Jt1078Session session = this.sessionMap.get(sim);
        if (session == null) {
            return Optional.empty();
        }
        if (updateLastCommunicateTime) {
            session.lastCommunicateTimestamp(System.currentTimeMillis());
        }
        return Optional.of(session);
    }

    @Override
    public void persistence(Jt1078Session session) {
        sessionMap.put(session.sim(), session);
    }
}
