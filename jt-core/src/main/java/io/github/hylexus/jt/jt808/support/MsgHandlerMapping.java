package io.github.hylexus.jt.jt808.support;

import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.handler.MsgHandler;
import io.github.hylexus.jt.jt808.msg.MsgType;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

/**
 * @author hylexus
 * Created At 2019-08-24 16:12
 */
@Slf4j
public class MsgHandlerMapping {
    private Map<MsgType, MsgHandler> mapping;

    @Setter
    private Supplier<MsgHandler> defaultMsgHandlerSupplier;

    public MsgHandlerMapping() {
        this.mapping = new ConcurrentHashMap<>();
    }

    private boolean containsHandler(@NonNull MsgType msgType) {
        return mapping.containsKey(msgType);
    }

    public MsgHandlerMapping registerHandler(@NonNull MsgType msgType, @NonNull MsgHandler handler) {
        if (containsHandler(msgType)) {
            log.warn("duplicate msgType:{}, the MsgHandler {} is replaced by {}", msgType, mapping.get(msgType).getClass(), handler);
        }
        this.mapping.put(msgType, handler);
        return this;
    }

    public MsgHandlerMapping registerHandler(@NonNull MsgHandler handler) {
        Set<MsgType> supportedMsgTypes = handler.getSupportedMsgTypes();

        if (CollectionUtils.isEmpty(supportedMsgTypes)) {
            throw new JtIllegalStateException("MsgHandler.getSupportedMsgTypes() is null or empty");
        }
        supportedMsgTypes.forEach(msgType -> registerHandler(msgType, handler));
        return this;
    }

    public Optional<MsgHandler> getHandler(MsgType msgType) {
        MsgHandler msgHandler = mapping.get(msgType);
        if (msgHandler != null) {
            return of(msgHandler);
        }

        return ofNullable(defaultMsgHandlerSupplier.get());
    }
}
