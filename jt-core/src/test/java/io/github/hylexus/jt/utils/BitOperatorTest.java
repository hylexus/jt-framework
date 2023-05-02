package io.github.hylexus.jt.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BitOperatorTest {

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