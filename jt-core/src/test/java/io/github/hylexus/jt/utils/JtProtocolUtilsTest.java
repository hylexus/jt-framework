package io.github.hylexus.jt.utils;

import org.junit.Test;

/**
 * @author hylexus
 */
public class JtProtocolUtilsTest {

    @Test
    public void testSetBitRange() {
        // 0b101010001110011
        // 0b101010001110111
        System.out.println(Integer.toBinaryString(JtProtocolUtils.setBitRange(0b10101, 10, 0b0_1_0_111_0001110011, 0)));
        System.out.println(Integer.toBinaryString(JtProtocolUtils.setBitRange(0b101, 3, 0b0_1_0_111_0001110011, 10)));
        System.out.println(Integer.toBinaryString(JtProtocolUtils.setBitRange(0b1, 1, 0b0_1_0_111_0001110011, 13)));
        System.out.println(Integer.toBinaryString(JtProtocolUtils.setBitRange(0b1, 1, 0b0_1_0_111_0001110011, 14)));
        System.out.println(Integer.toBinaryString(JtProtocolUtils.setBitRange(0b1, 1, 0b0_1_0_111_0001110011, 15)));
        System.out.println(Integer.toBinaryString(JtProtocolUtils.setBitRange(0b111001, 10, 0b0_1_0_111_0001110011, 0)));
        // setBitRange(0b10101, 10, 0b1000111000101010001110011, 3);

    }

    void setBitRange(int from, int length, int target, int offset) {
        int mask = Integer.SIZE - offset;
        System.out.println(toBinString(target));
        System.out.println(toBinString(from));
        System.out.println(toBinString(~(((1 << length) - 1) << offset)));
        System.out.println(toBinString((~(((1 << length) - 1) << offset)) & target));
        System.out.println(toBinString(from << offset));

        System.out.println(toBinString(((~(((1 << length) - 1) << offset)) & target) | (from << offset)));
    }

    String toBinString(int n) {
        return toBinString(n, Integer.SIZE);
    }

    String toBinString(int n, int length) {
        final String s = Integer.toBinaryString(n);
        if (s.length() >= length) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < length) {
            sb.insert(0, 0);
        }
        return sb.toString();
    }
}