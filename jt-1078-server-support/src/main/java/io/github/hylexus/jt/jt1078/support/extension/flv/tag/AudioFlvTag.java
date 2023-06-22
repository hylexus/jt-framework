package io.github.hylexus.jt.jt1078.support.extension.flv.tag;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.util.Optional;

public interface AudioFlvTag {


    /**
     * 非 {@link AudioSoundFormat#AAC} 类型: 1 byte
     * <p>
     * {@link AudioSoundFormat#AAC} 类型: 2 byte
     */
    interface AudioFlvTagHeader {

        /**
         * bit[4,7]
         *
         * @return 编码类型
         */
        AudioSoundFormat soundFormat();


        /**
         * bit[2,3]
         * <p>
         * AAC始终为 3
         * <p>
         * 0 = 5.5-kHz
         * 1 = 11-kHz
         * 2 = 22-kHz
         * 3 = 44-kHz
         *
         * @return 采样率
         */
        byte soundRate();

        /**
         * bit[1]
         * <p>
         * 0 = snd8Bit
         * <p>
         * 1 = snd16Bit
         *
         * @return 采样精度
         */
        AudioSoundSize soundSize();

        /**
         * bit[0]
         * <p>
         * 0 = sndMono 单声道
         * <p>
         * 1 = sndStereo 立体声，双声道
         * <p>
         * 对于AAC总是1
         *
         * @return 声道数
         */
        AudioSoundType soundType();

        Optional<AudioAacPacketType> aacPacketType();

        default void writeTo(ByteBuf byteBuf) {
            final int v = (this.soundType().getValue() & 0b0001)
                    | (this.soundSize().getValue() & 0b0010)
                    | (this.soundRate() & 0b1100)
                    | (this.soundFormat().getValue() & 0b11110000);

            byteBuf.writeByte(v);

            // 只有 H.264 才有该属性
            this.aacPacketType().ifPresent(t -> byteBuf.writeByte(t.getValue()));
        }
    }

    @Getter
    enum AudioSoundType {
        MONO((byte) 0),
        STEREO((byte) 1);
        private final byte value;

        AudioSoundType(byte value) {
            this.value = value;
        }
    }

    @Getter
    enum AudioSoundSize {
        BIT_8((byte) 0),
        BIT_16((byte) 1);
        private final byte value;

        AudioSoundSize(byte value) {
            this.value = value;
        }
    }

    @Getter
    enum AudioSoundFormat {
        MP3((byte) 2),
        AAC((byte) 10),
        // TODO 补充其他格式
        ;
        private final byte value;

        AudioSoundFormat(byte value) {
            this.value = value;
        }
    }

    @Getter
    enum AudioAacPacketType {
        // AAC sequence header
        AAC_SEQ_HEADER((byte) 0),
        // AAC raw
        AAC_RAW((byte) 1),
        ;
        private final byte value;

        AudioAacPacketType(byte value) {
            this.value = value;
        }
    }

    interface AudioFlvTagHeaderBuilder {
        AudioFlvTagHeaderBuilder soundFormat(AudioSoundFormat soundFormat);

        AudioFlvTagHeaderBuilder soundRate(byte soundRate);

        AudioFlvTagHeaderBuilder soundSize(AudioSoundSize soundSize);

        AudioFlvTagHeaderBuilder soundType(AudioSoundType soundType);

        AudioFlvTagHeaderBuilder aacPacketType(AudioAacPacketType aacPacketType);

        AudioFlvTagHeader build();
    }
}
