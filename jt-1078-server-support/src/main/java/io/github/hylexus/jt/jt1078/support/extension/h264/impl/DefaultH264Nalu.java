package io.github.hylexus.jt.jt1078.support.extension.h264.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078DataType;
import io.github.hylexus.jt.jt1078.support.extension.h264.H264Nalu;
import io.github.hylexus.jt.jt1078.support.extension.h264.H264NaluHeader;
import io.netty.buffer.ByteBuf;

import java.util.Optional;

public class DefaultH264Nalu implements H264Nalu {

    private final H264NaluHeader header;
    private final ByteBuf data;
    private final Jt1078DataType dataType;
    private final Integer lastIFrameInterval;
    private final Integer lastFrameInterval;
    private final Long timestamp;

    public DefaultH264Nalu(H264NaluHeader header, ByteBuf data, Jt1078DataType dataType, Integer lastIFrameInterval, Integer lastFrameInterval, Long timestamp) {
        this.header = header;
        this.data = data;
        this.dataType = dataType;
        this.lastIFrameInterval = lastIFrameInterval;
        this.lastFrameInterval = lastFrameInterval;
        this.timestamp = timestamp;
    }

    @Override
    public H264NaluHeader header() {
        return this.header;
    }

    @Override
    public ByteBuf data() {
        return this.data;
    }

    @Override
    public Jt1078DataType dataType() {
        return this.dataType;
    }

    @Override
    public Optional<Integer> lastIFrameInterval() {
        return Optional.ofNullable(this.lastIFrameInterval);
    }

    @Override
    public Optional<Integer> lastFrameInterval() {
        return Optional.ofNullable(this.lastFrameInterval);
    }

    @Override
    public Optional<Long> timestamp() {
        return Optional.ofNullable(this.timestamp);
    }

    @Override
    public String toString() {
        return "DefaultH264Nalu{"
                + "header=" + header
                + ", rbsp=" + data
                + ", dataType=" + dataType
                + ", lastIFrameInterval=" + lastIFrameInterval
                + ", lastFrameInterval=" + lastFrameInterval
                + ", timestamp=" + timestamp
                + '}';
    }
}
