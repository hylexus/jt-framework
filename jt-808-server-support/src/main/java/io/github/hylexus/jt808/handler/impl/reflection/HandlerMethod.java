package io.github.hylexus.jt808.handler.impl.reflection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;

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
public class HandlerMethod {

    private Object beanInstance;
    private Method method;
    private MethodParameter[] parameters;

    public HandlerMethod(Object beanInstance, Method method, MethodParameter[] parameters) {
        this.beanInstance = beanInstance;
        this.method = method;
        this.parameters = parameters;
    }
}
