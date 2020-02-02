package io.github.hylexus.jt808.handler.impl.reflection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author hylexus
 * Created At 2020-02-01 7:20 下午
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
