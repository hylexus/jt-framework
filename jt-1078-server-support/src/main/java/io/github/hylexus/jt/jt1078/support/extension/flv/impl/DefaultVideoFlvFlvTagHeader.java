package io.github.hylexus.jt.jt1078.support.extension.flv.impl;

import io.github.hylexus.jt.jt1078.support.extension.flv.tag.VideoFlvTag;
import io.github.hylexus.jt.utils.Assertions;

import java.util.Optional;

public class DefaultVideoFlvFlvTagHeader implements VideoFlvTag.VideoFlvTagHeader, VideoFlvTag.VideoFlvTagHeaderBuilder {
    private VideoFlvTag.VideoFrameType frameType;
    private VideoFlvTag.VideoCodecId codecId;
    private VideoFlvTag.VideoAvcPacketType avcPacketType;
    private Integer compositionTime;

    public DefaultVideoFlvFlvTagHeader() {
    }

    public DefaultVideoFlvFlvTagHeader(VideoFlvTag.VideoFrameType frameType, VideoFlvTag.VideoCodecId codecId, VideoFlvTag.VideoAvcPacketType avcPacketType, Integer compositionTime) {
        this.frameType = frameType;
        this.codecId = codecId;
        this.avcPacketType = avcPacketType;
        this.compositionTime = compositionTime;
    }

    @Override
    public VideoFlvTag.VideoFrameType frameType() {
        return this.frameType;
    }

    @Override
    public VideoFlvTag.VideoFlvTagHeaderBuilder frameType(VideoFlvTag.VideoFrameType frameType) {
        this.frameType = frameType;
        return this;
    }

    @Override
    public VideoFlvTag.VideoCodecId codecId() {
        return this.codecId;
    }

    @Override
    public VideoFlvTag.VideoFlvTagHeaderBuilder codecId(VideoFlvTag.VideoCodecId codecId) {
        this.codecId = codecId;
        return this;
    }

    @Override
    public Optional<VideoFlvTag.VideoAvcPacketType> avcPacketType() {
        return Optional.ofNullable(this.avcPacketType);
    }

    @Override
    public VideoFlvTag.VideoFlvTagHeaderBuilder avcPacketType(VideoFlvTag.VideoAvcPacketType avcPacketType) {
        this.avcPacketType = avcPacketType;
        return this;
    }

    @Override
    public Optional<Integer> compositionTime() {
        return Optional.ofNullable(this.compositionTime);
    }

    @Override
    public VideoFlvTag.VideoFlvTagHeaderBuilder compositionTime(int compositionTime) {
        this.compositionTime = compositionTime;
        return this;
    }

    @Override
    public VideoFlvTag.VideoFlvTagHeader build() {
        return new DefaultVideoFlvFlvTagHeader(
                Assertions.requireNonNull(this.frameType, "frameType is null"),
                Assertions.requireNonNull(this.codecId, "codecId is null"),
                this.avcPacketType,
                this.compositionTime
        );
    }
}
