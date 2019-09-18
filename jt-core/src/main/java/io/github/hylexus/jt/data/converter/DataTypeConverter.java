package io.github.hylexus.jt.data.converter;

/**
 * @author hylexus
 * Created At 2019-09-18 10:02 下午
 */
@FunctionalInterface
public interface DataTypeConverter<T> {

    T convert(byte[] bytes);

    class NoOpsConverter implements DataTypeConverter {
        @Override
        public Object convert(byte[] bytes) {
            return null;
        }
    }

}
