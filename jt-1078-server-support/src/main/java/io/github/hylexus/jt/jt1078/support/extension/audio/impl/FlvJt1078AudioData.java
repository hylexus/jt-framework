package io.github.hylexus.jt.jt1078.support.extension.audio.impl;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormatConverter;
import io.github.hylexus.jt.jt1078.support.extension.flv.tag.AudioFlvTag;
import io.netty.buffer.ByteBuf;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 */
@Getter
@Accessors(fluent = true)
@Builder
public class FlvJt1078AudioData implements Jt1078AudioFormatConverter.Jt1078AudioData {

    private final ByteBuf payload;
    private final int payloadSize;
    private final AudioFlvTag.AudioFlvTagHeader flvTagHeader;

    public FlvJt1078AudioData(ByteBuf payload, int payloadSize, AudioFlvTag.AudioFlvTagHeader flvTagHeader) {
        this.payload = payload;
        this.payloadSize = payloadSize;
        this.flvTagHeader = flvTagHeader;
    }

    @Override
    public void close() {
        if (this.payload != null) {
            JtCommonUtils.release(this.payload);
        }
    }

}
