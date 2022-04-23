package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;

import java.util.Optional;

public interface Jt1078PayloadType {

    byte value();

    String desc();

    Optional<Jt1078PayloadType> fromByte(byte pt);

    default boolean isAudio() {
        return isAudio(this.value());
    }

    static boolean isAudio(byte payloadType) {
        return payloadType <= 28 && payloadType >= 1;
    }

    default boolean isVideo() {
        return isVideo(this.value());
    }

    static boolean isVideo(byte payloadType) {
        return payloadType <= 101 && payloadType >= 92;
    }

    static Jt1078PayloadType createOrDefault(byte pt) {
        return DefaultJt1078PayloadType.AAC.fromByte(pt)
                .orElseGet(() -> new UnknownJt1078PayloadType(pt));
    }

    class UnknownJt1078PayloadType implements Jt1078PayloadType {
        private byte value;
        private String desc;

        public UnknownJt1078PayloadType(byte value) {
            this(value, "UnknownJt1078PayloadType(" + value + ")");
        }

        public UnknownJt1078PayloadType(byte value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public byte value() {
            return value;
        }

        public UnknownJt1078PayloadType value(byte value) {
            this.value = value;
            return this;
        }

        @Override
        public String desc() {
            return desc;
        }

        public UnknownJt1078PayloadType desc(String desc) {
            this.desc = desc;
            return this;
        }

        @Override
        public Optional<Jt1078PayloadType> fromByte(byte pt) {
            return Optional.of(new UnknownJt1078PayloadType(pt));
        }
    }


}