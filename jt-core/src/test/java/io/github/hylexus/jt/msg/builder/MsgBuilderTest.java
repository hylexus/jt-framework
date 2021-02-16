package io.github.hylexus.jt.msg.builder;

import io.github.hylexus.jt.msg.builder.jt808.Jt808MsgBodyBuilder;
import org.junit.Assert;
import org.junit.Test;

public class MsgBuilderTest {

    @Test
    public void testAppend() {
        final MsgBuilder builder = Jt808MsgBodyBuilder.newBuilder(8, 16);
        builder.appendDword(1);
        builder.appendDword(1L);
        builder.appendDword(1);
        builder.appendDword(1);

        Assert.assertArrayEquals(new byte[]{0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1}, builder.build());
    }
}