package io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters;

import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormatConverter;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnull;

public class SilenceJt1078AudioFormatConverter implements Jt1078AudioFormatConverter {
    @Nonnull
    @Override
    public Jt1078AudioData convert(ByteBuf stream, AudioFormatOptions sourceOptions) {
        return Jt1078AudioData.empty();
    }
}
