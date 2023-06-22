package io.github.hylexus.jt.jt1078.support.extension.flv;

import io.github.hylexus.jt.jt1078.support.extension.flv.impl.DefaultFlvTagHeader;
import io.github.hylexus.jt.jt1078.support.extension.flv.tag.FlvTagType;
import io.github.hylexus.oaks.utils.IntBitOps;
import io.netty.buffer.ByteBuf;

/**
 * 11 bytes
 */
public interface FlvTagHeader {
    /**
     * 1 byte
     *
     * @return Tag类型
     */
    FlvTagType tagTye();

    /**
     * 3 bytes
     * <p>
     * 从 {@link #streamId()} 之后开始算起
     *
     * @return data部分大小
     */
    int dataSize();

    /**
     * 3 bytes
     *
     * @return Tag时间戳
     */
    int timestamp();

    /**
     * 1 byte
     *
     * @return Tag时间戳扩展
     */
    byte timestampExtended();


    /**
     * 3 bytes
     * <p>
     * 总是为零
     *
     * @return streamId
     */
    default int streamId() {
        return 0;
    }

    default int writeTo(ByteBuf byteBuf) {
        // 1 byte
        byteBuf.writeByte(tagTye().getValue());
        // 3 bytes dataSize
        byteBuf.writeBytes(IntBitOps.intTo3Bytes(dataSize()));
        // byteBuf.writeInt(timestampExtended() << 24 | timestamp());
        // 3 bytes timestamp
        byteBuf.writeBytes(IntBitOps.intTo3Bytes(timestamp()));
        // 1 byte timestampExtended
        byteBuf.writeByte(timestampExtended() >>> 24);
        // 3 bytes streamId
        byteBuf.writeBytes(IntBitOps.intTo3Bytes(streamId()));
        return 11;
    }

    static FlvTagHeader newVideoTagHeader(int dataSize, int timestamp) {
        return new DefaultFlvTagHeader(FlvTagType.VIDEO, dataSize, timestamp);
    }

    static FlvTagHeader newAudioTagHeader(int dataSize, int timestamp) {
        return new DefaultFlvTagHeader(FlvTagType.AUDIO, dataSize, timestamp);
    }

    static FlvTagHeader newScriptTagHeader(int dataSize, int timestamp) {
        return new DefaultFlvTagHeader(FlvTagType.SCRIPT_DATA, dataSize, timestamp);
    }

}
