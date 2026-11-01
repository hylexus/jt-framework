/*
 * Copyright 2019 matrixy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Adapted for jt-framework by hylexus; upstream source links appear below.
 */

package io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters;

import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormatConverter;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.BuiltinAudioFormatOptions;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.FlvJt1078AudioData;
import io.github.hylexus.jt.jt1078.support.extension.flv.tag.AudioFlvTag;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import javax.annotation.Nonnull;

import static io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters.g7xx.G711.alaw2linear;

/**
 * @see <a href="https://gitee.com/matrixy/jtt1078-video-server">https://gitee.com/matrixy/jtt1078-video-server</a>
 */
public class G711aToPcmJt1078AudioFormatConverter implements Jt1078AudioFormatConverter {

    private final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    @Nonnull
    @Override
    public Jt1078AudioData convert(ByteBuf stream, AudioFormatOptions sourceOptions) {

        final ByteBuf buffer = allocator.buffer();
        while (stream.isReadable()) {
            short linear = (short) alaw2linear(stream.readByte());
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
