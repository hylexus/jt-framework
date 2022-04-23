package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078DataType;

import java.util.Optional;

/**
 * @author hylexus
 */
public interface Jt1078DataType {
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
        return DefaultJt1078DataType.VIDEO_I.fromByte(value)
                .orElseGet(() -> new Jt1078DataType.UnknownJt1078DataType(value));
    }

    class UnknownJt1078DataType implements Jt1078DataType {
        private byte value;
        private String desc;

        public UnknownJt1078DataType(byte value) {
            this(value, "UnknownJt1078DataType(" + value + ")");
        }

        public UnknownJt1078DataType(byte value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public byte value() {
            return value;
        }

        public UnknownJt1078DataType value(byte value) {
            this.value = value;
            return this;
        }

        @Override
        public String desc() {
            return desc;
        }

        public UnknownJt1078DataType desc(String desc) {
            this.desc = desc;
            return this;
        }

        @Override
        public Optional<Jt1078DataType> fromByte(byte value) {
            return Optional.of(new UnknownJt1078DataType(value));
        }
    }

}
