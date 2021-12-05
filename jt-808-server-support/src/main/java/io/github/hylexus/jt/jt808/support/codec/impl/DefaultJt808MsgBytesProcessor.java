
package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.exception.Jt808MsgEscapeException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hylexus
 */
@Slf4j
public class DefaultJt808MsgBytesProcessor implements Jt808MsgBytesProcessor {
    final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    @Override
    public ByteBuf doEscapeForReceive(ByteBuf byteBuf) throws Jt808MsgEscapeException {
        // TODO 转义+单元测试
        final int readableBytes = byteBuf.readableBytes();

        final byte packageDelimiter = 0x7d;
        int from = byteBuf.readerIndex();
        int indexOf = byteBuf.indexOf(from, readableBytes, packageDelimiter);
        if (indexOf < 0) {
            return byteBuf;
        }
        final List<ByteBuf> byteBufList = new ArrayList<>();

        do {
            final byte current = byteBuf.getByte(indexOf);
            final byte next = byteBuf.getByte(indexOf + 1);
            if (current == 0x7d && next == 0x01) {
                if (from < indexOf) {
                    byteBufList.add(byteBuf.slice(from, indexOf - from));
                }
                byteBufList.add(allocator.buffer().writeByte(0x7d));
                from = indexOf + 2;
            } else if (current == 0x7d && next == 0x02) {
                if (from < indexOf) {
                    byteBufList.add(byteBuf.slice(from, indexOf - from));
                }
                byteBufList.add(allocator.buffer().writeByte(0x7e));
                from = indexOf + 2;
            } else {
                log.warn("0x7d should be followed by 0x01 or 0x02, but " + next);
                if (from < indexOf) {
                    byteBufList.add(byteBuf.slice(from, indexOf - from + 1));
                }
                from = indexOf + 1;
            }
        } while (from < readableBytes && (indexOf = byteBuf.indexOf(from, readableBytes, packageDelimiter)) >= 0);

        byteBufList.add(byteBuf.slice(from, readableBytes - from));

        final CompositeByteBuf compositeBuffer = allocator.compositeBuffer(byteBufList.size());
        byteBufList.forEach(buf -> compositeBuffer.addComponent(true, buf));
        return compositeBuffer;
    }

    @Override
    public ByteBuf doEscapeForSend(ByteBuf byteBuf) throws Jt808MsgEscapeException {
        // TODO 转义
        return byteBuf;
    }

    @Override
    public byte calculateCheckSum(ByteBuf byteBuf) {
        byte sum = 0;
        while (byteBuf.isReadable()) {
            sum ^= byteBuf.readByte();
        }
        byteBuf.resetReaderIndex();
        return sum;
    }
}

