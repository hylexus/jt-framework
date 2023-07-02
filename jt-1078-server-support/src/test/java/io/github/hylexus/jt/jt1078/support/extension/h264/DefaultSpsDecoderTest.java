package io.github.hylexus.jt.jt1078.support.extension.h264;

import io.github.hylexus.jt.jt1078.support.extension.h264.impl.DefaultSpsDecoder;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DefaultSpsDecoderTest {

    SpsDecoder decoder = new DefaultSpsDecoder();

    @Test
    void test() {
        final String hex = "6764002AAC156A0B012640";
        final ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer().writeBytes(HexStringUtils.hexString2Bytes(hex));
        try {
            final Sps sps = decoder.decodeSps(byteBuf);
            Assertions.assertEquals(100, sps.getProfileIdc());
            Assertions.assertEquals(42, sps.getLevelIdc());
            Assertions.assertEquals(0, sps.getProfileCompat());
            Assertions.assertEquals(704, sps.getWidth());
            Assertions.assertEquals(576, sps.getHeight());
            Assertions.assertEquals(1, byteBuf.refCnt());
        } finally {
            byteBuf.release();
        }
    }
}