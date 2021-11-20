package io.github.hylexus.jt808.support;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.handler.MsgHandler;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.support.handler.scan.BytesEncoderAware;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author hylexus
 * Created At 2019-08-24 16:12
 */
@Slf4j
public class MsgHandlerMapping {

    // <msgId,<version,handler>>
    private final Map<Integer, Map<Jt808ProtocolVersion, MsgHandler<? extends RequestMsgBody>>> mappings;
    private final BytesEncoder bytesEncoder;

    @Setter
    private Supplier<MsgHandler<? extends RequestMsgBody>> defaultMsgHandlerSupplier;

    public MsgHandlerMapping(BytesEncoder bytesEncoder) {
        this.bytesEncoder = bytesEncoder;
        this.mappings = new HashMap<>();
    }

    private boolean containsHandler(@NonNull MsgType msgType, Jt808ProtocolVersion version) {
        return Optional.ofNullable(mappings.get(msgType.getMsgId())).map(m -> m.get(version)).isPresent();
    }

    private void registerWithAwareInterfaceCheck(@NonNull MsgHandler<? extends RequestMsgBody> handler, int msgId, Jt808ProtocolVersion version) {
        if (handler instanceof BytesEncoderAware) {
            ((BytesEncoderAware) handler).setBytesEncoder(this.bytesEncoder);
        }
        final Map<Jt808ProtocolVersion, MsgHandler<? extends RequestMsgBody>> map = this.mappings.computeIfAbsent(msgId, k -> new HashMap<>());
        map.put(version, handler);
        if (map.size() == Jt808ProtocolVersion.values().length) {
            map.remove(Jt808ProtocolVersion.AUTO_DETECTION);
        }
    }

    public MsgHandlerMapping registerHandlerWhen(@NonNull MsgHandler<? extends RequestMsgBody> handler, boolean register) {
        if (register) {
            this.registerHandler(handler);
        }
        return this;
    }

    public MsgHandlerMapping registerHandler(@NonNull MsgHandler<? extends RequestMsgBody> handler) {
        return registerHandler(handler, false);
    }

    public MsgHandlerMapping registerHandler(@NonNull MsgHandler<? extends RequestMsgBody> handler, boolean forceOverride) {
        Set<MsgType> supportedMsgTypes = handler.getSupportedMsgTypes();

        if (CollectionUtils.isEmpty(supportedMsgTypes)) {
            throw new JtIllegalStateException("MsgHandler.getSupportedMsgTypes() is null or empty");
        }
        supportedMsgTypes.forEach(msgType -> registerHandler(msgType, handler, forceOverride));
        return this;
    }

    public MsgHandlerMapping registerHandler(@NonNull MsgType msgType, @NonNull MsgHandler<? extends RequestMsgBody> handler) {
        return registerHandler(msgType, handler, false);
    }

    public MsgHandlerMapping registerHandler(@NonNull MsgType msgType, @NonNull MsgHandler<? extends RequestMsgBody> handler, boolean forceOverride) {
        for (Jt808ProtocolVersion version : handler.getSupportedProtocolVersions()) {
            this.registerHandler(msgType, version, handler, forceOverride);
        }
        return this;
    }

    public MsgHandlerMapping registerHandler(
            @NonNull MsgType msgType, Jt808ProtocolVersion version, @NonNull MsgHandler<? extends RequestMsgBody> handler, boolean forceOverride) {
        final int msgId = msgType.getMsgId();
        final Map<Jt808ProtocolVersion, MsgHandler<? extends RequestMsgBody>> map = mappings.computeIfAbsent(msgId, k -> new HashMap<>());
        if (map.containsKey(version)) {
            final MsgHandler<? extends RequestMsgBody> oldHandler = map.get(version);
            if (forceOverride || oldHandler.shouldBeReplacedBy(handler)) {
                log.warn("Duplicate MsgType : {}, the MsgHandler [{}] was replaced by {}", msgType, oldHandler.getClass(), handler);
                this.registerWithAwareInterfaceCheck(handler, msgId, version);
            } else {
                log.info("Duplicate MsgType  [{}] with [{}], the MsgHandler [{}] register is skipped.",
                        msgType, oldHandler.getClass(), handler);
            }
        } else {
            this.registerWithAwareInterfaceCheck(handler, msgId, version);
        }
        return this;
    }

    public Optional<MsgHandler<? extends RequestMsgBody>> getHandler(MsgType msgType, Jt808ProtocolVersion version) {
        final Optional<MsgHandler<? extends RequestMsgBody>> optional = Optional.ofNullable(mappings.get(msgType.getMsgId()))
                .map(m -> m.getOrDefault(version, m.get(Jt808ProtocolVersion.AUTO_DETECTION)));
        if (optional.isPresent()) {
            return optional;
        }
        return defaultMsgHandlerSupplier == null
                ? Optional.empty()
                : Optional.ofNullable(defaultMsgHandlerSupplier.get());
    }

    public Map<Integer, Map<Jt808ProtocolVersion, MsgHandler<? extends RequestMsgBody>>> getHandlerMappings() {
        return Collections.unmodifiableMap(this.mappings);
    }
}
