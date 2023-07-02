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
