package io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormatConverter;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.BuiltinAudioFormatOptions;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.FlvJt1078AudioData;
import io.github.hylexus.jt.jt1078.support.extension.flv.tag.AudioFlvTag;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

/**
 * IMA(Interactive Multimedia Association) ADPCM 解码算法实现。
 * <p>
 * <h3>NOTES!!!</h3>
 * 该算法是从下面几个 代码库/资料 中改造而来：
 * <p>
 * 该算法是从下面几个 代码库/资料 中改造而来：
 * <p>
 * 该算法是从下面几个 代码库/资料 中改造而来：
 * <ol>
 *     <li><a href="https://github.com/pdeljanov/Symphonia/blob/b157bb58826a2035405702f481b6fdcbd922cbf3/symphonia-codec-adpcm/src/codec_ima.rs#L65">symphonia-codec-adpcm/src/codec_ima.rs#L65(Rust 版本的音频库)</a></li>
 *     <li><a href="https://gitee.com/matrixy/jtt1078-video-server/blob/flv/src/main/java/cn/org/hentai/jtt1078/codec/ADPCMCodec.java#L181">cn.org.hentai.jtt1078.codec.ADPCMCodec</a></li>
 *     <li><a href="https://ww1.microchip.com/downloads/en/AppNotes/00643b.pdf">https://ww1.microchip.com/downloads/en/AppNotes/00643b.pdf</a></li>
 * </ol>
 *
 * @see <a href="https://wiki.multimedia.cx/index.php/IMA_ADPCM">https://wiki.multimedia.cx/index.php/IMA_ADPCM</a>
 * @see <a href="https://github.com/pdeljanov/Symphonia.git">https://github.com/pdeljanov/Symphonia.git(Rust 版本的音频库)</a>
 * @see <a href="https://www.cs.columbia.edu/~hgs/audio/dvi/IMA_ADPCM.pdf">https://www.cs.columbia.edu/~hgs/audio/dvi/IMA_ADPCM.pdf</a>
 * @see <a href="https://ww1.microchip.com/downloads/en/AppNotes/00643b.pdf">https://ww1.microchip.com/downloads/en/AppNotes/00643b.pdf</a>
 * @see <a href="https://www.hentai.org.cn/article?id=8">https://www.hentai.org.cn/article?id=8</a>
 */
@Slf4j
public class AdpcmImaToPcmJt1078AudioFormatConverter implements Jt1078AudioFormatConverter {
    private static final int[] IMA_INDEX_TABLE = {
            -1, -1, -1, -1, 2, 4, 6, 8,
            -1, -1, -1, -1, 2, 4, 6, 8
            // 0xff, 0xff, 0xff, 0xff, 2, 4, 6, 8,
            // 0xff, 0xff, 0xff, 0xff, 2, 4, 6, 8
    };

    private static final int[] IMA_STEP_TABLE = {
            7, 8, 9, 10, 11, 12, 13, 14, 16, 17,
            19, 21, 23, 25, 28, 31, 34, 37, 41, 45,
            50, 55, 60, 66, 73, 80, 88, 97, 107, 118,
            130, 143, 157, 173, 190, 209, 230, 253, 279, 307,
            337, 371, 408, 449, 494, 544, 598, 658, 724, 796,
            876, 963, 1060, 1166, 1282, 1411, 1552, 1707, 1878, 2066,
            2272, 2499, 2749, 3024, 3327, 3660, 4026, 4428, 4871, 5358,
            5894, 6484, 7132, 7845, 8630, 9493, 10442, 11487, 12635, 13899,
            15289, 16818, 18500, 20350, 22385, 24623, 27086, 29794, 32767
    };
    private final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    /**
     * @param stream ADPCM(IMA)
     * @return PCM
     */
    @Nonnull
    @Override
    public Jt1078AudioData convert(ByteBuf stream, AudioFormatOptions sourceOptions) {
        if (isEmptyStream(stream)) {
            log.warn("Jt1078AudioFormatConverter receive empty stream !!!");
            return Jt1078AudioData.empty();
        }

        final AdpcmImaBlockStatus status = this.readPreamble(stream);

        final ByteBuf buffer = this.allocator.buffer();
        try {
            // buffer.writeShortLE(status.predictor);

            while (stream.isReadable()) {
                final short nibbles = stream.readUnsignedByte();
                buffer.writeShortLE(this.expandNibble(status, (byte) ((nibbles >> 4) & 0x0F)));
                buffer.writeShortLE(this.expandNibble(status, (byte) (nibbles & 0x0F)));
            }
        } catch (Throwable e) {
            JtCommonUtils.release(buffer);
            throw e;
        }

        final AudioFlvTag.AudioFlvTagHeader flvTagHeader = AudioFlvTag.newTagHeaderBuilder()
                .soundFormat(AudioFlvTag.AudioSoundFormat.LINEAR_PCM_LITTLE_ENDIAN)
                .soundRate(AudioFlvTag.AudioSoundRate.RATE_11_KHZ)
                .soundSize(AudioFlvTag.AudioSoundSize.SND_BIT_16)
                .soundType(AudioFlvTag.AudioSoundType.MONO)
                .aacPacketType(null).build();

        return FlvJt1078AudioData.builder()
                .payload(buffer)
                .payloadSize(buffer.readableBytes())
                .flvTagHeader(flvTagHeader)
                .payloadOptions(BuiltinAudioFormatOptions.PCM_S16_LE_MONO)
                .build();
    }

