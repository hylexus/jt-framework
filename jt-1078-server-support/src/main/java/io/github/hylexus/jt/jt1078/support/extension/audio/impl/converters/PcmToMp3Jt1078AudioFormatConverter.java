package io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters;

import de.sciss.jump3r.lowlevel.LameEncoder;
import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormatConverter;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.FlvJt1078AudioData;
import io.github.hylexus.jt.jt1078.support.extension.flv.tag.AudioFlvTag;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.sound.sampled.AudioFormat;

/**
 * 这个实现类也是从 <a href="https://gitee.com/matrixy/jtt1078-video-server">https://gitee.com/matrixy/jtt1078-video-server</a> 复制过来之后修改的。
 *
 * @see <a href="https://gitee.com/matrixy/jtt1078-video-server/blob/flv/src/main/java/cn/org/hentai/jtt1078/codec/MP3Encoder.java#L12">https://gitee.com/matrixy/jtt1078-video-server/blob/flv/src/main/java/cn/org/hentai/jtt1078/codec/MP3Encoder.java#L12</a>
 * @see <a href="https://sourceforge.net/projects/jump3r/">https://sourceforge.net/projects/jump3r/</a>
 * @see <a href="https://pure-java-mp3-encoder.blogspot.com/">https://pure-java-mp3-encoder.blogspot.com/</a>
 */
@Slf4j
public class PcmToMp3Jt1078AudioFormatConverter implements Jt1078AudioFormatConverter {

    private volatile LameEncoder lameEncoder;
    private final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
    private byte[] buffer;

    public PcmToMp3Jt1078AudioFormatConverter() {
    }

    private void initLameEncoderIfNecessary(AudioFormatOptions sourceOptions) {
        if (this.lameEncoder == null) {
            synchronized (this) {
                if (this.lameEncoder == null) {
                    this.lameEncoder = this.createLameEncoder(sourceOptions);
                    this.buffer = new byte[this.lameEncoder.getPCMBufferSize()];
                }
            }
        }
    }

    private LameEncoder createLameEncoder(AudioFormatOptions sourceOptions) {
        final int sourceFrameSize = sourceOptions.bitDepth() / 8 * sourceOptions.channelCount();
        return new LameEncoder(
                new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceOptions.sampleRate(), sourceOptions.bitDepth(), sourceOptions.channelCount(), sourceFrameSize <= 0 ? -1 : sourceFrameSize, -1, false),
                sourceOptions.bitRate() / 1000,
                LameEncoder.CHANNEL_MODE_MONO,
                LameEncoder.QUALITY_MIDDLE,
                false);
    }

    @Nonnull
    @Override
    public Jt1078AudioData convert(ByteBuf stream, AudioFormatOptions sourceOptions) {
        this.initLameEncoderIfNecessary(sourceOptions);

        if (isEmptyStream(stream)) {
            return Jt1078AudioData.empty();
        }

        final byte[] pcm = JtCommonUtils.getBytes(stream);

        int offset = 0;
        int length = Math.min(this.lameEncoder.getPCMBufferSize(), pcm.length);
        int processed;
        final ByteBuf mp3Stream = this.allocator.buffer();
        try {
            while ((processed = this.lameEncoder.encodeBuffer(pcm, offset, length, buffer)) > 0) {
                mp3Stream.writeBytes(buffer, 0, processed);
                offset += length;
                length = Math.min(buffer.length, pcm.length - offset);
            }
            // processed = this.lameEncoder.encodeFinish(buffer);
            // if (processed > 0) {
            //     mp3Stream.writeBytes(buffer, 0, processed);
            // }
        } catch (Throwable e) {
            JtCommonUtils.release(mp3Stream);
            throw new RuntimeException(e);
        }

        final AudioFlvTag.AudioFlvTagHeader flvTagHeader = AudioFlvTag.newTagHeaderBuilder()
                .soundFormat(AudioFlvTag.AudioSoundFormat.MP3)
                .soundRate(AudioFlvTag.AudioSoundRate.RATE_5_5_KHZ)
                .soundSize(AudioFlvTag.AudioSoundSize.SND_BIT_16)
                .soundType(AudioFlvTag.AudioSoundType.MONO)
                .aacPacketType(null).build();

        return FlvJt1078AudioData.builder()
                .payload(mp3Stream)
                .payloadSize(mp3Stream.readableBytes())
                .flvTagHeader(flvTagHeader)
                .payloadOptions(sourceOptions)
                .build();
    }

    @Override
    public void close() {
        this.lameEncoder.close();
        log.info("{} closed", this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
}
