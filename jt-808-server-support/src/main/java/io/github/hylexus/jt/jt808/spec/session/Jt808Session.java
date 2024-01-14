package io.github.hylexus.jt.jt808.spec.session;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.exception.JtCommunicationException;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Created At 2020-06-20 15:58
 *
 * @author hylexus
 */
@BuiltinComponent
public interface Jt808Session extends Jt808FlowIdGenerator {

    enum Role {
        /**
         * 指令服务器对应的会话
         */
        INSTRUCTION,
        /**
         * 附件服务器对应的会话
         */
        ATTACHMENT,
    }

    /**
     * @since 2.1.4
     */
    default Role role() {
        return Role.INSTRUCTION;
    }

    /**
     * @param bytes 待发送给客户端的数据
     * @see #sendMsgToClient(ByteBuf)
     */
    default void sendMsgToClient(byte[] bytes) throws JtCommunicationException {
        this.sendMsgToClient(Unpooled.wrappedBuffer(bytes));
    }

    /**
     * @param byteBuf 待发送给客户端的数据
     */
    void sendMsgToClient(ByteBuf byteBuf) throws JtCommunicationException;

    String id();

    default String sessionId() {
        return id();
    }

    Channel channel();

    Jt808Session channel(Channel channel);

    String terminalId();

    /**
     * @return 当前终端的协议版本号
     */
    Jt808ProtocolVersion protocolVersion();

    /**
     * @return 上次通信时间
     */
    long lastCommunicateTimestamp();

    Jt808Session lastCommunicateTimestamp(long lastCommunicateTimestamp);

    /**
     * session 创建时间
     *
     * @since 2.2.0
     */
    default long createdAt() {
        return 0;
    }

    /**
     * 给当前会话中存储一些 Key-Value 属性
     *
     * @since 2.0.3
     */
    default Jt808Session setAttribute(String key, Object value) {
        getAttributes().put(key, value);
        return this;
    }

    default Jt808Session removeAttribute(String key) {
        getAttributes().remove(key);
        return this;
    }

    default Jt808Session removeAttributes(Iterable<String> keys) {
        final Map<String, Object> attributes = getAttributes();
        for (final String key : keys) {
            attributes.remove(key);
        }
        return this;
    }

    default Jt808Session removeAttributes(String... keys) {
        if (keys == null) {
            return this;
        }
        final Map<String, Object> attributes = getAttributes();
        for (final String key : keys) {
            attributes.remove(key);
        }
        return this;
    }

    /**
     * 获取当前会话中存储的所有 Key-Value 属性
     *
     * @since 2.0.3
     */
    Map<String, Object> getAttributes();

    /**
     * 从当前会话中获取指定名称的属性
     *
     * @since 2.0.3
     */
    @SuppressWarnings("unchecked")
    default <T> T getAttribute(String key) {
        return (T) getAttributes().get(key);
    }

    /**
     * 从当前会话中获取指定名称的属性, 如果获取不到，会抛 NPE 异常
     *
     * @since 2.0.3
     */
    default <T> T getRequiredAttribute(String key) {
        T value = getAttribute(key);
        Objects.requireNonNull(value, () -> "Required attribute '" + key + "' is missing");
        return value;
    }

    /**
     * 从当前会话中获取指定名称的属性。 如果获取不到，返回 {@code defaultValue}给定的默认值
     *
     * @since 2.0.3
     */
    @SuppressWarnings("unchecked")
    default <T> T getAttributeOrDefault(String key, T defaultValue) {
        return (T) getAttributes().getOrDefault(key, defaultValue);
    }

    /**
     * 从当前会话中获取指定名称的属性。 如果获取不到，返回 {@code supplier} 返回的默认值。
     * <p>
     * 和 {@link #getAttributeOrDefault(String, Object)} 的区别是: 如果能通过 {@code key} 获取到属性，就不会执行 {@code supplier} 的逻辑。
     *
     * @since 2.0.3
     */
    @SuppressWarnings("unchecked")
    default <T> T getAttributeOrDefault(String key, Supplier<T> supplier) {
        final Object value = getAttributes().get(key);
        if (value == null) {
            return supplier.get();
        }
        return (T) value;
    }

    String toString();
}
