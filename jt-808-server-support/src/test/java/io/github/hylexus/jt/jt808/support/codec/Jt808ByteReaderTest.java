package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author hylexus
 */
class Jt808ByteReaderTest {

    @Test
    void read() {
        final ByteBuf originalByteBuf = ByteBufAllocator.DEFAULT.buffer();

        try {
            JtProtocolUtils.writeBcd(originalByteBuf, "10203040");
            JtProtocolUtils.writeDword(originalByteBuf, 123);
            final ByteBuf afterRead = Jt808ByteReader.of(originalByteBuf)
                    .readBcd(4, bcdString -> assertEquals("10203040", bcdString))
                    .readUnsignedDword(dword -> assertEquals(123, dword))
                    .readable();

            // 读取完成之后，Jt808ByteReader 不会改变内部 originalByteBuf 的引用指向
            assertSame(originalByteBuf, afterRead);

        } finally {
            // Jt808ByteReader 只是个辅助类，不会改变内部 readable() 指向的 ByteBuf 的 refCnt
            // 应该在恰当的时机释放被包装的 originalByteBuf
            originalByteBuf.release();
            assertEquals(0, originalByteBuf.refCnt());
        }

    }

}