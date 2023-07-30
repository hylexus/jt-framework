package io.github.hylexus.jt.jt1078.spec.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078TerminalIdConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DefaultJt1078TerminalIdConverterTest {

    @Test
    void test() {
        final Jt1078TerminalIdConverter converter = new DefaultJt1078TerminalIdConverter();
        Assertions.assertEquals("123456789000", converter.convert("123456789000"));
        Assertions.assertEquals("123456789000", converter.convert("000000000123456789000"));
        Assertions.assertEquals("123456789000", converter.convert("0000000123456789000"));
    }
}