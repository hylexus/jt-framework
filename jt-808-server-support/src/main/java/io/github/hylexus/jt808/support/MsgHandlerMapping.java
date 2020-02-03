package io.github.hylexus.jt808.support;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.handler.MsgHandler;
import io.github.hylexus.jt808.support.handler.scan.BytesEncoderAware;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static java.util.Optional.of;

/**
 * @author hylexus
 * Created At 2019-08-24 16:12
 */
@Slf4j
public class MsgHandlerMapping {

    //private Map<MsgType, MsgHandler> mapping;
    private Map<Integer, MsgHandler> mapping;
    private final BytesEncoder bytesEncoder;

    @Setter
    private Supplier<MsgHandler> defaultMsgHandlerSupplier;

    public MsgHandlerMapping(BytesEncoder bytesEncoder) {
        this.bytesEncoder = bytesEncoder;
        this.mapping = new ConcurrentHashMap<>();
    }

    private boolean containsHandler(@NonNull MsgType msgType) {
        return mapping.containsKey(msgType.getMsgId());
    }

    private void registerWithAwareInterfaceCheck(@NonNull MsgHandler handler, int msgId) {
        if (handler instanceof BytesEncoderAware) {
            ((BytesEncoderAware) handler).setBytesEncoder(this.bytesEncoder);
        }
        this.mapping.put(msgId, handler);
    }

    public MsgHandlerMapping registerHandler(@NonNull MsgType msgType, @NonNull MsgHandler handler) {
        return registerHandler(msgType, handler, false);
    }

    public MsgHandlerMapping registerHandler(@NonNull MsgHandler handler) {
        return registerHandler(handler, false);
    }

    public MsgHandlerMapping registerHandler(@NonNull MsgHandler handler, boolean forceOverride) {
        @SuppressWarnings("unchecked")
        Set<MsgType> supportedMsgTypes = handler.getSupportedMsgTypes();

        if (CollectionUtils.isEmpty(supportedMsgTypes)) {
            throw new JtIllegalStateException("MsgHandler.getSupportedMsgTypes() is null or empty");
        }
        supportedMsgTypes.forEach(msgType -> registerHandler(msgType, handler, forceOverride));
        return this;
    }

    public MsgHandlerMapping registerHandler(@NonNull MsgType msgType, @NonNull MsgHandler handler, boolean forceOverride) {
        final int msgId = msgType.getMsgId();
        if (containsHandler(msgType)) {
            MsgHandler oldHandler = mapping.get(msgId);
            if (forceOverride || oldHandler.shouldBeReplacedBy(handler)) {
                log.warn("Duplicate MsgType : {}, the MsgHandler [{}] was replaced by {}", msgType, oldHandler.getClass(), handler);
                this.registerWithAwareInterfaceCheck(handler, msgId);
            } else {
                log.info("Duplicate MsgType  [{}] with [{}], the MsgHandler [{}] register is skipped.",
                        msgType, oldHandler.getClass(), handler);
            }
            return this;
        }

        this.registerWithAwareInterfaceCheck(handler, msgId);
        return this;
    }

    public Optional<MsgHandler> getHandler(MsgType msgType) {
        MsgHandler msgHandler = mapping.get(msgType.getMsgId());
        if (msgHandler != null) {
            return of(msgHandler);
        }

        return defaultMsgHandlerSupplier == null
                ? Optional.empty()
                : Optional.ofNullable(defaultMsgHandlerSupplier.get());
    }

    public Map<Integer, MsgHandler> getHandlerMappings() {
        return Collections.unmodifiableMap(this.mapping);
    }
}
