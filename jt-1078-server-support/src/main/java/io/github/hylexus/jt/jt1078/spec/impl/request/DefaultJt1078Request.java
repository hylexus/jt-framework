package io.github.hylexus.jt.jt1078.spec.impl.request;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestHeader;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hylexus
 */
@BuiltinComponent
public class DefaultJt1078Request implements Jt1078Request, Jt1078Request.Jt1078RequestBuilder {

    private Jt1078RequestHeader header;
    private ByteBuf rawByteBuf;
    private ByteBuf body;
    private Map<String, Object> attr = new HashMap<>();

    protected DefaultJt1078Request(Jt1078RequestHeader header, ByteBuf rawByteBuf, ByteBuf body) {
        this.header = header;
        this.rawByteBuf = rawByteBuf;
        this.body = body;
    }

    public DefaultJt1078Request() {
    }

    public DefaultJt1078Request(Jt1078Request another) {
        this.rawByteBuf(another.rawByteBuf())
                .body(another.body())
                .header(another.header());
    }

    @Override
    public Jt1078RequestHeader header() {
        return this.header;
    }

    @Override
    public Jt1078RequestBuilder header(Jt1078RequestHeader header) {
        this.header = header;
        return this;
    }

    @Override
    public ByteBuf rawByteBuf() {
        return this.rawByteBuf;
    }

    @Override
    public Jt1078RequestBuilder rawByteBuf(ByteBuf rawByteBuf, boolean autoRelease) {
        final ByteBuf oldBuf = this.rawByteBuf;
        try {
            this.rawByteBuf = rawByteBuf;
            return this;
        } finally {
            if (autoRelease) {
                JtCommonUtils.release(oldBuf);
            }
        }
    }

    @Override
    public ByteBuf body() {
        return this.body;
    }

    @Override
    public Jt1078RequestBuilder body(ByteBuf body, boolean autoRelease) {
        final ByteBuf oldBuf = this.body;
        try {
            this.body = body;
            return this;
        } finally {
            if (autoRelease) {
                JtCommonUtils.release(oldBuf);
            }
        }
    }

    @Override
    public Map<String, Object> attributes() {
        return this.attr;
    }

    @Override
    public Jt1078Request build() {
        return new DefaultJt1078Request(this.header, this.rawByteBuf, this.body);
    }

    @Override
    public String toString() {
        return "DefaultJt1078Request{" + "header=" + header + '}';
    }
}
