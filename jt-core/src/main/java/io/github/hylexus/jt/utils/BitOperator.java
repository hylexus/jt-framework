package io.github.hylexus.jt.utils;

import io.github.hylexus.oaks.utils.Numbers;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 位操作工具类，最多支持8字节。
 */
public interface BitOperator {

    /**
     * 返回不可变对象(每个操作都 {@code new} 一个新实例)
     */
    static BitOperator immutable(long value) {
        return () -> value;
    }

    /**
     * 返回可变对象(每个操作都返回当前实例)
     */
    static BitOperator mutable(long value) {
        return new MutableBitOperator(value);
    }

    long value();

    default BitOperator map(Function<Long, Long> mapper) {
        return () -> mapper.apply(BitOperator.this.value());
    }

    default BitOperator map(Function<Long, Long> mapper, Boolean condition) {
        if (condition) {
            return map(mapper);
        }
        return this;
    }

    default BitOperator map(Function<Long, Long> mapper, Predicate<Long> predicate) {
        if (predicate.test(this.value())) {
            return map(mapper);
        }
        return this;
    }

    default BitOperator map(Function<Long, Long> mapper, Supplier<Boolean> supplier) {
        return map(mapper, it -> supplier.get());
    }

    default BitOperator set(int offset, boolean status) {
        if (status) {
            return this.set(offset);
        }
        return this.reset(offset);
    }

    default BitOperator set(int offset) {
        Assertions.assertThat(offset >= 0 && offset < Long.SIZE, "offset >= 0 && offset < Long.SIZE");
        return map(it -> Numbers.setBitAt(it, offset));
    }

    default BitOperator set(int offset, Predicate<Long> predicate) {
        if (predicate.test(this.value())) {
            return this.set(offset);
        }
        return this;
    }

    default BitOperator setRange(int offset, int length) {
        return map(it -> it | (~0L >>> (Long.SIZE - length) << offset));
    }

    default BitOperator reset(int offset, Predicate<Long> predicate) {
        if (predicate.test(this.value())) {
            return this.reset(offset);
        }
        return this;
    }

    default BitOperator reset(int offset) {
        Assertions.assertThat(offset >= 0 && offset < Long.SIZE, "offset >= 0 && offset < Long.SIZE");
        return map(it -> Numbers.resetBitAt(it, offset));
    }

    default BitOperator resetRange(int offset, int length) {
        Assertions.assertThat(offset >= 0 && offset < Long.SIZE, "offset >= 0 && offset < Long.SIZE");
        return map(it -> it & (~(~0L >>> (Long.SIZE - length) << offset)));
    }

    default int get(int offset) {
        return Numbers.getBitAt(this.value(), offset);
    }

    default int rangedIntValue(int offset, int length) {
        Assertions.assertThat(offset >= 0, "offset >= 0");
        Assertions.assertThat(offset + length <= Integer.SIZE, "offset + length <= Integer.SIZE");
        final int number = this.intValue();
        return (number << (Integer.SIZE - (offset + length)))
                >>> (Integer.SIZE - length);
    }

    default long rangedLongValue(int offset, int length) {
        Assertions.assertThat(offset >= 0, "offset >= 0");
        Assertions.assertThat(offset + length <= Long.SIZE, "offset + length <= Long.SIZE");
        final long number = this.longValue();
        return (number << (Long.SIZE - (offset + length)))
                >>> (Long.SIZE - length);
    }

    default BitOperator longValue(Consumer<Long> consumer) {
        consumer.accept(this.longValue());
        return this;
    }

    default long longValue() {
        return this.value();
    }

    default long dwordValue() {
        return this.value();
    }

    default int wordValue() {
        return this.intValue();
    }

    default BitOperator intValue(Consumer<Integer> consumer) {
        consumer.accept(this.intValue());
        return this;
    }

    default int intValue() {
        return (int) this.value();
    }

    default BitOperator shortValue(Consumer<Short> consumer) {
        consumer.accept(this.shortValue());
        return this;
    }

    default short shortValue() {
        return (short) this.value();
    }

    default BitOperator byteValue(Consumer<Byte> consumer) {
        consumer.accept(this.byteValue());
        return this;
    }

    default byte byteValue() {
        return (byte) this.value();
    }

    default BitOperator boolValue(Consumer<Boolean> consumer) {
        consumer.accept(this.boolValue());
        return this;
    }

    default boolean boolValue() {
        return this.value() == 1L;
    }


    default BitOperator hexString(int minLeadingZeros, Consumer<String> consumer) {
        consumer.accept(this.hexString(minLeadingZeros));
        return this;
    }

    default BitOperator hexString(Consumer<String> consumer) {
        consumer.accept(this.hexString());
        return this;
    }

    default String hexString(int minLeadingZeros) {
        final String hexString = this.hexString();
        if (hexString.length() < minLeadingZeros) {
            return "0".repeat(minLeadingZeros - hexString.length()) + hexString;
        }
        return hexString;
    }

    default String hexString() {
        return Long.toHexString(this.value());
    }

    default BitOperator binaryString(int minLeadingZeros, Consumer<String> consumer) {
        consumer.accept(this.binaryString(minLeadingZeros));
        return this;
    }

    default BitOperator binaryString(Consumer<String> consumer) {
        consumer.accept(this.binaryString());
        return this;
    }

    default String binaryString(int minLeadingZeros) {
        return binaryString(minLeadingZeros, this.value());
    }

    default String binaryString() {
        return Long.toBinaryString(this.value());
    }

    static String binaryString(int minLeadingZeros, int value) {
        final String string = Integer.toBinaryString(value);
        if (string.length() < minLeadingZeros) {
            return "0".repeat(minLeadingZeros - string.length()) + string;
        }
        return string;
    }

    static String binaryString(int minLeadingZeros, long value) {
        final String string = Long.toBinaryString(value);
        if (string.length() < minLeadingZeros) {
            return "0".repeat(minLeadingZeros - string.length()) + string;
        }
        return string;
    }
}
