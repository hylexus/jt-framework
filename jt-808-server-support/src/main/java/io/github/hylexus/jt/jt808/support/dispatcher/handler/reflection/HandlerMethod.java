package io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection;

import com.google.common.collect.Lists;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.support.dispatcher.MultipleVersionSupport;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Set;

/**
 * 类似于 org.springframework.web.method.HandlerMethod
 *
 * @author hylexus
 */
@Getter
@Setter
@Accessors(chain = true)
@Slf4j
public class HandlerMethod implements MultipleVersionSupport {

    private final Object beanInstance;
    private final Method method;
    @SuppressFBWarnings("EI_EXPOSE_REP")
    private final MethodParameter[] parameters;
    private final boolean isVoidReturnType;
    private final Set<Jt808ProtocolVersion> supportedVersions;
    private final Set<MsgType> supportedMsgTypes;
    private final int order;

    public HandlerMethod(
            Object beanInstance, Method method, boolean isVoidReturnType,
            Set<Jt808ProtocolVersion> supportedVersions, Set<MsgType> supportedMsgTypes,
            int order) {
        this.beanInstance = beanInstance;
        this.method = method;
        this.parameters = this.initMethodParameters(method);
        this.isVoidReturnType = isVoidReturnType;
        this.supportedVersions = supportedVersions;
        this.supportedMsgTypes = supportedMsgTypes;
        this.order = order;
    }

    private MethodParameter[] initMethodParameters(Method method) {
        final int paramCount = method.getGenericParameterTypes().length;
        final MethodParameter[] methodParameters = new MethodParameter[paramCount];
        for (int i = 0; i < paramCount; i++) {
            final Type type = method.getGenericParameterTypes()[i];
            final MethodParameter parameter = new MethodParameter().setIndex(i).setGenericType(Lists.newArrayList()).setMethod(method);
            methodParameters[i] = parameter;
            if (type instanceof Class) {
                parameter.setParameterType((Class<?>) type);
            } else if (type instanceof ParameterizedType) {
                for (Type actualTypeArgument : ((ParameterizedType) type).getActualTypeArguments()) {
                    // ignore WildcardType
                    // ?, ? extends Number, or ? super Integer
                    if (actualTypeArgument instanceof WildcardType) {
                        continue;
                    }
                    parameter.setParameterType((Class<?>) ((ParameterizedType) type).getRawType());
                    parameter.getGenericType().add((Class<?>) actualTypeArgument);
                }
            } else {
                log.error("Unsupported type : {}", type);
            }
        }
        return methodParameters;
    }

    @Override
    public Set<Jt808ProtocolVersion> getSupportedVersions() {
        return supportedVersions;
    }

    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return supportedMsgTypes;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "HandlerMethod{"
               + "beanInstance=" + beanInstance
               + ", method=" + method
               + ", parameters=" + Arrays.toString(parameters)
               + ", isVoidReturnType=" + isVoidReturnType
               + '}';
    }
}
