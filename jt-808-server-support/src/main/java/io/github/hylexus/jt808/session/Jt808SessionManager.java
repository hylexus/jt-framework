package io.github.hylexus.jt808.session;

import io.netty.channel.Channel;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * @return 以 {@link Stream} 的形式返回当前 {@link Jt808Session} 列表
     */
    Stream<Jt808Session> list();

    /**
     * 以分页的形式获取 {@link Jt808SessionManager} 中的 {@link Jt808Session}
     *
     * @param page     当前页码，从1开始
     * @param pageSize 每页大小
     * @return 当前页的 {@link Jt808Session} 信息
     * @see #list()
     * @see #list(int, int, Predicate, Comparator, Function)
     */
    default List<Jt808Session> list(int page, int pageSize) {
        return list(page, pageSize, session -> true);
    }

    /**
     * 以分页的形式获取 {@link Jt808SessionManager} 中的 {@link Jt808Session}
     *
     * @param page     当前页码，从1开始
     * @param pageSize 每页大小
     * @param filter   过滤器
     * @return 当前页的 {@link Jt808Session} 信息
     * @see #list()
     * @see #list(int, int, Predicate, Comparator, Function)
     */
    default List<Jt808Session> list(int page, int pageSize, Predicate<Jt808Session> filter) {
        return list(page, pageSize, filter, Comparator.comparing(Jt808Session::getTerminalId), Function.identity());
    }

    /**
     * 以分页的形式获取 {@link Jt808SessionManager} 中的 {@link Jt808Session}
     *
     * @param page      当前页码，从1开始
     * @param pageSize  每页大小
     * @param converter {@link Jt808Session} 到 {@link T} 的转换器
     * @param filter    过滤器
     * @param sorter    排序器
     * @param <T>       结果类型
     * @return 当前页的 {@link Jt808Session} 信息
     * @see #list()
     * @see #list(int, int)
     */
    default <T> List<T> list(int page, int pageSize, Predicate<Jt808Session> filter, Comparator<Jt808Session> sorter, Function<Jt808Session, T> converter) {
        return list().filter(filter).sorted(sorter).skip((page - 1) * pageSize).limit(pageSize).map(converter).collect(Collectors.toList());
    }

    /**
     * @return 当前活跃的 {@link Jt808Session} 总数。
     */
    long count();

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

    @Nullable
    Jt808Session removeBySessionId(String sessionId);

    /**
     * @param sessionId sessionId
     * @param reason    关闭原因
     */
    void removeBySessionIdAndClose(String sessionId, ISessionCloseReason reason);

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

    Jt808SessionManagerEventListener getEventListener();

    void setEventListener(Jt808SessionManagerEventListener listener);
}
