package io.github.hylexus.jt.data.converter;

import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-10-19 9:59 下午
 */
public interface DataTypeConverter<S, T> {

    /**
     * source ==> target
     *
     * @param sourceType     source type
     * @param targetType     target type
     * @param sourceInstance source instance
     * @return target type
     */
    T convert(Class<S> sourceType, Class<T> targetType, S sourceInstance);

    Set<ConvertibleMetadata> getConvertibleTypes();
}
