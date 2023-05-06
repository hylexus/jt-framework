package io.github.hylexus.jt.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.github.hylexus.jt.utils.BitOperator.binaryString;
import static io.github.hylexus.jt.utils.BitOperator.mutable;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BitOperatorTest {

    @Test
    void testBit() {
        // 64个bit: 全是0
        final long x = 0;
        // 第 1、3、22 个 bit 被置为 1，其他位没变
        final BitOperator operator = mutable(x).set(1).set(3).set(22);
        assertEquals("0000000000000000000000000000000000000000010000000000000000001010", operator.binaryString(64));
        assertEquals("00000000010000000000000000001010", operator.binaryString(32));
        assertEquals("010000000000000000001010", operator.binaryString(24));

        // 第 1 个 bit 被置为 0
        final BitOperator operator1 = operator.reset(1);
        assertEquals("010000000000000000001000", operator1.binaryString(24));

        final int y = 0b11111111;
        // 第 3 个 bit 被置为 0
        assertEquals("11110111", mutable(y).reset(3).binaryString(8));
        // 从第 3 个 bit 开始, 连续将 2 个 bit 置为 0
        assertEquals("11100111", mutable(y).resetRange(3, 2).binaryString(8));

        final long z = 0b00001000;
        // z 的第 3 个 bit 是 1
        assertEquals(1, mutable(z).get(3));

        assertEquals(0b11, mutable(x).set(1).set(0).wordValue());
        assertEquals(3, mutable(x).set(1).set(0).wordValue());
    }

    @Test
    void test() {
        assertEquals("10001010", BitOperator.immutable(0).set(1).set(7).set(3).binaryString());
        assertEquals("10001010", BitOperator.immutable(0).set(1).set(7).set(3).binaryString(8));
        assertEquals("0000000010001010", BitOperator.immutable(0).set(1).set(7).set(3).binaryString(16));

        assertEquals(0b11010110L, BitOperator.immutable(0b11111111).reset(0).reset(3).reset(5).longValue());

        assertEquals("8a", BitOperator.immutable(0).set(1).set(7).set(3).hexString());
        assertEquals("0000008a", BitOperator.immutable(0).set(1).set(7).set(3).hexString(8));
        assertEquals(0b11010110, BitOperator.immutable(0b11111111).reset(0).reset(3).reset(5).intValue());
    }

    @Test
    void testImmutable() {
        final BitOperator instance1 = BitOperator.immutable(1);
        final BitOperator instance2 = instance1.map(it -> it * 2);
        // instance1 != instance2
        Assertions.assertNotSame(instance2, instance1);
    }

    @Test
    void testMutable() {
        final BitOperator instance1 = mutable(1);
        final BitOperator instance2 = instance1.map(it -> it * 2);
        // instance1 == instance2
        Assertions.assertSame(instance2, instance1);
    }

    @Test
    void testMapCondition() {
        final long original = 0b11111111L;

        assertEquals(original, mutable(original).map(it -> 0b11111110L, false).value());
        assertEquals(0b11111110L, mutable(original).map(it -> 0b11111110L, true).value());

        assertEquals(original, mutable(original).map(it -> 0b11111110L, it -> false).value());
        assertEquals(0b11111110L, mutable(original).map(it -> 0b11111110L, it -> true).value());

        assertEquals(original, mutable(original).map(it -> 0b11111110L, () -> false).value());
        assertEquals(0b11111110L, mutable(original).map(it -> 0b11111110L, () -> true).value());
    }

    @Test
    void testSet() {

        assertEquals(0b11111010L, mutable(0b11111111L).set(0, false).set(2, false).longValue());
        assertEquals(0b11111111L, mutable(0b11111000L).set(0, true).set(1, true).set(2, true).longValue());

        assertEquals(0b1110, mutable(0b1110).set(0, it -> false).intValue());
        assertEquals(0b1111, mutable(0b1110).set(0, it -> true).intValue());
    }

    @Test
    void testSetRange() {
        assertEquals(0b00001110L, mutable(0b00000000).setRange(1, 3).longValue());
    }

    @Test
    void testReset() {
        mutable(0b1111L)
                .reset(0)
                .reset(2)
                .longValue(it -> assertEquals(0b1010L, it))
                .reset(1, it -> false)
                .longValue(it -> assertEquals(0b1010L, it))
                .reset(1, it -> true)
                .longValue(it -> assertEquals(0b1000L, it));
    }

    @Test
    void testValue() {
        final int value = 0b1111;
        assertEquals(mutable(value).intValue(), mutable(value).wordValue());
        assertEquals(mutable(value).longValue(), mutable(value).dwordValue());

        mutable(1)
                .boolValue(b -> assertEquals(b, mutable(1).boolValue()))
                .reset(0)
                .boolValue(Assertions::assertFalse);

        mutable(0b1111)
                .byteValue(b -> assertEquals(b, mutable(0b1111).byteValue()))
                .reset(1)
                .binaryString(it -> assertEquals("1101", it))
                .binaryString(4, it -> assertEquals("1101", it))
                .binaryString(2, it -> assertEquals("1101", it))
                .binaryString(2, it -> binaryString(0b1101, 2))
                .hexString(it -> assertEquals("d", it))
                .hexString(4, it -> assertEquals("000d", it))
                .map(it -> 0xffffL)
                .hexString(4, it -> assertEquals("ffff", it));

        assertEquals("1111", binaryString(0, 0b1111));
        assertEquals("01111", binaryString(5, 0b1111));

        mutable(0b1111).shortValue(it -> assertEquals(mutable(0b1111).shortValue(), it))
                .binaryString(4, it -> assertEquals("1111", it));

        mutable(0b1111).intValue(it -> assertEquals(mutable(0b1111).intValue(), it))
                .binaryString(4, it -> assertEquals("1111", it));

        mutable(0b1111).longValue(it -> assertEquals(mutable(0b1111).longValue(), it))
                .binaryString(4, it -> assertEquals("1111", it));
    }

    @Test
    void testIntValue() {
        assertEquals(0b00000011, mutable(0b11111111).rangedIntValue(0, 2));
        assertEquals(0b00000000, mutable(0b11111001).rangedIntValue(1, 2));
        assertEquals(0b00000100, mutable(0b11111001).rangedIntValue(1, 3));
        assertEquals(0b00000101, mutable(0b10101010).rangedIntValue(1, 4));
        assertEquals(0b1010, mutable(0b10101010).rangedIntValue(4, 4));
        assertEquals(0b10101010, mutable(0b10101010).rangedIntValue(0, 32));

        assertEquals(0b00000011, mutable(0b11111111).rangedLongValue(0, 2));
        assertEquals(0b00000000, mutable(0b11111001).rangedLongValue(1, 2));
        assertEquals(0b00000100, mutable(0b11111001).rangedLongValue(1, 3));
        assertEquals(0b00000101, mutable(0b10101010).rangedLongValue(1, 4));
        assertEquals(0b1010, mutable(0b10101010).rangedLongValue(4, 4));
        assertEquals(0b10101010, mutable(0b10101010).rangedLongValue(0, 64));
    }
}