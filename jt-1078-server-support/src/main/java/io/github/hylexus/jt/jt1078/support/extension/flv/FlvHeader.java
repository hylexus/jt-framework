package io.github.hylexus.jt.jt1078.support.extension.flv;

import io.github.hylexus.jt.jt1078.support.extension.flv.impl.DefaultFlvHeader;
import io.github.hylexus.jt.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * 9 bytes. FLV file header.
 */
public interface FlvHeader {

    int FLV_HEADER_SIGNATURE = ('F' << 16) | ('L' << 8) | ('V');

    /**
     * signature: 3byte 固定为 "FLV" 即 0x46,0x4c,0x56
     */
    default int signature() {
        return FLV_HEADER_SIGNATURE;
    }

    /**
     * version: 1byte
     */
    default byte version() {
        return 0x01;
    }


    /**
     * total: 1byte (00000101)
     * <p>
     * bit[0]: 是否存在视频
     * <p>
     * bit[1]: 保留(必须为0)
     * <p>
     * bit[2]: 是否存在音频
     * <p>
     * bit[3,7]: 保留(必须为0)
     */
    byte flag();

    /**
     * version1 中始终为 9
     * <p>
     * 从起始位置到 body 部分的字节数(即 header 的大小)
     */
    default int headerSize() {
        return 9;
    }

    default byte[] toBytes(boolean withPreviousTagSize) {
        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        try {
            this.writeTo(buffer, withPreviousTagSize);
            return ByteBufUtils.getBytes(buffer);
        } finally {
            buffer.release();
        }
    }

    default void writeTo(ByteBuf byteBuf, boolean withPreviousTagSize) {
        // signature
        byteBuf.writeByte('F');
        byteBuf.writeByte('L');
        byteBuf.writeByte('V');
        // version
        byteBuf.writeByte(this.version());
        // flags
        byteBuf.writeByte(this.flag());
        // size
        byteBuf.writeInt(this.headerSize());
        //  第一个 previousTagSize === 0
        if (withPreviousTagSize) {
            byteBuf.writeInt(0);
        }
    }

    static FlvHeader of(boolean hasVideo, boolean hasAudio) {
        return new DefaultFlvHeader(hasVideo, hasAudio);
    }

}
