package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.impl.request.DefaultJt808Request;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteReader;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author hylexus
 * @see Jt808MsgDecoder
 */
@BuiltinComponent
public interface Jt808Request {

    /**
     * @since 2.1.4
     */
    Jt808Session session();

    /**
     * @return 消息ID
     */
    MsgType msgType();

    /**
     * @return 请求头
     */
    Jt808RequestHeader header();

    /**
     * WARNING!!! 内置的默认实现: 如果是「合并后」的分包消息，返回 {@code null}
     *
     * @return 原始报文(转义之后)
     */
    @Nullable
    ByteBuf rawByteBuf();

    /**
     * @return 消息体(转义之后)
     */
    ByteBuf body();

    /**
     * @return 校验码(原始报文)
     */
    byte originalCheckSum();

    /**
     * @return 校验码(计算后)
     * @see Jt808MsgBytesProcessor#calculateCheckSum(ByteBuf)
     */
    byte calculatedCheckSum();

    Map<String, Object> getAttributes();

    @Override
    String toString();

    // ++++++++++++++++++++++++++++++++++++++++++++
    // ==> Shortcut Methods Start
    // ++++++++++++++++++++++++++++++++++++++++++++

    default void release() {
        JtProtocolUtils.release(this.rawByteBuf(), this.body());
    }

    default Jt808ByteReader bodyAsReader() {
        return this::body;
    }

    default int msgBodyLength() {
        return header().msgBodyLength();
    }

    default String terminalId() {
        return header().terminalId();
    }

    default Jt808ProtocolVersion version() {
        return header().version();
    }

    default int flowId() {
        return header().flowId();
    }

    default Jt808Request setAttribute(String key, Object value) {
        getAttributes().put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    default <T> T getAttribute(String name) {
        return (T) getAttributes().get(name);
    }

    default <T> T getRequiredAttribute(String name) {
        T value = getAttribute(name);
        Objects.requireNonNull(value, () -> "Required attribute '" + name + "' is missing");
        return value;
    }

    @SuppressWarnings("unchecked")
    default <T> T getAttributeOrDefault(String name, T defaultValue) {
        return (T) getAttributes().getOrDefault(name, defaultValue);
    }

    @SuppressWarnings("unchecked")
    default <T> T getAttributeOrDefault(String name, Supplier<T> supplier) {
        return (T) getAttributes().getOrDefault(name, supplier.get());
    }

    default Jt808RequestBuilder mutate() {
        return new DefaultJt808Request.DefaultJt808RequestBuilder(this);
    }

    default Jt808Request copy() {
        final ByteBuf rawByteBuf = this.rawByteBuf();
        return mutate()
                .body(this.body().copy(), false)
                .rawByteBuf(rawByteBuf == null ? null : rawByteBuf.copy(), false)
                .build();
    }

    // ++++++++++++++++++++++++++++++++++++++++++++
    // <== Shortcut Methods End
    // ++++++++++++++++++++++++++++++++++++++++++++

    static Jt808RequestBuilder newBuilder(MsgType msgType) {
        return new DefaultJt808Request.DefaultJt808RequestBuilder(msgType);
    }

    interface Jt808RequestBuilder {

        Jt808RequestBuilder header(Jt808RequestHeader header);

        Jt808RequestBuilder session(Jt808Session session);

        default Jt808RequestBuilder rawByteBuf(ByteBuf byteBuf) {
            return this.rawByteBuf(byteBuf, true);
        }

        Jt808RequestBuilder rawByteBuf(ByteBuf byteBuf, boolean autoRelease);

        Jt808RequestBuilder body(ByteBuf body, boolean autoRelease);

        default Jt808RequestBuilder body(ByteBuf body) {
            return this.body(body, true);
        }

        Jt808RequestBuilder originalCheckSum(byte checkSum);

        Jt808RequestBuilder calculatedCheckSum(byte checkSum);

        Jt808Request build();
    }
}
