package io.github.hylexus.jt.data.converter;

/**
 * @author hylexus
 * Created At 2019-10-28 9:16 下午
 */
public interface Jt808MsgDataTypeConverter<T> extends DataTypeConverter<byte[], T> {

    T convert(byte[] bytes, int start, int length);

    @Override
    @Deprecated
    default T convert(Class<byte[]> sourceType, Class<T> targetType, byte[] sourceInstance) {
        return this.convert(sourceInstance, 0, sourceInstance.length);
    }
}
