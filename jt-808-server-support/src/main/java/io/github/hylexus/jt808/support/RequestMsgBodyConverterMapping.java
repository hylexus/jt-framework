package io.github.hylexus.jt808.support;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-08-22 21:21
 */
@Slf4j
public class RequestMsgBodyConverterMapping {

    // <msgId, <version,converter>>
    private final Map<Integer, Map<Jt808ProtocolVersion, RequestMsgBodyConverter<? extends RequestMsgBody>>> mappings;

    public RequestMsgBodyConverterMapping() {
        this.mappings = new HashMap<>();
    }

    public Optional<RequestMsgBodyConverter<? extends RequestMsgBody>> getConverter(MsgType msgType, Jt808ProtocolVersion version) {
        return Optional.ofNullable(mappings.get(msgType.getMsgId()))
                .map(m -> m.getOrDefault(version, m.get(Jt808ProtocolVersion.AUTO_DETECTION)));
    }

    public RequestMsgBodyConverterMapping registerConverterWhen(
            MsgType msgType, RequestMsgBodyConverter<? extends RequestMsgBody> converter, boolean condition) {
        if (condition) {
            this.registerConverter(msgType, converter);
        }
        return this;
    }

    public RequestMsgBodyConverterMapping registerConverter(
            MsgType msgType, Jt808ProtocolVersion version, RequestMsgBodyConverter<? extends RequestMsgBody> converter, boolean forceOverride) {

        final int msgId = msgType.getMsgId();
        final Map<Jt808ProtocolVersion, RequestMsgBodyConverter<? extends RequestMsgBody>> map = mappings.computeIfAbsent(msgId, k -> new HashMap<>());
        if (map.containsKey(version)) {
            final RequestMsgBodyConverter<? extends RequestMsgBody> oldConverter = map.get(version);
            if ((forceOverride || oldConverter.shouldBeReplacedBy(converter))) {
                map.put(version, converter);
                log.warn("duplicate msgType : {}, the MsgConverter {} was replaced by {}", msgType, oldConverter.getClass(), converter.getClass());
            }
        } else {
            map.put(version, converter);
        }

        if (map.size() == Jt808ProtocolVersion.values().length) {
            map.remove(Jt808ProtocolVersion.AUTO_DETECTION);
        }

        return this;
    }

    public RequestMsgBodyConverterMapping registerConverter(
            MsgType msgType, RequestMsgBodyConverter<? extends RequestMsgBody> converter) {
        return registerConverter(msgType, converter, false);
    }

    public RequestMsgBodyConverterMapping registerConverter(
            MsgType msgType, RequestMsgBodyConverter<? extends RequestMsgBody> converter, boolean forceOverride) {

        for (Jt808ProtocolVersion version : converter.getSupportedProtocolVersion()) {
            this.registerConverter(msgType, version, converter, forceOverride);
        }
        return this;
    }

    public boolean containsConverter(MsgType msgType) {
        return this.mappings.containsKey(msgType.getMsgId());
    }

    public boolean isEmpty() {
        return this.mappings == null || this.mappings.isEmpty();
    }

    public Map<Integer, Map<Jt808ProtocolVersion, RequestMsgBodyConverter<? extends RequestMsgBody>>> getMsgConverterMappings() {
        return Collections.unmodifiableMap(this.mappings);
    }
}
