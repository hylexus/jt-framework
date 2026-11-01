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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Codex
 * @author hylexus
 * @see RequestFieldAlias.LocationMsgExtraItemMapping
 * @see BuiltinMsg0200V2019AliasV2#getExtraItemMap()
 */
public class ExtendedJt808FieldDeserializerLocationExtraItem extends AbstractExtendedJt808FieldDeserializer<Object> {

    private final ConcurrentMap<Field, FieldMappingMetadata> mappingCache = new ConcurrentHashMap<>();

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
        // get 仅是快速路径；ConcurrentHashMap.computeIfAbsent 会原子地检查、初始化并安全发布完整配置。
        // 配置发布后不再修改，因此这里无需额外使用 synchronized。
        FieldMappingMetadata mapping = mappingCache.get(field);
        if (mapping == null) {
            mapping = mappingCache.computeIfAbsent(field, ignored -> this.initMetadata(context));
        }
        final RequestMsgConvertibleMetadata metadata = mapping.keyMappings.get(key);
        return metadata != null ? metadata : mapping.defaultMapping;
    }

    private FieldMappingMetadata initMetadata(Context context) {
        final RequestFieldAlias.LocationMsgExtraItemMapping annotation = (RequestFieldAlias.LocationMsgExtraItemMapping) context.fieldMetadata().getAnnotationCache().get(RequestFieldAlias.LocationMsgExtraItemMapping.class);
        final Map<Integer, RequestMsgConvertibleMetadata> keyMappings = new HashMap<>();
        final KeyValueMapping[] valueTypeMappings = annotation.keyValueMappings();
        for (final KeyValueMapping valueTypeMapping : valueTypeMappings) {
            final int key = valueTypeMapping.key();
            final ValueDescriptor valueDescriptor = valueTypeMapping.value();
            keyMappings.put(key, RequestMsgConvertibleMetadata.forJt808RequestMsgDataType(valueDescriptor.source(), valueDescriptor.target()));
        }

        final ValueDescriptor defaultDescriptor = annotation.defaultKeyValueMapping();
        final RequestMsgConvertibleMetadata defaultMapping = RequestMsgConvertibleMetadata.forJt808RequestMsgDataType(defaultDescriptor.source(), defaultDescriptor.target());
        return new FieldMappingMetadata(keyMappings, defaultMapping);
    }

    private static final class FieldMappingMetadata {
        private final Map<Integer, RequestMsgConvertibleMetadata> keyMappings;
        private final RequestMsgConvertibleMetadata defaultMapping;

        private FieldMappingMetadata(Map<Integer, RequestMsgConvertibleMetadata> keyMappings, RequestMsgConvertibleMetadata defaultMapping) {
            // Publish all key mappings and the default together; the local map is never modified afterwards.
            this.keyMappings = Collections.unmodifiableMap(keyMappings);
            this.defaultMapping = defaultMapping;
        }
    }

}
