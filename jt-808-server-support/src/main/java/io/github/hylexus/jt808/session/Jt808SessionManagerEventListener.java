package io.github.hylexus.jt808.session;

import javax.annotation.Nullable;

/**
 * {@link Jt808SessionManager} 的事件监听器。
 * <p>
 * <ul>
 *     <li>注意：该接口实现类中不宜做耗时太长/阻塞的操作！！！</li>
 *     <li>注意：该接口实现类中不宜做耗时太长/阻塞的操作！！！</li>
 *     <li>注意：该接口实现类中不宜做耗时太长/阻塞的操作！！！</li>
 * </ul>
 * <p>
 * 如果有耗时操作，请异步处理。
 * <p>
 * Created At 2020-06-22 20:33
 *
 * @author hylexus
 */
public interface Jt808SessionManagerEventListener {

    /**
     * {@link Jt808Session} 新建事件
     * <p>
     * 注意：该方法中不宜做耗时太长/阻塞的操作！！！
     *
     * @param session 新建的 {@link Jt808Session}
     */
    default void onSessionAdd(@Nullable Jt808Session session) {
    }

    /**
     * {@link Jt808Session} 移除事件
     * <p>
     * 注意：该方法中不宜做耗时太长/阻塞的操作！！！
     *
     * @param session 被移除的 {@link Jt808Session}
     */
    default void onSessionRemove(@Nullable Jt808Session session) {
    }

    /**
     * {@link Jt808Session} 关闭事件
     * <p>
     * 注意：该方法中不宜做耗时太长/阻塞的操作！！！
     *
     * @param session     被关闭的 {@link Jt808Session}
     * @param closeReason 关闭原因
     */
    default void onSessionClose(@Nullable Jt808Session session, ISessionCloseReason closeReason) {
    }

}
