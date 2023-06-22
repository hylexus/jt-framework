package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078Request;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author hylexus
 */
@BuiltinComponent
public interface Jt1078Request {

    static Jt1078RequestBuilder newBuilder() {
        return new DefaultJt1078Request();
    }

    Jt1078RequestHeader header();

    @Nullable
    ByteBuf rawByteBuf();

    /**
     * 请求体
     */
    ByteBuf body();

    default void release() {
        JtCommonUtils.release(this.rawByteBuf(), this.body());
    }

    default Jt1078Request retain() {
        final ByteBuf byteBuf = rawByteBuf();
        if (byteBuf != null) {
            byteBuf.retain();
        }
        body().retain();
        return this;
    }

    default Jt1078Request copy() {
        return this.mutate().header(this.header()).body(this.body().copy()).rawByteBuf(null).build();
    }

    default Jt1078RequestBuilder mutate() {
        return new DefaultJt1078Request(this);
    }

    // short-cut methods
    default String sim() {
        return header().sim();
    }

    default short channelNumber() {
        return header().channelNumber();
    }

    default int msgBodyLength() {
        return header().msgBodyLength();
    }

    default byte pt() {
        return header().pt();
    }

    default Jt1078PayloadType payloadType() {
        return header().payloadType();
    }

    default byte dataTypeValue() {
        return header().dataTypeValue();
    }

    Map<String, Object> attributes();

    default Jt1078Request attribute(String key, Object value) {
        attributes().put(key, value);
        return this;
    }

    interface Jt1078RequestBuilder {
        Jt1078RequestBuilder header(Jt1078RequestHeader header);

        default Jt1078RequestBuilder body(ByteBuf body) {
            return this.body(body, true);
        }

        Jt1078RequestBuilder body(ByteBuf body, boolean autoRelease);

        default Jt1078RequestBuilder rawByteBuf(ByteBuf rawByteBuf) {
            return this.rawByteBuf(rawByteBuf, true);
        }

        Jt1078RequestBuilder rawByteBuf(ByteBuf rawByteBuf, boolean autoRelease);

        Jt1078Request build();
    }
}
