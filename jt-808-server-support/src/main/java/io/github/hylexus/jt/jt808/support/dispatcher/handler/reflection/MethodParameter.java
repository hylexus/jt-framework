package io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(of = {"index", "parameterType"})
public class MethodParameter {
    private int index;
    private Class<?> parameterType;
    private List<Class<?>> genericType;
    private Method method;
}
