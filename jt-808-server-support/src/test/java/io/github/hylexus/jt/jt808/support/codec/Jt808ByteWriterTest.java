package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author hylexus
 */
class Jt808ByteWriterTest {

    @Test
    void write() {
        final ByteBuf originalByteBuf = ByteBufAllocator.DEFAULT.buffer(128);
        try {
            final Jt808ByteWriter writer = Jt808ByteWriter.of(originalByteBuf);
            final ByteBuf afterWrite = writer
                    .writeBcd("10203040")
                    .writeString("STRING")
                    .writeDWord(100)
                    // 这里返回的 ByteBuf 应该和初始化传入的是同一个对象
                    .writable();

            assertSame(originalByteBuf, afterWrite);

            assertEquals("10203040535452494E4700000064", HexStringUtils.byteBufToString(originalByteBuf));
            assertEquals("10203040535452494E4700000064", HexStringUtils.byteBufToString(afterWrite));

        } finally {
            // Jt808ByteWriter 只是个辅助类，不会改变内部 writable() 指向的 ByteBuf 的 refCnt
            // 应该在恰当的时机释放被包装的 originalByteBuf
            originalByteBuf.release();
            assertEquals(0, originalByteBuf.refCnt());
        }
    }
}