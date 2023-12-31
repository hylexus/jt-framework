package io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters;

import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormatConverter;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.BuiltinAudioFormatOptions;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.FlvJt1078AudioData;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters.g7xx.G711;
import io.github.hylexus.jt.jt1078.support.extension.flv.tag.AudioFlvTag;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import javax.annotation.Nonnull;

/**
 * @see <a href="https://gitee.com/matrixy/jtt1078-video-server">https://gitee.com/matrixy/jtt1078-video-server</a>
 */
public class G711uToPcmJt1078AudioFormatConverter implements Jt1078AudioFormatConverter {

    private final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    @Nonnull
    @Override
    public Jt1078AudioData convert(ByteBuf stream, AudioFormatOptions sourceOptions) {
        final ByteBuf buffer = allocator.buffer();
        while (stream.isReadable()) {
            final short value = stream.readUnsignedByte();
            int linear = G711.ulaw2linear(value & 0xff);
            buffer.writeByte((byte) (linear & 0xff));
            buffer.writeByte((byte) ((linear >> 8) & 0xff));
        }

        final AudioFlvTag.AudioFlvTagHeader flvTagHeader = AudioFlvTag.newTagHeaderBuilder()
                .soundFormat(AudioFlvTag.AudioSoundFormat.LINEAR_PCM_LITTLE_ENDIAN)
                .soundRate(AudioFlvTag.AudioSoundRate.RATE_5_5_KHZ)
                .soundSize(AudioFlvTag.AudioSoundSize.SND_BIT_16)
                .soundType(AudioFlvTag.AudioSoundType.MONO)
                .aacPacketType(null).build();

        return FlvJt1078AudioData.builder()
                .payload(buffer)
                .payloadSize(buffer.readableBytes())
                .flvTagHeader(flvTagHeader)
                .payloadOptions(BuiltinAudioFormatOptions.PCM_S32_LE_MONO)
                .build();
    }

}
