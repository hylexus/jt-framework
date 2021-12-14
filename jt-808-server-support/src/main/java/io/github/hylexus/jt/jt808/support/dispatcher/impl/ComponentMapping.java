package io.github.hylexus.jt.jt808.support.dispatcher.impl;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.support.dispatcher.MultipleVersionSupport;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * @author hylexus
 */
@Slf4j
public class ComponentMapping<T extends MultipleVersionSupport> {
    private final Map<Integer, Map<Jt808ProtocolVersion, T>> mappings = new HashMap<>();

    public Optional<T> getComponent(MsgType msgType, Jt808ProtocolVersion version) {
        return ofNullable(mappings.get(msgType.getMsgId()))
                .flatMap(m -> ofNullable(m.getOrDefault(version, m.get(Jt808ProtocolVersion.AUTO_DETECTION))));
    }

    public void register(T component) {
        for (MsgType msgType : component.getSupportedMsgTypes()) {
            for (Jt808ProtocolVersion version : component.getSupportedVersions()) {
                this.register(component, msgType, version);
            }
        }
    }

    public void register(T component, MsgType msgType) {
        for (Jt808ProtocolVersion version : component.getSupportedVersions()) {
            this.register(component, msgType, version);
        }
    }

    public void register(T component, MsgType msgType, Jt808ProtocolVersion version) {
        final int msgId = msgType.getMsgId();
        final Map<Jt808ProtocolVersion, T> map = mappings.computeIfAbsent(msgId, k -> new HashMap<>());
        if (map.containsKey(version)) {
            final T oldComponent = map.get(version);
            if (oldComponent.shouldBeReplacedBy(component)) {
                map.put(version, component);
                log.warn("duplicate msgType : {}, the Component {} was replaced by {}", msgType, oldComponent.getClass(), component.getClass());
            }
        } else {
            map.put(version, component);
        }

        if (map.size() == Jt808ProtocolVersion.values().length) {
            map.remove(Jt808ProtocolVersion.AUTO_DETECTION);
        }
    }

}
