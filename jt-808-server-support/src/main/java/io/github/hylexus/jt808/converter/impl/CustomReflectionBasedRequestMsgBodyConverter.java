package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.codec.Decoder;
import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author hylexus
 * Created At 2019-09-19 10:35 下午
 */
@Slf4j
public class CustomReflectionBasedRequestMsgBodyConverter implements RequestMsgBodyConverter<RequestMsgBody> {

    private final Decoder decoder;
    /**
     * <pre>
     *     MsgType -> RequestMsgBody.class
     * </pre>
     */
    // private final Map<Integer, Class<? extends RequestMsgBody>> msgTypeMsgBodyClassMapping = new HashMap<>();
    private final Map<Integer, Map<Jt808ProtocolVersion, Class<? extends RequestMsgBody>>> msgTypeMsgBodyClassMapping = new HashMap<>();

    public CustomReflectionBasedRequestMsgBodyConverter(Decoder decoder) {
        this.decoder = decoder;
    }

    public Map<Integer, Map<Jt808ProtocolVersion, Class<? extends RequestMsgBody>>> getMsgBodyMapping() {
        return Collections.unmodifiableMap(msgTypeMsgBodyClassMapping);
    }

    @Override
    public int getOrder() {
        return ANNOTATION_BASED_DEV_COMPONENT_ORDER;
    }

    public CustomReflectionBasedRequestMsgBodyConverter addSupportedMsgBody(
            MsgType msgType, Jt808ProtocolVersion version, Class<? extends RequestMsgBody> cls, boolean forceOverride) {

        int msgId = msgType.getMsgId();
        final Map<Jt808ProtocolVersion, Class<? extends RequestMsgBody>> map = msgTypeMsgBodyClassMapping.computeIfAbsent(msgId, k -> new HashMap<>());
        if (map.containsKey(version)) {
            if (forceOverride) {
                map.put(version, cls);
            }
        } else {
            map.put(version, cls);
        }
        if (map.size() == Jt808ProtocolVersion.values().length) {
            map.remove(Jt808ProtocolVersion.AUTO_DETECTION);
        }
        return this;
    }

    public CustomReflectionBasedRequestMsgBodyConverter addSupportedMsgBody(
            MsgType msgType, Jt808ProtocolVersion version, Class<? extends RequestMsgBody> cls) {

        return addSupportedMsgBody(msgType, version, cls, false);
    }

    @Override
    public Optional<RequestMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        final byte[] bytes = metadata.getBodyBytes();
        final int msgId = metadata.getMsgType().getMsgId();
        final Optional<Class<? extends RequestMsgBody>> optionalTargetBodyClass = getConverter(msgId, metadata.getHeader().getVersion());

        if (!optionalTargetBodyClass.isPresent()) {
            log.warn("Can not convert {}", metadata.getMsgType());
            return Optional.empty();
        }

        try {
            RequestMsgBody o = decoder.decodeRequestMsgBody(optionalTargetBodyClass.get(), bytes, metadata);
            return Optional.of(o);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<Class<? extends RequestMsgBody>> getConverter(int msgId, Jt808ProtocolVersion version) {
        return Optional.ofNullable(msgTypeMsgBodyClassMapping.get(msgId))
                .map(m -> m.getOrDefault(version, m.get(Jt808ProtocolVersion.AUTO_DETECTION)));
    }

}
