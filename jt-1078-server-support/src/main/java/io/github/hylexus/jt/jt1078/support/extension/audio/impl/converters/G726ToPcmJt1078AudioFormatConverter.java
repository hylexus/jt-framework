package io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormatConverter;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.BuiltinAudioFormatOptions;
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
 * @see <a href="https://blog.csdn.net/li_wen01/article/details/81141085">https://blog.csdn.net/li_wen01/article/details/81141085</a>
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3551#autoid-14">https://datatracker.ietf.org/doc/html/rfc3551#autoid-14</a>
 */
@Slf4j
public class G726ToPcmJt1078AudioFormatConverter implements Jt1078AudioFormatConverter {

    private final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    @Nonnull
    @Override
    public Jt1078AudioData convert(ByteBuf stream, AudioFormatOptions sourceOptions) {

        // 80/160/240/320/480
        final int readableBytes = stream.readableBytes();
        if (readableBytes <= 0) {
            log.warn("Jt1078AudioFormatConverter receive empty stream !!!");
            return Jt1078AudioData.empty();
        }

        final BuiltinAudioFormatOptions builtinAudioFormatOptions = (BuiltinAudioFormatOptions) sourceOptions;

        final G72X decoder;
        final AudioFormatOptions resultOptions;
        switch (builtinAudioFormatOptions) {
            case G726_S32_LE_MONO: {
                decoder = G726_32.getInstance();
                resultOptions = BuiltinAudioFormatOptions.PCM_S32_LE_MONO;
                break;
            }
            case G726_S16_LE_MONO: {
                decoder = G726_16.getInstance();
                resultOptions = BuiltinAudioFormatOptions.PCM_S16_LE_MONO;
                break;
            }
            case G726_S24_LE_MONO: {
                decoder = G726_24.getInstance();
                resultOptions = BuiltinAudioFormatOptions.PCM_S24_LE_MONO;
                break;
            }
            case G726_S40_LE_MONO: {
                decoder = G726_40.getInstance();
                resultOptions = BuiltinAudioFormatOptions.PCM_S40_LE_MONO;
                break;
            }
            default: {
                return Jt1078AudioData.empty();
            }
        }

        final byte[] pcm = new byte[builtinAudioFormatOptions.estimateDecodedPcmSize(readableBytes)];
        final byte[] data = JtCommonUtils.getBytes(stream);
        final int ret = decoder.decode(data, 0, data.length, AUDIO_ENCODING_LINEAR, pcm, 0);
        if (ret < 0) {
            log.warn("{} return empty data !!!", decoder.getClass().getSimpleName());
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
                .payloadOptions(resultOptions)
                .build();
    }

}
