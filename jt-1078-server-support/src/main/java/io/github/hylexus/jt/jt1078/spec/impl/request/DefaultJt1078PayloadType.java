package io.github.hylexus.jt.jt1078.spec.impl.request;

import io.github.hylexus.jt.jt1078.spec.Jt1078PayloadType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum DefaultJt1078PayloadType implements Jt1078PayloadType {

    G_721((byte) 1, "音频"),
    G_722((byte) 2, "音频"),
    G_723((byte) 3, "音频"),
    G_728((byte) 4, "音频"),
    G_729((byte) 5, "音频"),
    G_711A((byte) 6, "音频"),
    G_711U((byte) 7, "音频"),
    G_726((byte) 8, "音频"),
    G_729A((byte) 9, "音频"),
    DVI4_3((byte) 10, "音频"),
    DVI4_4((byte) 11, "音频"),
    DVI4_8K((byte) 12, "音频"),
    DVI4_16K((byte) 13, "音频"),
    LPC((byte) 14, "音频"),
    S16BE_STEREO((byte) 15, "音频"),
    S16BE_MONO((byte) 16, "音频"),
    MPEGAUDIO((byte) 17, "音频"),
    LPCM((byte) 18, "音频"),
    AAC((byte) 19, "音频"),
    WMA9STD((byte) 20, "音频"),
    HEAAC((byte) 21, "音频"),
    PCM_VOICE((byte) 22, "音频"),
    PCM_AUDIO((byte) 23, "音频"),
    AACLC((byte) 24, "音频"),
    MP3((byte) 25, "音频"),
    ADPCMA((byte) 26, "音频"),
    MP4AUDIO((byte) 27, "音频"),
    AMR((byte) 28, "音频"),
    // [29,90] 保留
    TRANSPARENT_TRANSMISSION((byte) 91, "透传"),
    // [92,97] 保留
    H264((byte) 98, "视频"),
    H265((byte) 99, "视频"),
    AVS((byte) 100, "视频"),
    SVAC((byte) 101, "视频"),
    // [102,110] 保留
    // [111,127] 自定义
    ;

    private final byte value;
    private final String desc;

    DefaultJt1078PayloadType(byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static final Map<Byte, DefaultJt1078PayloadType> MAPPING = new HashMap<>();

    static {
        for (DefaultJt1078PayloadType value : values()) {
            MAPPING.put(value.value, value);
        }
    }

    @Override
    public Optional<Jt1078PayloadType> fromByte(byte pt) {
        return Optional.ofNullable(MAPPING.get(pt));
    }

    @Override
    public byte value() {
        return this.value;
    }

    @Override
    public String desc() {
        return this.desc;
    }
}