package io.github.hylexus.jt.jt808.request;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeader;
import io.netty.buffer.ByteBuf;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author hylexus
 */
public interface Jt808Request {

    MsgType msgType();

    Jt808MsgHeader header();

    default Jt808ProtocolVersion version() {
        return header().version();
    }

    default int flowId() {
        return header().flowId();
    }

    ByteBuf rawByteBuf();

    ByteBuf body();

    default int msgBodyLength() {
        return header().msgBodyLength();
    }

    byte originalCheckSum();

    byte calculatedCheckSum();

    Map<String, Object> getAttributes();

    /**
     * Return the request attribute value if present.
     *
     * @param name the attribute name
     * @param <T>  the attribute type
     * @return the attribute value
     */
    @SuppressWarnings("unchecked")
    @Nullable
    default <T> T getAttribute(String name) {
        return (T) getAttributes().get(name);
    }

    /**
     * Return the request attribute value or if not present raise an
     * {@link IllegalArgumentException}.
     *
     * @param name the attribute name
     * @param <T>  the attribute type
     * @return the attribute value
     */
    @SuppressWarnings("unchecked")
    default <T> T getRequiredAttribute(String name) {
        T value = getAttribute(name);
        Assert.notNull(value, () -> "Required attribute '" + name + "' is missing");
        return value;
    }

    /**
     * Return the request attribute value, or a default, fallback value.
     *
     * @param name         the attribute name
     * @param defaultValue a default value to return instead
     * @param <T>          the attribute type
     * @return the attribute value
     */
    @SuppressWarnings("unchecked")
    default <T> T getAttributeOrDefault(String name, T defaultValue) {
        return (T) getAttributes().getOrDefault(name, defaultValue);
    }


    @Override
    String toString();
}
