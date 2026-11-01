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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @see <a href="https://www.bilibili.com/video/BV1V3411m7xK">https://www.bilibili.com/video/BV1V3411m7xK</a>
 * @see <a href="https://www.zzsin.com/catalog/write_avc_decoder.html">自己动手写 H.264 解码器</a>
 */
public interface BitStreamReader extends AutoCloseable {
    static BitStreamReader fromHexString(String hexString) {
        return fromByteBuf(ByteBufAllocator.DEFAULT.buffer().writeBytes(HexStringUtils.hexString2Bytes(hexString)));
    }

    static BitStreamReader fromByteBuf(ByteBuf buf) {
        return new DefaultBitStreamReader(buf);
    }

    /**
     * 读取 1 个 bit
     */
    int readBit();

    /**
     * 读取 {@code len} 个 bit
     *
     * @param len bit 数量
     */
    int readBit(int len);

    default int readU(int len) {
        return this.readBit(len);
    }

    default int readU1() {
        return this.readBit();
    }

    default int readU2() {
        return this.readBit(2);
    }

    default int readU3() {
        return this.readBit(3);
    }

    default int readU5() {
        return this.readBit(5);
    }

    default int readU6() {
        return this.readBit(6);
    }

    default int readU8() {
        return this.readBit(8);
    }

    default int readU16() {
        return this.readBit(16);
    }


    /**
     * 无符号指数哥伦布熵编码伪代码:
     * <pre>
     * {@code
     * let len = (i+1).toBinary().length();
     * let result = "0".repeat(len-1) concat i.toBinary();
     *
     * // 比如 i =4 时:
     * // (i+1).toBinary() == 5.toBinary() == 0b"101"
     * // 0b"101" --> 长度为 3 --> 前面补 2(3-1) 个 0
     * // 所以 ue(4) == 0b"00101"
     * }
     * </pre>
     *
     * @see <a href="https://www.bilibili.com/video/BV1V3411m7xK">https://www.bilibili.com/video/BV1V3411m7xK</a>
     * @see <a href="https://www.zzsin.com/catalog/write_avc_decoder.html">自己动手写 H.264 解码器</a>
     */
    default int readUnsignedExponentialGolombNumber() {
        int zeroCount = 0;
        while ((readBit() == 0) && zeroCount < Integer.SIZE) {
            zeroCount++;
        }

        // 上面循环跳出的时候实际上已经"多"往后移了一位(readBit() == 1 时跳出)
        // zeroCount+1 位数据
        int result = readBit(zeroCount);
        // 将多读取的一位加到最前面
        // (1 << zeroCount) -1 : zeroCount 个值为1的 bit
        result += (1 << zeroCount) - 1;
        return result;
    }

    default int readUe() {
        return this.readUnsignedExponentialGolombNumber();
    }

    /**
     * 有符号指数哥伦布熵编码伪代码:
     * <pre>
     * {@code
     * const sign = 0(正数) | 1(负数)
     * let suffix = abs(i).toBinary() concat sign
     * let result = "0".repeat(suffix.length() - 1) concat suffix
     *
     * // 比如 i = -5 时
     * // abc(-5) == 5 ---> 5.toBinary() == 0b"101"
     * // -5 是负数所以添加一个 1 作为后缀 --> suffix == 0b"1011"
     * // suffix(0b"1011") 长度为 4 --> 前面补 3(4-1) 个0
     * // 所以 se(-5) == 0b"0001011"
     *
     * // 比如 i = 7 时
     * // abc(-7) == 7 ---> 7.toBinary() == 0b"111"
     * // -7 是负数所以添加一个 1 作为后缀 --> suffix == 0b"1111"
     * // suffix(0b"1010") 长度为 4 --> 前面补 3(4-1) 个0
     * // 所以 se(-5) == 0b"0001111"
     *
     *
     * }
     * </pre>
     *
     * @see <a href="https://www.bilibili.com/video/BV1V3411m7xK">https://www.bilibili.com/video/BV1V3411m7xK</a>
     * @see <a href="https://www.zzsin.com/catalog/write_avc_decoder.html">自己动手写 H.264 解码器</a>
     */
    default int readSignedExponentialGolombNumber() {
        final int result = readUnsignedExponentialGolombNumber();
        if ((result & 0b01) == 1) {
            return (result + 1) >> 1;
        } else {
            return -(result >> 1);
        }
    }

    default int readSe() {
        return this.readSignedExponentialGolombNumber();
    }

    void release();

    @Override
    default void close() {
        this.release();
    }
}
