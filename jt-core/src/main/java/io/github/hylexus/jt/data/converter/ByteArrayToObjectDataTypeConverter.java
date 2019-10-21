package io.github.hylexus.jt.data.converter;

import io.github.hylexus.jt.exception.JtDataTypeConvertException;

/**
 * @author hylexus
 * Created At 2019-10-22 12:16 上午
 */
public interface ByteArrayToObjectDataTypeConverter<T> extends DataTypeConverter<byte[], T> {

    T convert(Class<byte[]> sourceType, Class<T> targetType, byte[] sourceInstance, int startIndex, int length);

    @Override
    default T convert(Class<byte[]> sourceType, Class<T> targetType, byte[] sourceInstance) {
        if (sourceInstance == null) {
            throw new JtDataTypeConvertException("Can not convert " + sourceType + " to " + targetType + ", [sourceInstance] is null");
        }
        return convert(sourceType, targetType, sourceInstance, 0, sourceInstance.length);
    }
}
