package io.github.hylexus.jt.jt808.support.dispatcher.impl;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.support.dispatcher.MultipleVersionSupport;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.HandlerMethod;
import io.github.hylexus.jt.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
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
                log.info("Duplicate msgType : {}, the Component [{}] has been replaced by [{}]",
                        msgType, formatComponentName(oldComponent), formatComponentName(component)
                );
            }
        } else {
            map.put(version, component);
        }

        if (map.size() == Jt808ProtocolVersion.values().length) {
            map.remove(Jt808ProtocolVersion.AUTO_DETECTION);
        }
    }

    private String formatComponentName(MultipleVersionSupport instance) {
        if (instance instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) instance;
            return CommonUtils.shortClassName(handlerMethod.getBeanInstance().getClass()) + "#" + handlerMethod.getMethod().getName();
        }
        return CommonUtils.shortClassName(instance.getClass());
    }

    public Map<Integer, Map<Jt808ProtocolVersion, T>> getMappings() {
        return Collections.unmodifiableMap(mappings);
    }
}
