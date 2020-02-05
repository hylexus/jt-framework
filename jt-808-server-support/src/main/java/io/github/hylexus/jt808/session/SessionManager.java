package io.github.hylexus.jt808.session;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 * Created At 2019-08-24 16:50
 */
@Slf4j
public class SessionManager {
    private final Object LOCK = new Object();
    private static volatile SessionManager instance = new SessionManager();

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return instance;
    }

    // <terminalId,Session>
    private Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    // <sessionId,terminalId>
    private Map<String, String> sessionIdTerminalIdMapping = new ConcurrentHashMap<>();

    public void persistenceIfNecessary(String terminalId, Channel channel) {
        Optional<Session> session = findByTerminalId(terminalId, true);
        if (!session.isPresent()) {
            Session newSession = Session.buildSession(channel, terminalId);
            persistence(newSession);
        }
    }

    public void persistence(Session session) {
        synchronized (LOCK) {
            this.sessionMap.put(session.getTerminalId(), session);
            sessionIdTerminalIdMapping.put(session.getId(), session.getTerminalId());
        }
    }

    public void removeBySessionIdAndClose(String sessionId, SessionCloseReason reason) {
        synchronized (LOCK) {
            sessionIdTerminalIdMapping.remove(sessionId);
            sessionMap.remove(sessionId);
        }
        log.info("session removed [{}] , sessionId = {}", reason, sessionId);
    }

    public Optional<Session> findByTerminalId(String terminalId) {
        return findByTerminalId(terminalId, false);
    }

    public Optional<Session> findByTerminalId(String terminalId, boolean updateLastCommunicateTime) {
        Session session = sessionMap.get(terminalId);
        if (session == null) {
            return Optional.empty();
        }

        if (updateLastCommunicateTime) {
            synchronized (LOCK) {
                session.setLastCommunicateTimeStamp(System.currentTimeMillis());
            }
        }

        if (!this.checkStatus(session)) {
            return Optional.empty();
        }

        return Optional.of(session);
    }

    private boolean checkStatus(Session session) {
        if (!session.getChannel().isActive()) {
            synchronized (LOCK) {
                this.sessionMap.remove(session.getId());
                sessionIdTerminalIdMapping.remove(session.getId());
                session.getChannel().close();
                log.error("Close by server, terminalId = {}", session.getTerminalId());
                return false;
            }
        }

        return true;
    }

}
