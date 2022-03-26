package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author hylexus
 */
public class DefaultJt808MsgBytesProcessorTest {

    private final Jt808MsgBytesProcessor bytesProcessor = new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT);
    private final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    @Test
    public void testDoEscapeForReceive() {
        final byte[] result1 = escapeForReceive(new byte[]{0, 0, 0, 0});
        assertEquals(4, result1.length);
        assertArrayEquals(new byte[]{0, 0, 0, 0}, result1);

        final byte[] result2 = escapeForReceive(new byte[]{0x7d, 0x02, 0, 0});
        assertEquals(3, result2.length);
        assertEquals(0x7e, result2[0]);

        final byte[] result3 = escapeForReceive(new byte[]{0x7d, 0x02, 0x7d, 0x01});
        assertEquals(2, result3.length);
        assertEquals(0x7e, result3[0]);
        assertEquals(0x7d, result3[1]);

        final byte[] result4 = escapeForReceive(new byte[]{0x7d, 0x02, 0, 1, 2, 3, 0x7d, 0x02, 4, 5, 0x7d, 0x01});
        assertArrayEquals(new byte[]{0x7e, 0, 1, 2, 3, 0x7e, 4, 5, 0x7d}, result4);

        final byte[] result5 = escapeForReceive(new byte[]{1, 2, 0, 1, 2, 3, 0x7d, 0x02, 4, 5, 0x7d, 0x02, 9, 9, 0x7d, 0x01, 0x7d, 0x02, 1});
        assertArrayEquals(new byte[]{1, 2, 0, 1, 2, 3, 0x7e, 4, 5, 0x7e, 9, 9, 0x7d, 0x7e, 1}, result5);

        final byte[] result6 = escapeForReceive(new byte[]{0x7d, 0x02, 0, 0, 0, 0});
        assertArrayEquals(new byte[]{0x7e, 0, 0, 0, 0}, result6);

        final byte[] result7 = escapeForReceive(new byte[]{0x7d, 0x22, 0, 0, 0, 0});
        assertArrayEquals(new byte[]{0x7d, 0x22, 0, 0, 0, 0}, result7);
    }

    @Test
    public void testDoEscapeForSend() {
        final byte[] bytes1 = escapeForSend(new byte[]{0x7d, 1, 0x7e, 2, 3, 0x7d, 0x7d, 0x7e});
        assertArrayEquals(new byte[]{0x7d, 0x01, 1, 0x7d, 0x02, 2, 3, 0x7d, 0x01, 0x7d, 0x01, 0x7d, 0x02}, bytes1);

        assertArrayEquals(new byte[]{}, escapeForSend(new byte[]{}));
        assertArrayEquals(new byte[]{0}, escapeForSend(new byte[]{0}));
        assertArrayEquals(new byte[]{0, 0}, escapeForSend(new byte[]{0, 0}));
        assertArrayEquals(new byte[]{1, 0, 1}, escapeForSend(new byte[]{1, 0, 1}));
        assertArrayEquals(
                new byte[]{1, 2, 3, 0x7d, 0x01, 3, 2, 1, 0x7d, 0x01, 0x7d, 0x02, 0x7d, 0x01},
                escapeForSend(new byte[]{1, 2, 3, 0x7d, 3, 2, 1, 0x7d, 0x7e, 0x7d})
        );
    }

    byte[] escapeForReceive(byte[] bytes) {
        final ByteBuf byteBuf = this.bytesProcessor.doEscapeForReceive(generateFromByteArray(bytes));
        try {
            return fromByteBuf(byteBuf);
        } finally {
            JtProtocolUtils.release(byteBuf);
        }
    }

    byte[] escapeForSend(byte[] bytes) {
        final ByteBuf byteBuf = this.bytesProcessor.doEscapeForSend(generateFromByteArray(bytes));
        try {
            return fromByteBuf(byteBuf);
        } finally {
            JtProtocolUtils.release(byteBuf);
        }
    }

    ByteBuf generateFromByteArray(byte[] bytes) {
        return allocator.buffer().writeBytes(bytes);
    }

    byte[] fromByteBuf(ByteBuf byteBuf) {
        final byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, bytes);
        return bytes;
    }
}