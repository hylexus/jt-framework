package io.github.hylexus.jt.data.converter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-10-22 12:08 上午
 */
public class ByteSequenceToStringDataConverter implements ByteArrayToObjectDataTypeConverter<String> {
    private static final Set<ConvertibleMetadata> CONVERTIBLE_METADATA_SET;

    static {
        Set<ConvertibleMetadata> set = new HashSet<>();
        set.add(new ConvertibleMetadata(byte[].class, String.class));
        set.add(new ConvertibleMetadata(Byte[].class, String.class));

        CONVERTIBLE_METADATA_SET = Collections.unmodifiableSet(set);
    }

    @Override
    public String convert(Class<byte[]> sourceType, Class<String> targetType, byte[] sourceInstance, int startIndex, int length) {
        return null;
    }

    @Override
    public Set<ConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }
}
