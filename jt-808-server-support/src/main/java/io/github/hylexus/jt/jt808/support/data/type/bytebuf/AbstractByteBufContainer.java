package io.github.hylexus.jt.jt808.support.data.type.bytebuf;

import io.github.hylexus.jt.jt808.JtProtocolConstant;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;

public abstract class AbstractByteBufContainer implements ByteBufContainer {
    private final ByteBuf byteBuf;
    private final int length;

    public AbstractByteBufContainer(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
        this.length = byteBuf.readableBytes();
    }

    @Override
    public ByteBuf value() {
        return byteBuf;
    }

    @Override
    public void release() {
        byteBuf.release();
    }

    @Override
    public int length() {
        return this.length;
    }

    static class BcdByteBufContainer extends AbstractByteBufContainer {

        public BcdByteBufContainer(String value) {
            super(JtProtocolUtils.writeBcd(ByteBufAllocator.DEFAULT.buffer(), value));
        }
    }

    static class ByteArrayByteBufContainer extends AbstractByteBufContainer {

        public ByteArrayByteBufContainer(byte[] value) {
            super(JtProtocolUtils.writeBytes(ByteBufAllocator.DEFAULT.buffer(), value));
        }
    }

    static class ByteByteBufContainer extends AbstractByteBufContainer {

        public ByteByteBufContainer(int value) {
            super(ByteBufAllocator.DEFAULT.buffer().writeByte(value));
        }
    }

    static class DWordByteBufContainer extends AbstractByteBufContainer {

        public DWordByteBufContainer(int value) {
            super(JtProtocolUtils.writeDword(ByteBufAllocator.DEFAULT.buffer(), value));
        }
    }

    static class StringByteBufContainer extends AbstractByteBufContainer {

        public StringByteBufContainer(String value, Charset charset) {
            super(JtProtocolUtils.writeString(ByteBufAllocator.DEFAULT.buffer(), value, charset));
        }

        public StringByteBufContainer(String value) {
            this(value, JtProtocolConstant.JT_808_STRING_ENCODING);
        }
    }

    static class WordByteBufContainer extends AbstractByteBufContainer {

        public WordByteBufContainer(int value) {
            super(JtProtocolUtils.writeWord(ByteBufAllocator.DEFAULT.buffer(), value));
        }
    }
}
