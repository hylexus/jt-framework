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

    default BitOperator resetRange(int offset, int length) {
        Assertions.assertThat(offset >= 0 && offset < Long.SIZE, "offset >= 0 && offset < Long.SIZE");
        return map(it -> it & (~(~0L >>> (Long.SIZE - length) << offset)));
    }

    default BitOperator reset(int offset) {
        Assertions.assertThat(offset >= 0 && offset < Long.SIZE, "offset >= 0 && offset < Long.SIZE");
        return map(it -> Numbers.resetBitAt(it, offset));
    }

    default int get(int offset) {
        return Numbers.getBitAt(this.value(), offset);
    }

    default BitOperator longValue(int start, int end, Consumer<Long> consumer) {
        consumer.accept(this.longValue(start, end));
        return this;
    }

    default long longValue(int start, int end) {
        Assertions.assertThat(start >= 0 && start < Long.BYTES, "offset >= 0 && offset < Long.BYTES");
        Assertions.assertThat(end >= 0 && end < Long.BYTES, "offset >= 0 && offset < Long.BYTES");
        Assertions.assertThat(start <= end, "start <= end");
        return Numbers.getBitRangeAsLong(this.value(), start, end);
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

    default BitOperator intValue(int start, int end, Consumer<Integer> consumer) {
        consumer.accept(this.intValue(start, end));
        return this;
    }

    default int intValue(int start, int end) {
        // TODO end,len??? ---> len
        Assertions.assertThat(start >= 0 && start < Long.BYTES, "offset >= 0 && offset < Long.BYTES");
        Assertions.assertThat(end >= 0 && end < Long.BYTES, "offset >= 0 && offset < Long.BYTES");
        Assertions.assertThat(start <= end, "start <= end");
        return Numbers.getBitRangeAsInt((int) this.value(), start, end);
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

    default BitOperator binaryString(Consumer<String> consumer) {
        consumer.accept(this.binaryString());
        return this;
    }

    static String binaryString(int value, int leadingZeros) {
        final String string = Integer.toBinaryString(value);
        if (string.length() < leadingZeros) {
            return "0".repeat(leadingZeros - string.length()) + string;
        }
        return string;
    }

    static String binaryString(long value, int leadingZeros) {
        final String string = Long.toBinaryString(value);
        if (string.length() < leadingZeros) {
            return "0".repeat(leadingZeros - string.length()) + string;
        }
        return string;
    }

    default String binaryString(int leadingZeros) {
        return binaryString(this.value(), leadingZeros);
    }

    default String binaryString() {
        return Long.toBinaryString(this.value());
    }

    default BitOperator hexString(Consumer<String> consumer) {
        consumer.accept(this.hexString());
        return this;
    }

    default String hexString(int leadingZeros) {
        final String hexString = this.hexString();
        if (hexString.length() < leadingZeros) {
            return "0".repeat(leadingZeros - hexString.length()) + hexString;
        }
        return hexString;
    }

    default String hexString() {
        return Long.toHexString(this.value());
    }

}
