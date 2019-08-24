package io.github.hylexus.jt.jt808.session;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
    // <terminal,SessionId>
    private Map<String, String> terminalIdSessionIdMapping = new ConcurrentHashMap<>();

    public void persistenceIfNecessary(String terminalId, Channel channel) {
        Optional<Session> session = findByTerminalId(terminalId, true);
        if (!session.isPresent()) {
            Session newSession = Session.buildSession(channel, terminalId);
            persistence(newSession);
        }
    }

    public void persistence(Session session) {
        String sessionId = this.terminalIdSessionIdMapping.get(session.getTerminalId());
        if (sessionId != null) {
            session.setLastCommunicateTimeStamp(System.currentTimeMillis());
            return;
        }

        synchronized (lock) {
            this.sessionMap.put(session.getId(), session);
            this.terminalIdSessionIdMapping.put(session.getTerminalId(), session.getId());
        }
    }

    public void removeByTerminalIdAndClose(String terminalId, SessionCloseReason reason) {
        synchronized (lock) {
            String sessionId = terminalIdSessionIdMapping.get(terminalId);
            if (sessionId == null) {
                log.info("session removed [{}] , terminalId={}", reason, terminalId);
                return;
            }
            Session remove = sessionMap.remove(sessionId);
            if (remove != null) {
                remove.getChannel().close();
            }
            log.info("session removed [{}] , terminalId={}, sessionId={}", reason, terminalId, sessionId);
        }
    }

    public Optional<Session> findByTerminalId(String terminalId) {
        return findByTerminalId(terminalId, false);
    }

    public Optional<Session> findByTerminalId(String terminalId, boolean updateLastCommunicateTime) {
        String sessionId = terminalIdSessionIdMapping.get(terminalId);
        if (StringUtils.isEmpty(sessionId)) {
            return Optional.empty();
        }
        Session session = sessionMap.get(sessionId);
        if (session == null) {
            synchronized (lock) {
                log.error("xxx remove by server null session");
                terminalIdSessionIdMapping.remove(terminalId);
            }
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

    private boolean checkStatus(Session session) {
        if (!session.getChannel().isActive()) {
            synchronized (lock) {
                this.sessionMap.remove(session.getId());
                this.terminalIdSessionIdMapping.remove(session.getTerminalId());
                session.getChannel().close();
                log.error("Close by server, terminalId = {}", session.getTerminalId());
                return false;
            }
        }

        return true;
    }

}
