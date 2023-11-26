package io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormatConverter;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.FlvJt1078AudioData;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters.g7xx.*;
import io.github.hylexus.jt.jt1078.support.extension.flv.tag.AudioFlvTag;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

import static io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters.g7xx.G72X.AUDIO_ENCODING_LINEAR;

/**
 * @see <a href="https://gitee.com/matrixy/jtt1078-video-server/blob/flv/src/main/java/cn/org/hentai/jtt1078/codec/G726Codec.java#L28">https://gitee.com/matrixy/jtt1078-video-server/blob/flv/src/main/java/cn/org/hentai/jtt1078/codec/G726Codec.java#L28</a>
 */
@Slf4j
public class G726ToPcmJt1078AudioFormatConverter implements Jt1078AudioFormatConverter {

    // pcm采样率
    private static final int PCM_SAMPLE = 8000;

    // pcm采样点
    private static final int PCM_POINT = 320;
    // 音频通道数
    private static final int CHANNEL = 1;

    private final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    @Nonnull
    @Override
    public Jt1078AudioData convert(ByteBuf stream) {

        final int readableBytes = stream.readableBytes();
        if (readableBytes <= 0) {
            log.warn("Jt1078AudioFormatConverter receive empty stream !!!");
            return Jt1078AudioData.empty();
        }

        int point = PCM_POINT;

        // 计算G726的码率
        int rateBit = readableBytes * 8 * PCM_SAMPLE / point;

        int pcmSize = point * CHANNEL * 2;
        byte[] pcm = new byte[pcmSize];

        final byte[] data = JtCommonUtils.getBytes(stream);
        final int length = data.length;
        final G72X g72X = this.getDecoder(rateBit);
        if (g72X == null) {
            return Jt1078AudioData.empty();
        }

        final int ret = g72X.decode(data, 0, length, AUDIO_ENCODING_LINEAR, pcm, 0);
        if (ret < 0) {
            log.warn("{} return empty data !!!", g72X.getClass().getSimpleName());
            return Jt1078AudioData.empty();
        }

        final ByteBuf payload = allocator.buffer().writeBytes(pcm);

        final AudioFlvTag.AudioFlvTagHeader flvTagHeader = AudioFlvTag.newTagHeaderBuilder()
                .soundFormat(AudioFlvTag.AudioSoundFormat.LINEAR_PCM_LITTLE_ENDIAN)
                .soundRate(AudioFlvTag.AudioSoundRate.RATE_5_5_KHZ)
                .soundSize(AudioFlvTag.AudioSoundSize.SND_BIT_16)
                .soundType(AudioFlvTag.AudioSoundType.MONO)
                .aacPacketType(null).build();

        return FlvJt1078AudioData.builder()
                .payload(payload)
                .payloadSize(payload.readableBytes())
                .flvTagHeader(flvTagHeader)
                .build();
    }

    private G72X getDecoder(int rateBit) {
        switch (rateBit) {
            case 40000: {
                return G726_40.getInstance();
            }
            case 32000: {
                return G726_32.getInstance();
            }
            case 16000: {
                return G726_16.getInstance();
            }
            case 24000: {
                return G726_24.getInstance();
            }
            default: {
                return null;
            }
        }
    }

}
