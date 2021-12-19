package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.spec.Jt808ProtocolVersionDetector;
import io.github.hylexus.jt.jt808.spec.Jt808ProtocolVersionDetectorRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hylexus
 */
@BuiltinComponent
public class DefaultJt808ProtocolVersionDetectorRegistry implements Jt808ProtocolVersionDetectorRegistry {
    private final Map<Integer, Jt808ProtocolVersionDetector> mapping = new HashMap<>();

    private Jt808ProtocolVersionDetector defaultDetector;

    public DefaultJt808ProtocolVersionDetectorRegistry(Jt808ProtocolVersionDetector defaultDetector) {
        this.defaultDetector = Objects.requireNonNull(defaultDetector);
    }

    @Override
    public Jt808ProtocolVersionDetector getJt808ProtocolVersionDetector(int msgId) {
        return Optional.ofNullable(this.mapping.get(msgId)).orElse(defaultDetector);
    }

    @Override
    public Jt808ProtocolVersionDetectorRegistry register(int msgId, Jt808ProtocolVersionDetector detector) {
        Objects.requireNonNull(detector);
        final Jt808ProtocolVersionDetector old = this.mapping.get(msgId);
        if (old != null) {
            if (old.shouldBeReplacedBy(detector)) {
                this.mapping.put(msgId, detector);
            }
        } else {
            this.mapping.put(msgId, detector);
        }
        return this;
    }

    @Override
    public Jt808ProtocolVersionDetectorRegistry clear() {
        this.mapping.clear();
        return this;
    }

    @Override
    public Jt808ProtocolVersionDetector defaultDetector() {
        return this.defaultDetector;
    }

    @Override
    public Jt808ProtocolVersionDetectorRegistry defaultDetector(Jt808ProtocolVersionDetector detector) {
        this.defaultDetector = Objects.requireNonNull(detector);
        return this;
    }
}
