package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.exception.Jt808MsgEscapeException;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hylexus
 */
@Slf4j
@BuiltinComponent
public class DefaultJt808MsgBytesProcessor implements Jt808MsgBytesProcessor {
    private final ByteBufAllocator allocator;

    private static final byte BYTE_7D = 0x7d;
    private static final byte BYTE_7E = 0x7e;

    public DefaultJt808MsgBytesProcessor(ByteBufAllocator allocator) {
        this.allocator = allocator;
    }

    @Override
    public ByteBuf doEscapeForReceive(ByteBuf byteBuf) throws Jt808MsgEscapeException {
        final int readableBytes = byteBuf.readableBytes();

        final byte delimiter = 0x7d;
        int from = byteBuf.readerIndex();
        int indexOf = byteBuf.indexOf(from, readableBytes, delimiter);
        if (indexOf < 0) {
            return byteBuf.retain();
        }

        final List<ByteBuf> byteBufList = new ArrayList<>();
        do {
            final byte current = byteBuf.getByte(indexOf);
            final byte next = byteBuf.getByte(indexOf + 1);
            if (current == 0x7d && next == 0x01) {
                if (from <= indexOf) {
                    // xxx7D01xxx --> xxx7Dxxx
                    byteBufList.add(byteBuf.retainedSlice(from, indexOf - from + 1));
                }
                //byteBufList.add(allocator.buffer().writeByte(0x7d));
                from = indexOf + 2;
            } else if (current == 0x7d && next == 0x02) {
                if (from <= indexOf) {
                    // xxx7D02xxx --> xxx7Exxx
                    byteBufList.add(byteBuf.retainedSlice(from, indexOf - from + 1));
                }
                byteBuf.setByte(indexOf, 0x7E);
                //byteBufList.add(allocator.buffer().writeByte(0x7e));
                from = indexOf + 2;
            } else {
                log.warn("0x7d should be followed by 0x01 or 0x02, but " + next);
                if (from <= indexOf) {
                    byteBufList.add(byteBuf.retainedSlice(from, indexOf - from + 1));
                }
                from = indexOf + 1;
            }
        } while (from < readableBytes && (indexOf = byteBuf.indexOf(from, readableBytes, delimiter)) >= 0);

        byteBufList.add(byteBuf.retainedSlice(from, readableBytes - from));
        return allocator.compositeBuffer(byteBufList.size()).addComponents(true, byteBufList);
    }

    @Override
    public ByteBuf doEscapeForSend(ByteBuf byteBuf) throws Jt808MsgEscapeException {
        int readableBytes = byteBuf.readableBytes();
        int from = 0;
        int indexOf = nextIndexOf(byteBuf, from, readableBytes);
        if (indexOf < 0) {
            return byteBuf;
        }

        final List<ByteBuf> bufList = new ArrayList<>();
        try {
            do {
                if (from < indexOf) {
                    bufList.add(byteBuf.retainedSlice(from, indexOf - from));
                }
                final byte current = byteBuf.getByte(indexOf);
                if (current == BYTE_7D) {
                    bufList.add(allocator.buffer().writeByte(0x7d).writeByte(0x01));
                } else if (current == BYTE_7E) {
                    bufList.add(allocator.buffer().writeByte(0x7d).writeByte(0x02));
                }
                from = indexOf + 1;
            } while (from < readableBytes && (indexOf = nextIndexOf(byteBuf, from, readableBytes)) > 0);
            bufList.add(byteBuf.retainedSlice(from, readableBytes - from));
        } finally {
            JtProtocolUtils.release(byteBuf);
        }
        return allocator.compositeBuffer(bufList.size()).addComponents(true, bufList);
    }


    private int nextIndexOf(ByteBuf byteBuf, int from, int to) {
        final int index1 = byteBuf.indexOf(from, to, BYTE_7E);
        final int index2 = byteBuf.indexOf(from, to, BYTE_7D);
        if (index1 < 0 && index2 < 0) {
            return -1;
        }
        if (index1 < 0) {
            return index2;
        }
        if (index2 < 0) {
            return index1;
        }
        return Math.min(index1, index2);
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

