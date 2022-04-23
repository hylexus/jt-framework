package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078SubPackageIdentifier;

import java.util.Optional;

/**
 * @author hylexus
 */
public interface Jt1078SubPackageIdentifier {
    byte value();

    String desc();

    Optional<Jt1078SubPackageIdentifier> fromByte(byte value);

    default boolean isAtomic() {
        return this == DefaultJt1078SubPackageIdentifier.ATOMIC;
    }

    default boolean isFirst() {
        return this == DefaultJt1078SubPackageIdentifier.FIRST_SUB_PACKAGE;
    }

    default boolean isLast() {
        return this == DefaultJt1078SubPackageIdentifier.LAST_SUB_PACKAGE;
    }

    default boolean isMiddle() {
        return this == DefaultJt1078SubPackageIdentifier.MIDDLE_SUB_PACKAGE;
    }

    static Jt1078SubPackageIdentifier defaultOrCreate(byte value) {
        return DefaultJt1078SubPackageIdentifier.ATOMIC.fromByte(value)
                .orElseGet(() -> new UnknownJt1078SubPackageIdentifier(value));
    }

    class UnknownJt1078SubPackageIdentifier implements Jt1078SubPackageIdentifier {
        private final byte value;
        private final String desc;

        public UnknownJt1078SubPackageIdentifier(byte value) {
            this(value, "UnknownJt1078SubPackageIdentifier");
        }

        public UnknownJt1078SubPackageIdentifier(byte value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public byte value() {
            return value;
        }

        @Override
        public String desc() {
            return desc;
        }

        @Override
        public Optional<Jt1078SubPackageIdentifier> fromByte(byte value) {
            return Optional.of(this);
        }

        @Override
        public String toString() {
            return desc + "(" + this.value + ")";
        }
    }
}
