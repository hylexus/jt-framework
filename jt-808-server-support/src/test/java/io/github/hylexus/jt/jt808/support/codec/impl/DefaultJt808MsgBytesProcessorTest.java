package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * @author hylexus
 */
public class DefaultJt808MsgBytesProcessorTest {

    private final Jt808MsgBytesProcessor bytesProcessor = new DefaultJt808MsgBytesProcessor();
    private final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    @Test
    public void testDoEscapeForReceive() {
//        final byte[] result1 = escapeForReceive(new byte[]{0, 0, 0, 0});
//        System.out.println(bytesToString(result1));
//
//        final byte[] result2 = escapeForReceive(new byte[]{0x7d, 0x02, 0, 0});
//        System.out.println(bytesToString(result2));
//
//        final byte[] result3 = escapeForReceive(new byte[]{0x7d, 0x02, 0x7d, 0x01});
//        System.out.println(bytesToString(result3));

//        final byte[] result4 = escapeForReceive(new byte[]{0x7d, 0x02, 0, 1, 2, 3, 0x7d, 0x02, 4, 5, 0x7d, 0x01});
//        System.out.println(bytesToString(result4));

        final byte[] result5 = escapeForReceive(new byte[]{1, 2, 0, 1, 2, 3, 0x7d, 0x02, 4, 5, 0x7d, 0x02, 9, 9, 0x7d, 0x01, 0x7d, 0x02, 1});
        System.out.println(bytesToString(result5));
    }

    String bytesToString(byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            final String str = Integer.toHexString(b);
            sb.append(str.length() == 1 ? "0" + str : str).append(" ");
        }
        return sb.toString();
    }

    byte[] escapeForReceive(byte[] in) {
        final ByteBuf byteBuf = this.bytesProcessor.doEscapeForReceive(generateFromByteArray(in));
        return fromByteBuf(byteBuf);
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