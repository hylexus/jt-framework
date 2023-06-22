package io.github.hylexus.jt.jt1078.support.extension.flv.tag;

import io.github.hylexus.jt.jt1078.support.extension.flv.impl.DefaultVideoFlvFlvTagHeader;
import io.github.hylexus.oaks.utils.IntBitOps;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.util.Optional;

public interface VideoFlvTag {
    /**
     * 非 H.264 : 1 byte
     * <p>
     * H.264: 5 bytes({@link #avcPacketType()} + {@link #compositionTime()})
     */
    interface VideoFlvTagHeader {

        default int length() {
            if (codecId() == VideoCodecId.AVC) {
                return 5;
            }
            return 1;
        }

        /**
         * 4 bits
         *
         * @return 帧类型
         */
        VideoFrameType frameType();

        /**
         * 4 bits
         *
         * @return 编码 ID
         */
        VideoCodecId codecId();

        /**
         * 8 bits
         * <p>
         * 只有 H.264 才有该属性
         */
        Optional<VideoAvcPacketType> avcPacketType();

        /**
         * 24 bits
         * <p>
         * 只有 H.264 才有该属性
         *
         * @return {@link #avcPacketType()} == 1 ({@link VideoAvcPacketType#AVC_NALU}) 时为相对时间戳，否则为零
         */
        Optional<Integer> compositionTime();

        static VideoFlvTagHeader createAvcNaluHeader(VideoFrameType frameType, int compositionTime) {
            return newBuilder().frameType(frameType)
                    .codecId(VideoFlvTag.VideoCodecId.AVC)
                    .avcPacketType(VideoAvcPacketType.AVC_NALU)
                    .compositionTime(compositionTime)
                    .build();
        }

        static VideoFlvTagHeader createAvcEndSequenceHeader() {
            return newBuilder().frameType(VideoFlvTag.VideoFrameType.KEY_FRAME)
                    .codecId(VideoFlvTag.VideoCodecId.AVC)
                    .avcPacketType(VideoAvcPacketType.AVC_END_OF_SEQ)
                    .compositionTime(0)
                    .build();
        }

        static VideoFlvTagHeader createAvcSequenceHeader() {
            return newBuilder().frameType(VideoFlvTag.VideoFrameType.KEY_FRAME)
                    .codecId(VideoFlvTag.VideoCodecId.AVC)
                    .avcPacketType(VideoAvcPacketType.AVC_SEQ_HEADER)
                    .compositionTime(0)
                    .build();
        }

        static VideoFlvTagHeaderBuilder newBuilder() {
            return new DefaultVideoFlvFlvTagHeader();
        }

        default int writeTo(ByteBuf byteBuf) {
            // 1 byte
            byteBuf.writeByte(this.frameType().getValue() << 4 | (this.codecId().getValue() & 0xF));
            // 只有 H.264 才有该属性
            // 1 byte
            avcPacketType().ifPresent(avcPacketType -> byteBuf.writeByte(avcPacketType.getValue()));
            // 只有 H.264 才有该属性
            // 3 bytes
            compositionTime().ifPresent(time -> byteBuf.writeBytes(IntBitOps.intTo3Bytes(time)));
            return length();
        }
    }

    @Getter
    enum VideoCodecId {
        JPEG((byte) 1),
        SORENSON_H263((byte) 2),
        SCREEN_VIDE((byte) 3),
        VP6_WITH_ALPHA((byte) 5),
        SCREEN_VIDEO_V2((byte) 6),
        AVC((byte) 7),
        ;
        private final byte value;

        VideoCodecId(byte value) {
            this.value = value;
        }
    }

    @Getter
    enum VideoFrameType {
        KEY_FRAME((byte) 1),
        INTER_FRAME((byte) 2),
        DISPOSABLE_INTER_FRAME((byte) 3),
        GENERATED_KEY_FRAME((byte) 4),
        VIDEO_INFO_OR_COMMAND_FRAME((byte) 5),
        ;
        private final byte value;

        VideoFrameType(byte value) {
            this.value = value;
        }
    }

    @Getter
    enum VideoAvcPacketType {
        AVC_SEQ_HEADER((byte) 0),
        AVC_NALU((byte) 1),
        AVC_END_OF_SEQ((byte) 2),
        ;
        private final byte value;

        VideoAvcPacketType(byte value) {
            this.value = value;
        }
    }

    interface VideoFlvTagHeaderBuilder {
        VideoFlvTagHeaderBuilder frameType(VideoFrameType frameType);

        VideoFlvTagHeaderBuilder codecId(VideoCodecId codecId);

        VideoFlvTagHeaderBuilder avcPacketType(VideoAvcPacketType avcPacketType);

        VideoFlvTagHeaderBuilder compositionTime(int compositionTime);

        VideoFlvTagHeader build();
    }

}
