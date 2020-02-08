package io.github.hylexus.jt808.handler.impl.reflection;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.converter.ResponseMsgBodyConverter;
import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.handler.ExceptionHandler;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.resp.VoidRespMsgBody;
import io.github.hylexus.jt808.session.Session;
import io.github.hylexus.jt808.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hylexus
 * Created At 2020-02-01 5:58 下午
 */
@Slf4j
public class CustomReflectionBasedRequestMsgHandler extends AbstractMsgHandler<RequestMsgBody> {

    private Set<MsgType> supportedMsgTypes = new HashSet<>();
    private ConcurrentMap<MsgType, HandlerMethod> mapping = new ConcurrentHashMap<>();
    private final HandlerMethodArgumentResolver argumentResolver;
    private final ResponseMsgBodyConverter responseMsgBodyConverter;
    private final ExceptionHandler exceptionHandler;

    public CustomReflectionBasedRequestMsgHandler(HandlerMethodArgumentResolver argumentResolver, ResponseMsgBodyConverter responseMsgBodyConverter, ExceptionHandler exceptionHandler) {
        this.argumentResolver = argumentResolver;
        this.responseMsgBodyConverter = responseMsgBodyConverter;
        this.exceptionHandler = exceptionHandler;
    }

    public Map<MsgType, HandlerMethod> getHandlerMethodMapping() {
        return Collections.unmodifiableMap(mapping);
    }

    @Override
    public int getOrder() {
        return ANNOTATION_BASED_DEV_COMPONENT_ORDER;
    }

    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return supportedMsgTypes;
    }

    public void addSupportedMsgType(MsgType msgType, HandlerMethod handlerMethod) {
        this.supportedMsgTypes.add(msgType);
        this.mapping.put(msgType, handlerMethod);
    }

    @Override
    protected Optional<RespMsgBody> doProcess(RequestMsgMetadata metadata, RequestMsgBody msg, Session session) {
        final MsgType msgType = metadata.getMsgType();
        final HandlerMethod handlerMethod = mapping.get(msgType);
        if (handlerMethod == null) {
            log.warn("No HandlerMethod found for msgType {}, ReflectionBasedRequestMsgHandler return empty().", msgType);
            return Optional.empty();
        }

        Object result;
        try {
            result = this.invokeHandlerMethod(handlerMethod, metadata, msg, session);
        } catch (Throwable e) {
            try {
                result = this.exceptionHandler.handleException(metadata, session, handlerMethod, msg, e);
            } catch (Throwable throwable) {
                log.error("An unexpected exception occurred while invoke ExceptionHandler", throwable);
                return Optional.of(VoidRespMsgBody.NO_DATA_WILL_BE_SENT_TO_CLIENT);
            }
        }

        // [userType] --> RespMsgBody
        return this.responseMsgBodyConverter.convert(result, session, metadata);
    }

    private Object invokeHandlerMethod(HandlerMethod handlerMethod, RequestMsgMetadata metadata, RequestMsgBody msg, Session session) throws InvocationTargetException, IllegalAccessException {
        final Object[] args = this.resolveArgs(handlerMethod, metadata, msg, session);
        return doInvoke(handlerMethod, args);
    }

    private Object[] resolveArgs(HandlerMethod handlerMethod, RequestMsgMetadata metadata, RequestMsgBody msg, Session session) {
        return CommonUtils.resolveArguments(handlerMethod, metadata, msg, session, this.argumentResolver);
    }


    private Object doInvoke(HandlerMethod handlerMethod, Object[] args) throws IllegalAccessException, InvocationTargetException {
        final Object result = handlerMethod.getMethod().invoke(handlerMethod.getBeanInstance(), args);

        if (result == null && handlerMethod.isVoidReturnType()) {
            return VoidRespMsgBody.NO_DATA_WILL_BE_SENT_TO_CLIENT;
        }
        return result;
    }

}
