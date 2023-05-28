package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078SubPackageIdentifier;
import io.github.hylexus.jt.jt1078.support.exception.Jt1078DecodeException;

import java.util.Optional;

/**
 * @author hylexus
 */
public interface Jt1078SubPackageIdentifier {
    Jt1078SubPackageIdentifier ATOMIC = DefaultJt1078SubPackageIdentifier.ATOMIC;
    Jt1078SubPackageIdentifier FIRST_SUB_PACKAGE = DefaultJt1078SubPackageIdentifier.FIRST_SUB_PACKAGE;
    Jt1078SubPackageIdentifier LAST_SUB_PACKAGE = DefaultJt1078SubPackageIdentifier.LAST_SUB_PACKAGE;
    Jt1078SubPackageIdentifier MIDDLE_SUB_PACKAGE = DefaultJt1078SubPackageIdentifier.MIDDLE_SUB_PACKAGE;

    byte value();

    String desc();

    Optional<Jt1078SubPackageIdentifier> fromByte(byte value);

    default boolean isAtomic() {
        return this == DefaultJt1078SubPackageIdentifier.ATOMIC;
    }

    default boolean isFirst() {
        return this == Jt1078SubPackageIdentifier.FIRST_SUB_PACKAGE;
    }

    default boolean isLast() {
        return this == Jt1078SubPackageIdentifier.LAST_SUB_PACKAGE;
    }

    default boolean isMiddle() {
        return this == Jt1078SubPackageIdentifier.MIDDLE_SUB_PACKAGE;
    }

    static Jt1078SubPackageIdentifier defaultOrCreate(byte value) {
        return Jt1078SubPackageIdentifier.ATOMIC.fromByte(value)
                .orElseThrow(() -> new Jt1078DecodeException("Unknown subPackageIdentifier " + value));
    }

}