    // 前 4 个字节
    private AdpcmImaBlockStatus readPreamble(ByteBuf stream) {
        // 2 bytes
        final short predictor = stream.readShortLE();
        // 1 byte
        final byte stepIndex = stream.readByte();
        if (stepIndex > 88) {
            throw new IllegalStateException("adpcm (ima): invalid step index : " + stepIndex);
        }
        // reserved 1 byte
        stream.readByte();
        return new AdpcmImaBlockStatus(predictor, stepIndex);
    }

    /**
     * 参考 Rust 音频库的算法：<a href="https://github.com/pdeljanov/Symphonia/blob/b157bb58826a2035405702f481b6fdcbd922cbf3/symphonia-codec-adpcm/src/codec_ima.rs#L65">symphonia-codec-adpcm/src/codec_ima.rs#L65</a>
     *
     * @see <a href="https://github.com/pdeljanov/Symphonia/blob/b157bb58826a2035405702f481b6fdcbd922cbf3/symphonia-codec-adpcm/src/codec_ima.rs#L65">https://github.com/pdeljanov/Symphonia/blob/b157bb58826a2035405702f481b6fdcbd922cbf3/symphonia-codec-adpcm/src/codec_ima.rs#L65</a>
     */
    private short expandNibble(AdpcmImaBlockStatus status, short nibble) {
        final int step = IMA_STEP_TABLE[status.stepIndex];

        final boolean sign = (nibble & 0x08) > 0;
        final int delta = (nibble & 0x07);
        final int diff = ((2 * delta + 1) * step) >> 3;
        final int predictor = sign ? status.predictor - diff : status.predictor + diff;

        status.predictor = this.ensurePredictorRange(predictor);
        status.stepIndex = this.ensureIndexCapacity(status.stepIndex + IMA_INDEX_TABLE[nibble]);

        return status.predictor;
    }

    /**
     * 参考这里的算法: <a href="https://ww1.microchip.com/downloads/en/AppNotes/00643b.pdf">https://ww1.microchip.com/downloads/en/AppNotes/00643b.pdf</a>
     *
     * <pre>{@code
     * long step; // Quantizer step size
     * signed long predsample; // Output of ADPCM predictor
     * signed long diffq; // Dequantized predicted difference
     * int index; // Index into step size table
     *
     * signed long ADPCMDecoder(char code )
     * {
     * // Restore previous values of predicted sample and quantizer step size index
     * predsample =state.prevsample;
     * index =state.previndex;
     *
     * // Find quantizer step size from lookup table using index
     * step = StepSizeTable[index];
     *
     * // Inverse quantize the ADPCM code into a difference using the quantizer step size
     * diffq = step >> 3;
     * if( code & 4 )
     *   diffq += step;
     * if( code & 2 )
     *   diffq += step >> 1;
     * if( code & 1 )
     *   diffq += step >> 2;
     *
     * // Add the difference to the predicted sample
     * if( code & 8 )
     *   predsample -= diffq;
     * else
     *   predsample += diffq;
     *
     * // Check for overflow of the new predicted sample
     * if( predsample > 32767 )
     *   predsample = 32767;
     * else if( predsample < -32768 )
     *   predsample = -32768;
     *
     * // Find new quantizer step size by adding the old index and a table lookup using the ADPCM code
     * index += IndexTable[code];
     *
     * // Check for overflow of the new quantizer step size index
     * if( index < 0 )
     *   index = 0;
     * if( index > 88 )
     *   index = 88;
     * // Save predicted sample and quantizer step size index for next iteration
     * state.prevsample = predsample;
     * state.previndex = index;
     *
     * // Return the new speech sample
     * return( predsample );
     *
     * }
     * }</pre>
     *
     * @see <a href="https://ww1.microchip.com/downloads/en/AppNotes/00643b.pdf">https://ww1.microchip.com/downloads/en/AppNotes/00643b.pdf</a>
     */
    private short expandNibbleV2(AdpcmImaBlockStatus status, short code) {
        int predSample = status.predictor;
        byte index = status.stepIndex;
        final int step = IMA_STEP_TABLE[index];
        int diffq = step >> 3;
        if ((code & 4) > 0) {
            diffq += step;
        }
        if ((code & 2) > 0) {
            diffq += step >> 1;
        }
        if ((code & 1) > 0) {
            diffq += step >> 2;
        }

        if ((code & 8) > 0) {
            predSample -= diffq;
        } else {
            predSample += diffq;
        }

        if (predSample > 32767) {
            predSample = 32767;
        }
        if (predSample < -32768) {
            predSample = -32768;
        }

        index += (byte) IMA_INDEX_TABLE[code];
        if (index < 0) {
            index = 0;
        }
        if (index > 88) {
            index = 88;
        }
        status.predictor = (short) predSample;
        status.stepIndex = index;
        return status.predictor;
    }

    /**
     * 确保索引在 {@link #IMA_STEP_TABLE} 的索引范围内。
     *
     * @return index ∈ [0, {@link #IMA_STEP_TABLE IMA_STEP_TABLE.length - 1}] 即 [0, 88]
     */
    byte ensureIndexCapacity(int index) {
        if (index < 0) {
            return 0;
        } else {
            return (byte) Math.min(index, 88);
        }
    }

    short ensurePredictorRange(int predictor) {
        return (short) predictor;
    }

    @ToString
    static class AdpcmImaBlockStatus {
        short predictor;
        byte stepIndex;

        public AdpcmImaBlockStatus(short predictor, byte stepIndex) {
            this.predictor = predictor;
            this.stepIndex = stepIndex;
        }
    }

}

