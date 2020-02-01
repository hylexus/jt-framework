package io.github.hylexus.jt808.handler.impl.reflection;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.session.Session;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hylexus
 * Created At 2020-02-01 5:58 下午
 */
@Slf4j
@BuiltinComponent
public class ReflectionBasedRequestMsgHandler extends AbstractMsgHandler {

    private Set<MsgType> supportedMsgTypes = new HashSet<>();
    private ConcurrentMap<MsgType, HandlerMethod> mapping = new ConcurrentHashMap<>();

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

        log.info("REQ:{}", msg);
        final Object[] args = this.resolveArgs(handlerMethod, metadata, msg, session);

        final Object result;
        try {
            result = invokeHandlerMethod(handlerMethod, args);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
        return Optional.of((RespMsgBody) result);
    }

    private Object[] resolveArgs(HandlerMethod handlerMethod, RequestMsgMetadata metadata, RequestMsgBody msg, Session session) {
        final Object[] args = new Object[handlerMethod.getParameters().length];
        for (int i = 0; i < handlerMethod.getParameters().length; i++) {
            final MethodParameter parameter = handlerMethod.getParameters()[i];
            final Class<?> parameterType = parameter.getParameterType();

            // TODO ArgumentResolver
            if (RequestMsgMetadata.class.isAssignableFrom(parameterType)) {
                args[i] = metadata;
            } else if (RequestMsgHeader.class.isAssignableFrom(parameterType)) {
                args[i] = metadata.getHeader();
            } else if (RequestMsgBody.class.isAssignableFrom(parameterType)) {
                args[i] = msg;
            } else if (Session.class.isAssignableFrom(parameterType)) {
                args[i] = session;
            }
        }
        return args;
    }

    private Object invokeHandlerMethod(HandlerMethod handlerMethod, Object[] args) throws IllegalAccessException, InvocationTargetException {
        return handlerMethod.getMethod().invoke(handlerMethod.getBeanInstance(), args);
    }

}
