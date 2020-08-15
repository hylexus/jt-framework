package io.github.hylexus.jt.data.converter;

import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-10-19 9:59 下午
 */
public interface DataTypeConverter<S, T> {

    Set<ConvertibleMetadata> getConvertibleTypes();

    /**
     * source ==> target
     *
     * @param sourceType     source type
     * @param targetType     target type
     * @param sourceInstance source instance
     * @return target type
     */
    T convert(Class<S> sourceType, Class<T> targetType, S sourceInstance);

    default T convert(@Nullable ConvertibleMetadata key, JavaBeanFieldMetadata targetFieldMetadata, S sourceInstance, @Nullable T targetInstance,
                      @Nullable MsgDataType itemMsgDataType) {
        if (key == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Class<S> sourceClass = (Class<S>) key.getSourceClass();
        @SuppressWarnings("unchecked")
        Class<T> targetClass = (Class<T>) key.getTargetClass();
        return convert(sourceClass, targetClass, sourceInstance);
    }
}
