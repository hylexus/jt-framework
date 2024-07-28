package io.github.hylexus.jt.jt808.support.utils;

import io.github.hylexus.jt.annotation.Transient;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.SlicedFrom;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanFieldMetadata;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanMetadata;
import io.github.hylexus.jt.jt808.support.data.meta.RequestFieldLengthExtractor;
import io.github.hylexus.jt.jt808.support.data.meta.RequestFieldOffsetExtractor;
import io.github.hylexus.jt.utils.Jdk8Adapter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hylexus
 */
public class JavaBeanMetadataUtils {

    /**
     * 支持按bit拆分的类型
     */
    public static final Set<Class<?>> SLICED_TYPE = Jdk8Adapter.setOf(
            Byte.class, byte.class,
            Short.class, short.class,
            Integer.class, int.class,
            Long.class, long.class
    );
    private static final ConcurrentMap<Class<?>, JavaBeanMetadata> CLASS_METADATA_CACHE = new ConcurrentHashMap<>();

    public static boolean isSlicedType(Class<?> cls) {
        return SLICED_TYPE.contains(cls);
    }

    public static JavaBeanMetadata getBeanMetadata(Class<?> cls) {
        JavaBeanMetadata javaBeanMetadata = CLASS_METADATA_CACHE.get(cls);
        if (javaBeanMetadata != null) {
            return javaBeanMetadata;
        }

        javaBeanMetadata = buildClassMetadata(cls);

        CLASS_METADATA_CACHE.put(cls, javaBeanMetadata);
        return javaBeanMetadata;
    }

    private static JavaBeanMetadata buildClassMetadata(Class<?> cls) {
        final JavaBeanMetadata javaBeanMetadata = new JavaBeanMetadata();
        javaBeanMetadata.setOriginalClass(cls);
        javaBeanMetadata.setFieldMetadataList(new ArrayList<>());
        javaBeanMetadata.setFieldMapping(new HashMap<>());
        javaBeanMetadata.setSliceFromSupportedFieldList(new ArrayList<>());
        javaBeanMetadata.setRequestFieldMetadataList(new ArrayList<>());
        javaBeanMetadata.setResponseFieldMetadataList(new ArrayList<>());

        for (Field field : cls.getDeclaredFields()) {
            final Class<?> fieldType = field.getType();

            // @see https://github.com/hylexus/jt-framework/issues/32
            if (shouldBeIgnore(field)) {
                continue;
            }

            final JavaBeanFieldMetadata javaBeanFieldMetadata = buildFieldMetadata(field, fieldType);
            javaBeanFieldMetadata.setRawBeanMetadata(javaBeanMetadata);
            if (javaBeanFieldMetadata.isAnnotationPresent(RequestField.class)) {
                final RequestField annotation = javaBeanFieldMetadata.getAnnotation(RequestField.class);
                javaBeanFieldMetadata.setOrder(annotation.order());
                javaBeanFieldMetadata.setRequestFieldOffsetExtractor(RequestFieldOffsetExtractor.createFor(javaBeanFieldMetadata, annotation));
                javaBeanFieldMetadata.setRequestFieldLengthExtractor(RequestFieldLengthExtractor.createFor(fieldType, annotation.dataType(), annotation));
                javaBeanFieldMetadata.setFieldCharset(Charset.forName(annotation.charset()));
                javaBeanMetadata.getRequestFieldMetadataList().add(javaBeanFieldMetadata);
            }

            if (javaBeanFieldMetadata.isAnnotationPresent(ResponseField.class)) {
                final ResponseField annotation = javaBeanFieldMetadata.getAnnotation(ResponseField.class);
                javaBeanFieldMetadata.setOrder(annotation.order());
                javaBeanMetadata.getResponseFieldMetadataList().add(javaBeanFieldMetadata);
                javaBeanFieldMetadata.setFieldCharset(Charset.forName(annotation.charset()));
            }

            javaBeanMetadata.getFieldMetadataList().add(javaBeanFieldMetadata);
            javaBeanMetadata.getFieldMapping().put(field.getName(), javaBeanFieldMetadata);
            if (ReflectionUtils.getAnnotation(field, SlicedFrom.class) != null) {
                javaBeanMetadata.getSliceFromSupportedFieldList().add(javaBeanFieldMetadata);
            }
        }
        javaBeanMetadata.getRequestFieldMetadataList().sort(Comparator.comparing(JavaBeanFieldMetadata::getOrder));
        javaBeanMetadata.getResponseFieldMetadataList().sort(Comparator.comparing(JavaBeanFieldMetadata::getOrder));
        javaBeanMetadata.getFieldMetadataList().sort(Comparator.comparing(JavaBeanFieldMetadata::getOrder));

        return javaBeanMetadata;
    }

    private static JavaBeanFieldMetadata buildFieldMetadata(Field field, Class<?> fieldType) {
        final JavaBeanFieldMetadata javaBeanFieldMetadata = new JavaBeanFieldMetadata(
                ReflectionUtils.getAllAnnotations(field, RequestField.class, ResponseField.class, SlicedFrom.class)
        );

        javaBeanFieldMetadata.setField(field);
        javaBeanFieldMetadata.setFieldType(fieldType);
        javaBeanFieldMetadata.setGenericType(new ArrayList<>());

        Type genericType = field.getGenericType();

        if (genericType instanceof ParameterizedType) {
            for (Type actualTypeArgument : ((ParameterizedType) genericType).getActualTypeArguments()) {
                // ignore WildcardType
                // ?, ? extends Number, or ? super Integer
                if (actualTypeArgument instanceof WildcardType) {
                    continue;
                }
                javaBeanFieldMetadata.getGenericType().add((Class<?>) actualTypeArgument);
            }
        }
        return javaBeanFieldMetadata;
    }

    private static boolean shouldBeIgnore(Field field) {
        return field.getAnnotation(Transient.class) != null
                || field.getAnnotation(java.beans.Transient.class) != null;
    }
}
