package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.codec.Decoder;
import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    private final Map<Integer, Class<? extends RequestMsgBody>> msgTypeMsgBodyClassMapping = new HashMap<>();

    public CustomReflectionBasedRequestMsgBodyConverter(Decoder decoder) {
        this.decoder = decoder;
    }

    public Map<Integer, Class<? extends RequestMsgBody>> getMsgBodyMapping() {
        return Collections.unmodifiableMap(msgTypeMsgBodyClassMapping);
    }

    @Override
    public int getOrder() {
        return ANNOTATION_BASED_DEV_COMPONENT_ORDER;
    }

    public CustomReflectionBasedRequestMsgBodyConverter addSupportedMsgBody(
            MsgType msgType, Class<? extends RequestMsgBody> cls, boolean forceOverride) {

        int msgId = msgType.getMsgId();
        if (msgTypeMsgBodyClassMapping.containsKey(msgId)) {
            if (forceOverride) {
                this.msgTypeMsgBodyClassMapping.put(msgId, cls);
            }
        } else {
            msgTypeMsgBodyClassMapping.put(msgId, cls);
        }
        return this;
    }

    public CustomReflectionBasedRequestMsgBodyConverter addSupportedMsgBody(MsgType msgType, Class<? extends RequestMsgBody> cls) {
        return addSupportedMsgBody(msgType, cls, false);
    }

    @Override
    public Optional<RequestMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        byte[] bytes = metadata.getBodyBytes();
        Class<? extends RequestMsgBody> targetBodyClass = msgTypeMsgBodyClassMapping.get(metadata.getMsgType().getMsgId());
        if (targetBodyClass == null) {
            log.warn("Can not convert {}", metadata.getMsgType());
            return Optional.empty();
        }
        try {
            RequestMsgBody o = decoder.decodeRequestMsgBody(targetBodyClass, bytes, metadata);
            return Optional.of(o);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

}
