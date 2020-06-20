package io.github.hylexus.jt808.session;

import io.netty.channel.Channel;

import java.util.Optional;

/**
 * Session管理器。
 * <p>
 * 如有必要，你可以自定义该类的实现，并注入到Spring容器中以替换默认实现。
 * <p>
 * Created At 2020-06-20 15:53
 *
 * @author hylexus
 */
public interface Jt808SessionManager {
    /**
     * @param channel Channel
     * @return sessionId
     */
    default String generateSessionId(Channel channel) {
        return channel.id().asLongText();
    }

    /**
     * @param channel    Channel
     * @param terminalId 终端号(手机号)
     * @return 生成的新Session
     */
    Jt808Session generateSession(Channel channel, String terminalId);

    /**
     * @param terminalId 终端号(手机号)
     * @param channel    Channel
     * @return 如果已经存在，则刷新上次通信时间并返回Session。否则，生成新的Session并返回。
     * @see #persistenceIfNecessary(String, Channel, boolean)
     */
    default Jt808Session persistenceIfNecessary(String terminalId, Channel channel) {
        return persistenceIfNecessary(terminalId, channel, true);
    }

    /**
     * @param terminalId                终端号(手机号)
     * @param channel                   Channel
     * @param updateLastCommunicateTime 是否更新上次通信时间
     * @return 如果已经存在，则刷新上次通信时间并返回Session。否则，生成新的Session并返回。
     */
    default Jt808Session persistenceIfNecessary(String terminalId, Channel channel, boolean updateLastCommunicateTime) {
        final Optional<Jt808Session> session = findByTerminalId(terminalId, updateLastCommunicateTime);
        if (session.isPresent()) {
            return session.get();
        }
        Jt808Session newSession = generateSession(channel, terminalId);
        persistence(newSession);
        return newSession;
    }

    /**
     * @param session 持久化session
     */
    void persistence(Jt808Session session);

    /**
     * @param sessionId sessionId
     * @param reason    关闭原因
     */
    void removeBySessionIdAndClose(String sessionId, SessionCloseReason reason);

    /**
     * @param sessionId sessionId
     * @return 当前 {@link Jt808Session}
     */
    Optional<Jt808Session> findBySessionId(String sessionId);

    /**
     * @param terminalId                终端号(手机号)
     * @param updateLastCommunicateTime 是否更新上次通信时间
     * @return 当前 {@link Jt808Session}
     */
    Optional<Jt808Session> findByTerminalId(String terminalId, boolean updateLastCommunicateTime);

    /**
     * @param terminalId 终端号(手机号)
     * @return 当前 {@link Jt808Session}
     * @see Jt808SessionManager#findByTerminalId(String, boolean)
     */
    default Optional<Jt808Session> findByTerminalId(String terminalId) {
        return findByTerminalId(terminalId, false);
    }

    default void sendBytesToClient(Jt808Session session, byte[] bytes) throws InterruptedException {
        session.sendMsgToClient(bytes);
    }
}
