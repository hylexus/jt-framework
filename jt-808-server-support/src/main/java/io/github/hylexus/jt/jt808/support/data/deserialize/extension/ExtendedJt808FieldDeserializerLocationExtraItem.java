package io.github.hylexus.jt.jt808.support.data.deserialize.extension;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2019AliasV2;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.extensions.KeyValueMapping;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.extensions.ValueDescriptor;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.utils.ReflectionUtils;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @see RequestFieldAlias.LocationMsgExtraItemMapping
 * @see BuiltinMsg0200V2019AliasV2#getExtraItemMap()
 */
public class ExtendedJt808FieldDeserializerLocationExtraItem extends AbstractExtendedJt808FieldDeserializer<Object> {

    private final Map<Field, RequestMsgConvertibleMetadata> defaultMapping = new HashMap<>();
    private final Map<Field, Map<Integer, RequestMsgConvertibleMetadata>> keyValueMappingCache = new HashMap<>();

    @Override
    public Object deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length, Context context) {
        final Jt808AnnotationBasedDecoder annotationBasedDecoder = context.delegate();
        if (annotationBasedDecoder == null) {
            throw new JtIllegalStateException("Jt808AnnotationBasedDecoder is null");
        }

        if (length > 0) {
            byteBuf = byteBuf.readSlice(length);
        }

        final Map<Integer, Object> result = new LinkedHashMap<>();
        while (byteBuf.isReadable()) {
            final int key = byteBuf.readUnsignedByte();
            final int valueLength = byteBuf.readUnsignedByte();
            final ByteBuf valueContent = byteBuf.readBytes(valueLength);
            try {
                final RequestMsgConvertibleMetadata convertibleMetadata = getValueType(context, key);
                final Optional<Jt808FieldDeserializer<?>> basicConverter = annotationBasedDecoder.basicFieldDeserializerRegistry().getConverter(convertibleMetadata);

                final Object decodedValue;
                if (basicConverter.isPresent()) {
                    decodedValue = basicConverter.get().deserialize(valueContent, convertibleMetadata.getSourceDataType(), 0, valueLength);
                } else {
                    decodedValue = annotationBasedDecoder.decode(convertibleMetadata.getTargetClass(), ReflectionUtils.createInstance(convertibleMetadata.getTargetClass()), valueContent, context.request());
                }
                result.put(key, decodedValue);
            } finally {
                JtCommonUtils.release(valueContent);
            }
        }

        return result;
    }

    private RequestMsgConvertibleMetadata getValueType(Context context, int key) {
        final Field field = context.fieldMetadata().getField();
        Map<Integer, RequestMsgConvertibleMetadata> map = keyValueMappingCache.get(field);
        if (map == null) {
            this.initMetadata(context, field);
            map = keyValueMappingCache.get(field);
        }
        final RequestMsgConvertibleMetadata metadata = map.get(key);
        if (metadata == null) {
            return Optional.ofNullable(defaultMapping.get(field))
                    .orElseGet(() -> RequestMsgConvertibleMetadata.forJt808RequestMsgDataType(MsgDataType.BYTES, byte[].class));
        }
        return metadata;
    }

    private void initMetadata(Context context, Field currentField) {
        final RequestFieldAlias.LocationMsgExtraItemMapping annotation = (RequestFieldAlias.LocationMsgExtraItemMapping) context.fieldMetadata().getAnnotationCache().get(RequestFieldAlias.LocationMsgExtraItemMapping.class);
        final KeyValueMapping[] valueTypeMappings = annotation.keyValueMappings();
        for (final KeyValueMapping valueTypeMapping : valueTypeMappings) {
            final int key = valueTypeMapping.key();
            final ValueDescriptor valueDescriptor = valueTypeMapping.value();
            final Map<Integer, RequestMsgConvertibleMetadata> cache = keyValueMappingCache.computeIfAbsent(currentField, field -> new HashMap<>());
            cache.put(key, RequestMsgConvertibleMetadata.forJt808RequestMsgDataType(valueDescriptor.source(), valueDescriptor.target()));
        }

        defaultMapping.put(currentField, RequestMsgConvertibleMetadata.forJt808RequestMsgDataType(annotation.defaultKeyValueMapping().source(), annotation.defaultKeyValueMapping().target()));
    }

}
