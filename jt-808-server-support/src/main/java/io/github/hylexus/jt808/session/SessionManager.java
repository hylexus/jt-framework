package io.github.hylexus.jt808.session;

import io.github.hylexus.jt.annotation.BuiltinComponent;
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
@BuiltinComponent
public class SessionManager implements Jt808SessionManager {
    private final Object lock = new Object();
    private static final Jt808SessionManager instance = new SessionManager();

    private SessionManager() {
    }

    public static Jt808SessionManager getInstance() {
        return instance;
    }

    // <terminalId,Session>
    private final Map<String, Jt808Session> sessionMap = new ConcurrentHashMap<>();
    // <sessionId,terminalId>
    private final Map<String, String> sessionIdTerminalIdMapping = new ConcurrentHashMap<>();

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
        synchronized (lock) {
            this.sessionMap.put(session.getTerminalId(), session);
            sessionIdTerminalIdMapping.put(session.getId(), session.getTerminalId());
        }
    }

    @Override
    public void removeBySessionIdAndClose(String sessionId, SessionCloseReason reason) {
        synchronized (lock) {
            sessionIdTerminalIdMapping.remove(sessionId);
            sessionMap.remove(sessionId);
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
            synchronized (lock) {
                session.setLastCommunicateTimeStamp(System.currentTimeMillis());
            }
        }

        if (!this.checkStatus(session)) {
            return Optional.empty();
        }

        return Optional.of(session);
    }

    private boolean checkStatus(Jt808Session session) {
        if (!session.getChannel().isActive()) {
            //if (!session.getChannel().isOpen()) {
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
