package io.github.hylexus.jt.jt808.session;

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
    private final String lock = "lock";
    private static volatile SessionManager instance = new SessionManager();

    private SessionManager() {
        log.info("SessionManager init time");
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
        synchronized (lock) {
            this.sessionMap.put(session.getTerminalId(), session);
            sessionIdTerminalIdMapping.put(session.getId(), session.getTerminalId());
        }
    }

    public void removeBySessionIdAndClose(String sessionId, SessionCloseReason reason) {
        synchronized (lock) {
            sessionIdTerminalIdMapping.remove(sessionId);
            sessionMap.remove(sessionId);
        }
        log.info("session removed [{}] , sessionId = {}", reason, sessionId);
    }

    public void removeByTerminalIdAndClose(String terminalId, SessionCloseReason reason) {
        synchronized (lock) {
            Session sessionInfo = sessionMap.get(terminalId);
            if (sessionInfo != null) {
                sessionIdTerminalIdMapping.remove(sessionInfo.getId());
                log.info("session removed [{}] , terminalId={}", reason, terminalId);
            }
        }
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
            synchronized (lock) {
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
            synchronized (lock) {
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
