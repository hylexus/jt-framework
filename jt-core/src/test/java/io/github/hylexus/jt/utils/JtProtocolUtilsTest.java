package io.github.hylexus.jt.utils;

import org.junit.Test;

/**
 * @author hylexus
 */
public class JtProtocolUtilsTest {

    @Test
    public void testSetBitRange() {
        System.out.println(JtProtocolUtils.setBitRange(0b101010001110011, 7, 0b101, 3));
    }
}