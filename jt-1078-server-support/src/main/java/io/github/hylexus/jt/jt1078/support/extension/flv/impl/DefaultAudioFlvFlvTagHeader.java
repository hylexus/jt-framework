package io.github.hylexus.jt.jt1078.support.extension.flv.impl;

import io.github.hylexus.jt.jt1078.support.extension.flv.tag.AudioFlvTag;
import io.github.hylexus.jt.utils.Assertions;

import java.util.Optional;

public class DefaultAudioFlvFlvTagHeader implements AudioFlvTag.AudioFlvTagHeader, AudioFlvTag.AudioFlvTagHeaderBuilder {
    private AudioFlvTag.AudioSoundFormat soundFormat;
    private AudioFlvTag.AudioSoundRate soundRate;
    private AudioFlvTag.AudioSoundSize soundSize;
    private AudioFlvTag.AudioSoundType soundType;
    private AudioFlvTag.AudioAacPacketType aacPacketType;

    public DefaultAudioFlvFlvTagHeader() {
    }

    public DefaultAudioFlvFlvTagHeader(
            AudioFlvTag.AudioSoundFormat soundFormat, AudioFlvTag.AudioSoundRate soundRate,
            AudioFlvTag.AudioSoundSize soundSize, AudioFlvTag.AudioSoundType soundType,
            AudioFlvTag.AudioAacPacketType aacPacketType) {
        this.soundFormat = soundFormat;
        this.soundRate = soundRate;
        this.soundSize = soundSize;
        this.soundType = soundType;
        this.aacPacketType = aacPacketType;
    }

    @Override
    public AudioFlvTag.AudioSoundFormat soundFormat() {
        return this.soundFormat;
    }

    @Override
    public AudioFlvTag.AudioFlvTagHeaderBuilder soundFormat(AudioFlvTag.AudioSoundFormat soundFormat) {
        this.soundFormat = soundFormat;
        return this;
    }

    @Override
    public AudioFlvTag.AudioSoundRate soundRate() {
        return this.soundRate;
    }

    @Override
    public AudioFlvTag.AudioFlvTagHeaderBuilder soundRate(AudioFlvTag.AudioSoundRate soundRate) {
        this.soundRate = soundRate;
        return this;
    }

    @Override
    public AudioFlvTag.AudioSoundSize soundSize() {
        return this.soundSize;
    }

    @Override
    public AudioFlvTag.AudioFlvTagHeaderBuilder soundSize(AudioFlvTag.AudioSoundSize soundSize) {
        this.soundSize = soundSize;
        return this;
    }

    @Override
    public AudioFlvTag.AudioSoundType soundType() {
        return this.soundType;
    }

    @Override
    public AudioFlvTag.AudioFlvTagHeaderBuilder soundType(AudioFlvTag.AudioSoundType soundType) {
        this.soundType = soundType;
        return this;
    }

    @Override
    public Optional<AudioFlvTag.AudioAacPacketType> aacPacketType() {
        return Optional.ofNullable(this.aacPacketType);
    }

    @Override
    public AudioFlvTag.AudioFlvTagHeaderBuilder aacPacketType(AudioFlvTag.AudioAacPacketType aacPacketType) {
        this.aacPacketType = aacPacketType;
        return this;
    }

    @Override
    public AudioFlvTag.AudioFlvTagHeader build() {
        return new DefaultAudioFlvFlvTagHeader(
                Assertions.requireNonNull(this.soundFormat, "soundFormat is null"),
                Assertions.requireNonNull(this.soundRate, "soundFormat is null"),
                Assertions.requireNonNull(this.soundSize, "soundSize is null"),
                Assertions.requireNonNull(this.soundType, "soundType is null"),
                this.aacPacketType
        );
    }
}
