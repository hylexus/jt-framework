package io.github.hylexus.jt.jt1078.support.extension.audio.impl;

import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormatConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum BuiltinAudioFormatOptions implements Jt1078AudioFormatConverter.AudioFormatOptions {
    /**
     * 静音--忽略音频数据
     */
    SILENCE(-1, 0, 0, 0, 0),
    /**
     * G726 有符号数 16bit 小端 单声道
     */
    G726_S16_LE_MONO(0, 8_000, 2, 16, 1),
    /**
     * G726 有符号数 24bit 小端 单声道
     */
    G726_S24_LE_MONO(0, 8_000, 3, 16, 1),
    /**
     * G726 有符号数 32bit 小端 单声道
     */
    G726_S32_LE_MONO(0, 8_000, 4, 16, 1),
    /**
     * G726 有符号数 40bit 小端 单声道
     */
    G726_S40_LE_MONO(0, 8_000, 5, 16, 1),

    /**
     * ADPCM IMA 单声道
     */
    ADPCM_IMA_MONO(1, 8_000, 8, 16, 1),
    /**
     * G711_A 单声道
     */
    G711_A_MONO(2, 8_000, 8, 8, 1),
    /**
     * G711_U 单声道
     */
    G711_U_MONO(2, 8_000, 8, 8, 1),

    /**
     * PCM 有符号数 16bit 小端 单声道
     */
    PCM_S16_LE_MONO(3, 8_000, 16, 16, 1),
    /**
     * PCM 有符号数 24bit 小端 单声道
     */
    PCM_S24_LE_MONO(3, 8_000, 24, 24, 1),
    /**
     * PCM 有符号数 32bit 小端 单声道
     */
    PCM_S32_LE_MONO(3, 8_000, 32, 32, 1),
    /**
     * PCM 有符号数 40bit 小端 单声道
     */
    PCM_S40_LE_MONO(3, 8_000, 40, 40, 1),
    ;

    private final int family;
    private final int sampleRate;
    private final int bitCount;
    private final int bitDepth;
    private final int channelCount;

    BuiltinAudioFormatOptions(int family, int sampleRate, int bitCount, int bitDepth, int channelCount) {
        this.family = family;
        this.sampleRate = sampleRate;
        this.bitCount = bitCount;
        this.bitDepth = bitDepth;
        this.channelCount = channelCount;
    }

    @Override
    public int sampleRate() {
        return sampleRate;
    }

    @Override
    public int bitDepth() {
        return bitDepth;
    }

    @Override
    public int channelCount() {
        return channelCount;
    }

    @Override
    public int bitCount() {
        return bitCount;
    }

    public boolean isG726() {
        return this.family == 0;
    }

    public boolean isAdpcm() {
        return this.family == 1;
    }

    public boolean isG711() {
        return this.family == 2;
    }

    public int estimateDecodedPcmSize(int g726Size) {
        return g726Size / bitCount() * 16;
    }

    public int estimateEncodedG726Size(int pcmSize) {
        return pcmSize / 16 * bitCount();
    }

    @Override
    public String toString() {
        return "BuiltinAudioFormatOptions{"
                + this.name() + ": family=" + family
                + ", sampleRate=" + sampleRate
                + ", bitCount=" + bitCount
                + ", bitDepth=" + bitDepth
                + ", channelCount=" + channelCount
                + ", bitRate=" + bitRate()
                + '}';
    }

    private static final Map<String, BuiltinAudioFormatOptions> NAME_CACHE = new HashMap<>(BuiltinAudioFormatOptions.values().length);

    static {
        for (final BuiltinAudioFormatOptions value : BuiltinAudioFormatOptions.values()) {
            NAME_CACHE.put(value.name(), value);
        }
    }

    public static Optional<BuiltinAudioFormatOptions> parseFrom(String name) {
        return Optional.ofNullable(NAME_CACHE.get(name));
    }
}