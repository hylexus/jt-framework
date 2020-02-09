package io.github.hylexus.jt808.handler.impl.reflection;

import com.google.common.collect.Lists;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * 类似于 org.springframework.web.method.HandlerMethod
 *
 * @author hylexus
 * Created At 2020-02-01 6:01 下午
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Slf4j
public class HandlerMethod {

    private final Object beanInstance;
    private final Method method;
    @SuppressFBWarnings("EI_EXPOSE_REP")
    private final MethodParameter[] parameters;
    private final boolean isVoidReturnType;

    public HandlerMethod(Object beanInstance, Method method, boolean isVoidReturnType) {
        this.beanInstance = beanInstance;
        this.method = method;
        this.parameters = this.initMethodParameters(method);
        this.isVoidReturnType = isVoidReturnType;
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
}
