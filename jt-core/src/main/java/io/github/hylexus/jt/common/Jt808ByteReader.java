package io.github.hylexus.jt.common;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.function.Consumer;

/**
 * @author hylexus
 */
public interface Jt808ByteReader {

    ByteBuf readable();

    static Jt808ByteReader of(ByteBuf value) {
        return () -> value;
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

    default int readDword() {
        return JtCommonUtils.readDword(readable());
    }

    default Jt808ByteReader readDword(Consumer<Integer> consumer) {
        final int dword = this.readDword();
        consumer.accept(dword);
        return this;
    }

    default short readWord() {
        return JtCommonUtils.readWord(readable());
    }

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
