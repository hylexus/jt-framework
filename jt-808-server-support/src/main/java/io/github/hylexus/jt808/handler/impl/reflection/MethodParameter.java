package io.github.hylexus.jt808.handler.impl.reflection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author hylexus
 * Created At 2020-02-01 7:20 下午
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class MethodParameter {
    private int index;
    private Class<?> parameterType;
    private List<Class<?>> genericType;
}
