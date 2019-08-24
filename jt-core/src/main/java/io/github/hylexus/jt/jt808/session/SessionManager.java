package io.github.hylexus.jt.jt808.session;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author hylexus
 * Created At 2019-08-24 16:50
 */
@Slf4j
public class SessionManager {
    private final String lock = "lock";
    private static volatile SessionManager instance = new SessionManager();

    private SessionManager() {
        log.debug("1 time");
    }

    public static SessionManager getInstance() {
//        if (instance == null) {
//            synchronized (SessionHolder.class) {
//                if (instance == null) {
//                    instance = new SessionHolder();
//                }
//            }
//        }
        return instance;
    }

    private Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    private Map<String, String> phoneMap = new ConcurrentHashMap<>();
    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
//
//    public List<TerminalInfoVO> toList() {
//        List<TerminalInfoVO> list = Lists.newArrayListWithCapacity(phoneMap.size());
//        for (Map.Entry<String, String> entry : this.phoneMap.entrySet()) {
//            Session session = sessionMap.get(entry.getValue());
//            if (session == null) {
//                continue;
//            }
//
//            TerminalInfoVO vo = new TerminalInfoVO();
//            vo.setAuth(session.isAuthenticated());
//            vo.setLastCommunicationTime(new Date(session.getLastCommunicateTimeStamp()));
//            vo.setSessionId(session.getId());
//            vo.setTerminalId(entry.getKey());
//            list.add(vo);
//        }
//
//        return list;
//    }

    public synchronized void persistenceIfNecessary(Session session) {
        String sessionId = this.phoneMap.get(session.getTerminalId());
        if (sessionId != null) {
            return;
        }

        this.sessionMap.put(session.getId(), session);
        this.phoneMap.put(session.getTerminalId(), session.getId());
        this.channels.add(session.getChannel());
    }

    public void remove(String sessionId) {
        Session session = this.sessionMap.get(sessionId);
        if (session == null) {
            return;
        }

        synchronized (lock) {
            this.sessionMap.remove(sessionId);
            this.phoneMap.remove(session.getTerminalId());
            this.channels.remove(session.getChannel());
            session.getChannel().close();
        }

        log.warn("session removed , terminalId={}, sessionId={}", session.getTerminalId(), session.getId());
    }

    public synchronized Session sync(String terminalId, Channel channel) {
        final Optional<Session> sessionInfo = findByTerminalId(terminalId);
        if (sessionInfo.isPresent()) {
            Session session = sessionInfo.get();
            session.setLastCommunicateTimeStamp(System.currentTimeMillis());
            session.setChannel(channel);
            return session;
        } else {
            Session session = Session.buildSession(channel, terminalId);
            persistenceIfNecessary(session);
            return session;
        }

    }

    public Optional<Session> findByTerminalId(String terminalId) {
        String sessionId = phoneMap.get(terminalId);
        if (StringUtils.isEmpty(sessionId))
            return Optional.empty();
        Session session = sessionMap.get(sessionId);
        if (session == null) {
            synchronized (lock) {
                log.error("xxx remove by server null session");
                phoneMap.remove(terminalId);
            }
            return Optional.empty();
        }

        if (!this.checkStatus(session))
            return Optional.empty();

        return Optional.of(session);
    }

    private boolean checkStatus(Session session) {
        if (!session.getChannel().isActive()) {
            synchronized (lock) {
                this.sessionMap.remove(session.getId());
                this.phoneMap.remove(session.getTerminalId());
                session.getChannel().close();
                log.error("xxx close by server");
                return false;
            }
        }

        return true;
    }

    public int size() {
        return this.phoneMap.size();
    }

    public int channelSize() {
        return this.channels.size();
    }

    public List<String> terminalIdList() {
        return this.phoneMap.keySet()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
