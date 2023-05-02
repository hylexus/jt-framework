package io.github.hylexus.jt.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BitOperatorTest {

    @Test
    void testBit() {
        // 64个bit: 全是0
        final long x = 0;
        // 第 1、3、22 个 bit 被置为 1，其他位没变
        final BitOperator operator = BitOperator.mutable(x).set(1).set(3).set(22);
        assertEquals("0000000000000000000000000000000000000000010000000000000000001010", operator.binaryString(64));
        assertEquals("00000000010000000000000000001010", operator.binaryString(32));
        assertEquals("010000000000000000001010", operator.binaryString(24));

        // 第 1 个 bit 被置为 0
        final BitOperator operator1 = operator.reset(1);
        assertEquals("010000000000000000001000", operator1.binaryString(24));

        final int y = 0b11111111;
        // 第 3 个 bit 被置为 0
        assertEquals("11110111", BitOperator.mutable(y).reset(3).binaryString(8));
        // 从第 3 个 bit 开始, 连续将 2 个 bit 置为 0
        assertEquals("11100111", BitOperator.mutable(y).resetRange(3, 2).binaryString(8));

        final long z = 0b00001000;
        // z 的第 3 个 bit 是 1
        assertEquals(1, BitOperator.mutable(z).get(3));

        assertEquals(0b11, BitOperator.mutable(x).set(1).set(0).wordValue());
        assertEquals(3, BitOperator.mutable(x).set(1).set(0).wordValue());
    }

    @Test
    void test1() {
        int x = -0b001000001011111100001;
        int offset = 2;
        int length = 2;
        // 3,5
        System.out.println(BitOperator.binaryString(x, 64));
        System.out.println(BitOperator.binaryString(~(~0L >>> (64 - length) << offset), 64));
        System.out.println(BitOperator.binaryString(x & (~(~0L >>> (64 - length) << offset)), 64));
        System.out.println(BitOperator.mutable(x).resetRange(offset, length).binaryString(64));

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
        final BitOperator instance1 = BitOperator.mutable(1);
        final BitOperator instance2 = instance1.map(it -> it * 2);
        // instance1 == instance2
        Assertions.assertSame(instance2, instance1);
    }
}