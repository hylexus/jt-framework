package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.jt1078.support.exception.Jt1078DecodeException;

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
                .orElseThrow(() -> new Jt1078DecodeException("Unknown payLoadType " + pt));
    }

}