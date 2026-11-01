/*
 * MIT License
 *
 * Copyright (c) 2021 Redknot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/*
 * Adapted algorithm portions: EyerH264Decoder (MIT).
 * Comparison source: https://github.com/redknotmiaoyuqiao/EyerH264Decoder/blob/59e0a396f0b9c0a4a2c034ba6d44fae22297e979/EyerH264Decoder/BitStream.cpp
 * Changes by hylexus: Java, Netty buffers and JT/T 1078 integration.
 * Attribution restored 2026-10-02; exact historical import revision is unknown.
 */

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
