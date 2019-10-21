package io.github.hylexus.jt.data.converter;

import io.github.hylexus.oaks.utils.IntBitOps;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-10-21 11:31 下午
 */
public class ByteArrayToIntegerDataTypeConverter implements ByteArrayToNumberDataTypeConverter<Integer> {

    private static final Set<ConvertibleMetadata> CONVERTIBLE_METADATA_SET;

    static {
        Set<ConvertibleMetadata> set = new HashSet<>(2);

        set.add(new ConvertibleMetadata(byte[].class, int.class));
        set.add(new ConvertibleMetadata(byte[].class, Integer.class));

        CONVERTIBLE_METADATA_SET = Collections.unmodifiableSet(set);
    }


    @Override
    public Set<ConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    @Override
    public Integer convert(Class<byte[]> sourceType, Class<Integer> targetType, byte[] bytes, int startIndex, int length) {
        return IntBitOps.intFromBytes(bytes, startIndex, length);
    }
}
