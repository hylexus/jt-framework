package io.github.hylexus.jt.jt1078.support.extension.flv.tag;

import io.github.hylexus.jt.jt1078.support.extension.flv.impl.DefaultAudioFlvFlvTagHeader;
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
        AudioSoundRate soundRate();

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

        /**
         * 只有 AAC 格式才有该字段
         */
        Optional<AudioAacPacketType> aacPacketType();

        default int writeTo(ByteBuf byteBuf) {
            final int writerIndex = byteBuf.writerIndex();
            final int v = (this.soundType().getValue() & 0b1)
                    | ((this.soundSize().getValue() & 0b1) << 1)
                    | ((this.soundRate().getValue() & 0b11) << 2)
                    | (this.soundFormat().getValue() << 4);

            byteBuf.writeByte(v);

            // 只有 H.264 才有该属性
            this.aacPacketType().ifPresent(t -> byteBuf.writeByte(t.getValue()));
            return byteBuf.writerIndex() - writerIndex;
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
        SND_BIT_8((byte) 0),
        SND_BIT_16((byte) 1);
        private final byte value;

        AudioSoundSize(byte value) {
            this.value = value;
        }
    }

    @Getter
    enum AudioSoundRate {
        // 5.5-kHz
        RATE_5_5_KHZ((byte) 0),
        // 11-kHz
        RATE_11_KHZ((byte) 1),
        // 22-kHz
        RATE_22_KHZ((byte) 2),
        // 44-kHz
        RATE_44_KHZ((byte) 3),
        ;
        private final byte value;

        AudioSoundRate(byte value) {
            this.value = value;
        }
    }

    @Getter
    enum AudioSoundFormat {
        LINEAR_PCM_PLATFORM_ENDIAN((byte) 0),
        ADPCM((byte) 1),
        MP3((byte) 2),
        LINEAR_PCM_LITTLE_ENDIAN((byte) 3),
        NELLYMOSER_16_KHZ_MONO((byte) 4),
        NELLYMOSER_8_KHZ_MONO((byte) 5),
        NELLYMOSER_6((byte) 6),
        G_711_A_LAW_LOGARITHMIC_PCM((byte) 7),
        G_711_MU_LAW_LOGARITHMIC_PCM((byte) 8),
        AAC((byte) 10),
        SPEEX((byte) 11),
        MP3_8_KHZ((byte) 14),
        DEVICE_SPECIFIC_SOUND((byte) 15),
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

        /**
         * @param soundRate 采样率
         */
        AudioFlvTagHeaderBuilder soundRate(AudioSoundRate soundRate);

        /**
         * @param soundSize 采样精度(位深)
         */
        AudioFlvTagHeaderBuilder soundSize(AudioSoundSize soundSize);

        /**
         * @param soundType 声道类型
         */
        AudioFlvTagHeaderBuilder soundType(AudioSoundType soundType);

        AudioFlvTagHeaderBuilder aacPacketType(AudioAacPacketType aacPacketType);

        AudioFlvTagHeader build();
    }

    static AudioFlvTagHeaderBuilder newTagHeaderBuilder() {
        return new DefaultAudioFlvFlvTagHeader();
    }
}
