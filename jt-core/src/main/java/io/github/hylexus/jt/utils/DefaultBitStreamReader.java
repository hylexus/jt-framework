package io.github.hylexus.jt.utils;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.netty.buffer.ByteBuf;

public class DefaultBitStreamReader implements BitStreamReader {

    private final ByteBuf buffer;

    // [0, byteBuf.readableBytes()]
    private int byteOffset;

    // [0-7]
    private int bitOffset;

    public DefaultBitStreamReader(ByteBuf buffer) {
        this.buffer = buffer;
        this.byteOffset = 0;
        this.bitOffset = 7;
    }

    public int readBit() {
        final int result = (buffer.getByte(byteOffset) >> (bitOffset--)) & 0b01;
        if (bitOffset == -1) {
            byteOffset++;
            bitOffset = 7;
        }
        return result;
    }

    @Override
    public int readBit(int len) {
        // Assertions.range(len, 1, Integer.SIZE, "len >= 1 && len < " + Integer.SIZE);
        int result = 0;
        for (int i = 0; i < len; i++) {
            result |= (readBit() << (len - i - 1));
        }
        return result;
    }

    @Override
    public void release() {
        if (this.buffer != null) {
            JtCommonUtils.release(this.buffer);
        }
    }
}
