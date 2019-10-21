package io.github.hylexus.jt.data.converter;

/**
 * @author hylexus
 * Created At 2019-10-19 10:01 下午
 */
public abstract class AbstractDataTypeConverter<S, T> implements DataTypeConverter {
    private Class<S> sourceType;
    private Class<T> targetType;
}
