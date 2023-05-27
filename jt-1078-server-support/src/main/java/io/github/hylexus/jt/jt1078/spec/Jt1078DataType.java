package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078DataType;
import io.github.hylexus.jt.jt1078.support.exception.Jt1078DecodeException;

import java.util.Optional;

/**
 * @author hylexus
 */
public interface Jt1078DataType {
    Jt1078DataType VIDEO_I = DefaultJt1078DataType.VIDEO_I;
    Jt1078DataType VIDEO_P = DefaultJt1078DataType.VIDEO_P;
    Jt1078DataType VIDEO_B = DefaultJt1078DataType.VIDEO_B;
    Jt1078DataType AUDIO = DefaultJt1078DataType.AUDIO;
    Jt1078DataType TRANSPARENT_TRANSMISSION = DefaultJt1078DataType.TRANSPARENT_TRANSMISSION;

    byte value();

    String desc();

    Optional<Jt1078DataType> fromByte(byte value);

    static boolean isAudio(byte dataType) {
        return (dataType & 0b11) == 0b11;
    }

    default boolean isAudio() {
        return isAudio(this.value());
    }

    static boolean isVideo(byte dataType) {
        // 0000: 视频I帧
        // 0001: 视频P帧
        // 0010: 视频B帧
        // 0011: 音频帧
        // 0100: 透传数据
        return (dataType & 0b11) <= 0b10;
    }

    default boolean isVideo() {
        return isVideo(this.value());
    }

    static Jt1078DataType createOrDefault(byte value) {
        return Jt1078DataType.VIDEO_I.fromByte(value)
                .orElseThrow(() -> new Jt1078DecodeException("Unknown dataType " + value));
    }

}
