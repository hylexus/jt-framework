package io.github.hylexus.jt.utils;

import io.github.hylexus.oaks.utils.Numbers;

import java.math.BigInteger;
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

    /**
     * @deprecated use {@link #mapIf(Function, Boolean)} instead.
     */
    //@Deprecated(forRemoval = true, since = "2.1.5")
    @Deprecated
    default BitOperator map(Function<Long, Long> mapper, Boolean condition) {
        if (condition) {
            return map(mapper);
        }
        return this;
    }

    /**
     * @deprecated use {@link #mapIf(Function, Predicate)} instead.
     */
    //@Deprecated(forRemoval = true, since = "2.1.5")
    @Deprecated
    default BitOperator map(Function<Long, Long> mapper, Predicate<Long> predicate) {
        if (predicate.test(this.value())) {
            return map(mapper);
        }
        return this;
    }

    /**
     * @deprecated use {@link #mapIf(Function, Supplier)} instead.
     */
    //@Deprecated(forRemoval = true, since = "2.1.5")
    @Deprecated
    default BitOperator map(Function<Long, Long> mapper, Supplier<Boolean> supplier) {
        return map(mapper, it -> supplier.get());
    }

    /**
     * @since 2.1.5
     */
    default BitOperator mapIf(Function<Long, Long> mapper, Boolean condition) {
        if (condition) {
            return map(mapper);
        }
        return this;
    }

    /**
     * @since 2.1.5
     */
    default BitOperator mapIf(Function<Long, Long> mapper, Predicate<Long> predicate) {
        if (predicate.test(this.value())) {
            return map(mapper);
        }
        return this;
    }

    /**
     * @since 2.1.5
     */
    default BitOperator mapIf(Function<Long, Long> mapper, Supplier<Boolean> supplier) {
        return map(mapper, it -> supplier.get());
    }

    /**
     * @param offset 偏移量: 0 ~ 63
     * @param status {@code true}: 将 {@code offset} 偏移处的 bit 设置为 {@code 1};
     *               {@code false}: 将 {@code offset} 偏移处的 bit 设置为 {@code 0};
     * @return {@code offset} 偏移处的 bit 被设置为 {@code status} 后的 {@code BitOperator} 实例
     * @since 2.1.5
     */
    default BitOperator setWithStatus(int offset, boolean status) {
        if (status) {
            return this.set(offset);
        }
        return this.reset(offset);
    }

    /**
     * @param offset    偏移量: 0 ~ 63
     * @param status    {@code true}: 将 {@code offset} 偏移处的 bit 设置为 {@code 1};
     *                  {@code false}: 将 {@code offset} 偏移处的 bit 设置为 {@code 0};
     * @param condition 为 {@code true} 时，执行 {@link  #setWithStatus(int, boolean)} 操作，否则不执行任何操作
     * @since 2.1.5
     */
    default BitOperator setWithStatusIf(int offset, boolean status, boolean condition) {
        if (condition) {
            return this.setWithStatus(offset, status);
        }
        return this;
    }

    /**
     * @param offset    偏移量: 0 ~ 63
     * @param status    {@code true}: 将 {@code offset} 偏移处的 bit 设置为 {@code 1};
     *                  {@code false}: 将 {@code offset} 偏移处的 bit 设置为 {@code 0};
     * @param condition 为 {@code true} 时，执行 {@link  #setWithStatus(int, boolean)} 操作，否则不执行任何操作
     * @since 2.1.5
     */
    default BitOperator setWithStatusIf(int offset, boolean status, Supplier<Boolean> condition) {
        if (condition.get()) {
            return this.setWithStatus(offset, status);
        }
        return this;
    }

    /**
     * @param offset    偏移量: 0 ~ 63
     * @param status    {@code true}: 将 {@code offset} 偏移处的 bit 设置为 {@code 1};
     *                  {@code false}: 将 {@code offset} 偏移处的 bit 设置为 {@code 0};
     * @param predicate 为 {@code true} 时，执行 {@link  #setWithStatus(int, boolean)} 操作，否则不执行任何操作
     * @since 2.1.5
     */
    default BitOperator setWithStatusIf(int offset, boolean status, Predicate<Long> predicate) {
        if (predicate.test(this.value())) {
            return this.setWithStatus(offset, status);
        }
        return this;
    }

    /**
     * @deprecated use {@link #setWithStatus(int, boolean)} instead.
     */
    //@Deprecated(forRemoval = true, since = "2.1.5")
    @Deprecated
    default BitOperator set(int offset, boolean status) {
        return this.setWithStatus(offset, status);
    }

    default BitOperator set(int offset) {
        Assertions.assertThat(offset >= 0 && offset < Long.SIZE, "offset >= 0 && offset < Long.SIZE");
        return map(it -> Numbers.setBitAt(it, offset));
    }

    /**
     * @deprecated use {@link #setIf(int, Predicate)} instead.
     */
    //@Deprecated(forRemoval = true, since = "2.1.5")
    @Deprecated
    default BitOperator set(int offset, Predicate<Long> predicate) {
        if (predicate.test(this.value())) {
            return this.set(offset);
        }
        return this;
    }

    /**
     * @since 2.1.5
     */
    default BitOperator setIf(int offset, boolean condition) {
        if (condition) {
            return this.set(offset);
        }
        return this;
    }

    /**
     * @since 2.1.5
     */
    default BitOperator setIf(int offset, Predicate<Long> predicate) {
        if (predicate.test(this.value())) {
            return this.set(offset);
        }
        return this;
    }

    /**
     * @since 2.1.5
     */
    default BitOperator setIf(int offset, Supplier<Boolean> condition) {
        if (condition.get()) {
            return this.set(offset);
        }
        return this;
    }

    default BitOperator setRange(int offset, int length) {
        Assertions.assertThat(offset >= 0 && offset < Long.SIZE, "offset >= 0 && offset < Long.SIZE");
        Assertions.assertThat(length > 0 && offset + length <= Long.SIZE, "length > 0 && offset+length <= Long.SIZE");
        return map(it -> it | (~0L >>> (Long.SIZE - length) << offset));
    }

    /**
     * @since 2.1.5
     */
    default BitOperator setRangeIf(int offset, int length, boolean condition) {
        if (condition) {
            return setRange(offset, length);
        }
        return this;
    }

    /**
     * @since 2.1.5
     */
    default BitOperator setRangeIf(int offset, int length, Supplier<Boolean> condition) {
        if (condition.get()) {
            return setRange(offset, length);
        }
        return this;
    }

    /**
     * @since 2.1.5
     */
    default BitOperator setRangeIf(int offset, int length, Predicate<Long> predicate) {
        if (predicate.test(this.value())) {
            return setRange(offset, length);
        }
        return this;
    }

    /**
     * @deprecated use {@link #resetIf(int, Predicate)} instead.
     */
    //@Deprecated(forRemoval = true, since = "2.1.5")
    @Deprecated
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

    /**
     * @since 2.1.5
     */
    default BitOperator resetIf(int offset, Predicate<Long> predicate) {
        if (predicate.test(this.value())) {
            return this.reset(offset);
        }
        return this;
    }

    /**
     * @since 2.1.5
     */
    default BitOperator resetIf(int offset, Supplier<Boolean> condition) {
        if (condition.get()) {
            return this.reset(offset);
        }
        return this;
    }

    /**
     * @since 2.1.5
     */
    default BitOperator resetIf(int offset, Boolean condition) {
        if (condition) {
            return this.reset(offset);
        }
        return this;
    }

    default BitOperator resetRange(int offset, int length) {
        Assertions.assertThat(offset >= 0 && offset < Long.SIZE, "offset >= 0 && offset < Long.SIZE");
        Assertions.assertThat(length > 0 && offset + length <= Long.SIZE, "length > 0 && offset + length <= Long.SIZE");
        return map(it -> it & (~(~0L >>> (Long.SIZE - length) << offset)));
    }

    /**
     * @since 2.1.5
     */
    default BitOperator resetRangeIf(int offset, int length, boolean condition) {
        if (condition) {
            return this.resetRange(offset, length);
        }
        return this;
    }

    /**
     * @since 2.1.5
     */
    default BitOperator resetRangeIf(int offset, int length, Supplier<Boolean> condition) {
        if (condition.get()) {
            return this.resetRange(offset, length);
        }
        return this;
    }

    /**
     * @since 2.1.5
     */
    default BitOperator resetRangeIf(int offset, int length, Predicate<Long> predicate) {
        if (predicate.test(this.value())) {
            return this.resetRange(offset, length);
        }
        return this;
    }

    default int get(int offset) {
        return Numbers.getBitAt(this.value(), offset);
    }

    default int rangedIntValue(int offset, int length) {
        Assertions.assertThat(offset >= 0, "offset >= 0");
        Assertions.assertThat(length > 0 && offset + length <= Long.SIZE, "length > 0 && offset + length <= Long.SIZE");
        final int number = this.intValue();
        return (number << (Integer.SIZE - (offset + length)))
                >>> (Integer.SIZE - length);
    }

    /**
     * @since 2.1.5
     */
    default long rangedUnsignedIntValue(int offset, int length) {
        final int x = this.rangedIntValue(offset, length);
        return ((long) x) & 0XFFFFFFFFL;
    }

    default long rangedLongValue(int offset, int length) {
        Assertions.assertThat(offset >= 0, "offset >= 0");
        Assertions.assertThat(length > 0 && offset + length <= Long.SIZE, "length > 0 && offset + length <= Long.SIZE");
        final long number = this.longValue();
        return (number << (Long.SIZE - (offset + length)))
                >>> (Long.SIZE - length);
    }

    /**
     * @since 2.1.5
     */
    default BigInteger rangedUnsignedLongValue(int offset, int length) {
        return toUnsignedBigInteger(this.rangedLongValue(offset, length));
    }

    default BitOperator longValue(Consumer<Long> consumer) {
        consumer.accept(this.longValue());
        return this;
    }

    default long longValue() {
        return this.value();
    }

    /**
     * @return 等值的 无符号 {@link BigInteger} 对象
     * @since 2.1.5
     */
    default BigInteger unsignedLongValue() {
        return toUnsignedBigInteger(this.value());
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
            return Jdk8Adapter.stringRepeat("0", minLeadingZeros - hexString.length()) + hexString;
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
            return Jdk8Adapter.stringRepeat("0", minLeadingZeros - string.length()) + string;
        }
        return string;
    }

    static String binaryString(int minLeadingZeros, long value) {
        final String string = Long.toBinaryString(value);
        if (string.length() < minLeadingZeros) {
            return Jdk8Adapter.stringRepeat("0", minLeadingZeros - string.length()) + string;
        }
        return string;
    }

    /**
     * 这个方法是从 {@code java.lang.Long#toUnsignedBigInteger(long)} 中复制过来的。
     *
     * @since 2.1.5
     */
    static BigInteger toUnsignedBigInteger(long i) {
        if (i >= 0L) {
            return BigInteger.valueOf(i);
        } else {
            int upper = (int) (i >>> 32);
            int lower = (int) i;

            // return (upper << 32) + lower
            return (BigInteger.valueOf(Integer.toUnsignedLong(upper)))
                    .shiftLeft(32)
                    .add(BigInteger.valueOf(Integer.toUnsignedLong(lower)));
        }
    }
}
