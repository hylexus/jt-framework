package io.github.hylexus.jt.jt808.support.annotation.codec;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt808.support.utils.ReflectionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.function.Supplier;

/**
 * @author hylexus
 * @since 2.1.4
 */
public class SimpleJt808MsgDecoder {
    private final Jt808AnnotationBasedDecoder delegate;

    private final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    public SimpleJt808MsgDecoder(Jt808AnnotationBasedDecoder delegate) {
        this.delegate = delegate;
    }

    public <T> T decode(byte[] byteStream, Class<T> cls) {
        return this.decode(byteStream, cls, () -> ReflectionUtils.createInstance(cls));
    }

    public <T> T decode(byte[] byteStream, Class<T> cls, Supplier<T> instanceSupplier) {
        ByteBuf byteBuf = null;
        try {
            byteBuf = allocator.buffer(byteStream.length).writeBytes(byteStream);
            return this.decode(byteBuf, cls, instanceSupplier);
        } finally {
            JtCommonUtils.release(byteBuf);
        }
    }

    public <T> T decode(ByteBuf byteStream, Class<T> cls) {
        return this.decode(byteStream, cls, () -> ReflectionUtils.createInstance(cls));
    }

    public <T> T decode(ByteBuf byteStream, Class<T> cls, Supplier<T> instanceSupplier) {
        @SuppressWarnings("unchecked") final T result = (T) this.delegate.decode(cls, instanceSupplier.get(), byteStream, null);
        return result;
    }

}
