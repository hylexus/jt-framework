package io.github.hylexus.jt.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author hylexus
 */
public interface Jt808ByteReader {

    ByteBuf readable();

    static Jt808ByteReader of(ByteBuf value) {
        return () -> value;
    }

    static <T> T doWithReader(byte[] bytes, Function<Jt808ByteReader, T> fn) {
        final ByteBuf buf = ByteBufAllocator.DEFAULT.buffer().writeBytes(bytes);
        try {
            return fn.apply(of(buf));
        } finally {
            JtCommonUtils.release(buf);
        }
    }

    static void doWithReader(byte[] bytes, Consumer<Jt808ByteReader> fn) {
        final ByteBuf buf = ByteBufAllocator.DEFAULT.buffer().writeBytes(bytes);
        try {
            fn.accept(of(buf));
        } finally {
            JtCommonUtils.release(buf);
        }
    }

    default String readBcd(int length) {
        return JtCommonUtils.readBcd(readable(), length);
    }

    default Jt808ByteReader readBcd(int length, Consumer<String> consumer) {
        final String str = this.readBcd(length);
        consumer.accept(str);
        return this;
    }

    default String readString(int length) {
        return readString(length, JtCommonUtils.CHARSET_GBK);
    }

    default String readString(int length, Charset charset) {
        return JtCommonUtils.readString(readable(), length, charset);
    }

    default Jt808ByteReader readString(int length, Charset charset, Consumer<String> consumer) {
        final String string = JtCommonUtils.readString(readable(), length, charset);
        consumer.accept(string);
        return this;
    }

    default Jt808ByteReader readString(int length, Consumer<String> consumer) {
        final String string = this.readString(length);
        consumer.accept(string);
        return this;
    }

    default long readUnsignedDword() {
        return JtCommonUtils.readUnsignedDword(readable());
    }

    default Jt808ByteReader readUnsignedDword(Consumer<Long> consumer) {
        final long unsignedDword = this.readUnsignedDword();
        consumer.accept(unsignedDword);
        return this;
    }

    /**
     * @deprecated 建议使用 {@link #readUnsignedDword()} 代替
     */
    //@Deprecated (since = "2.1.4", forRemoval = false)
    @Deprecated
    default int readDword() {
        return JtCommonUtils.readDword(readable());
    }

    /**
     * @deprecated 建议使用 {@link #readUnsignedDword(Consumer)} 代替
     */
    //@Deprecated(since = "2.1.4", forRemoval = false)
    @Deprecated
    default Jt808ByteReader readDword(Consumer<Integer> consumer) {
        final int dword = this.readDword();
        consumer.accept(dword);
        return this;
    }

    /**
     * @deprecated 建议使用 {@link #readUnsignedWord()} 代替
     */
    //@Deprecated(since = "2.1.4", forRemoval = false)
    @Deprecated
    default short readWord() {
        return JtCommonUtils.readWord(readable());
    }

    /**
     * @deprecated 建议使用 {@link #readUnsignedWord(Consumer)} 代替
     */
    //@Deprecated(since = "2.1.4", forRemoval = false)
    @Deprecated
    default Jt808ByteReader readWord(Consumer<Short> consumer) {
        final short word = this.readWord();
        consumer.accept(word);
        return this;
    }

    default short readUnsignedByte() {
        return readable().readUnsignedByte();
    }

    default Jt808ByteReader readUnsignedByte(Consumer<Short> consumer) {
        final short unsignedByte = this.readUnsignedByte();
        consumer.accept(unsignedByte);
        return this;
    }

    default byte readByte() {
        return readable().readByte();
    }

    default Jt808ByteReader readByte(Consumer<Byte> consumer) {
        final byte aByte = this.readByte();
        consumer.accept(aByte);
        return this;
    }

    default byte[] readBytes(int length) {
        return JtCommonUtils.readBytes(readable(), length);
    }

    default Jt808ByteReader readBytes(int length, Consumer<byte[]> consumer) {
        final byte[] bytes = this.readBytes(length);
        consumer.accept(bytes);
        return this;
    }

    default int readUnsignedWord() {
        return JtCommonUtils.readUnsignedWord(readable());
    }

    default Jt808ByteReader readUnsignedWord(Consumer<Integer> consumer) {
        final int word = this.readUnsignedWord();
        consumer.accept(word);
        return this;
    }

}
