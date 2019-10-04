package io.github.hylexus.jt.utils;

import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.jt.mata.JavaBeanMetadata;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hylexus
 * Created At 2019-09-28 8:53 下午
 */
public class JavaBeanMetadataUtils {

    private static final ConcurrentMap<Class<?>, JavaBeanMetadata> CLASS_METADATA_CACHE = new ConcurrentHashMap<>();
    /**
     * 可以从其他属性拆分而来的类型,目前只支持 int 和 boolean
     */
    private static final Set<Class<?>> SLICEABLE_TYPE;

    static {
        final Set<Class<?>> set = new HashSet<>(4);
        set.add(Integer.class);
        set.add(int.class);
        set.add(Boolean.class);
        set.add(boolean.class);
        SLICEABLE_TYPE = Collections.unmodifiableSet(set);
    }

    public static boolean isSliceableType(Class<?> cls) {
        return SLICEABLE_TYPE.contains(cls);
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
        JavaBeanMetadata javaBeanMetadata = new JavaBeanMetadata();
        javaBeanMetadata.setOriginalClass(cls);
        javaBeanMetadata.setFieldMetadataList(new ArrayList<>());
        javaBeanMetadata.setFieldMapping(new HashMap<>());
        javaBeanMetadata.setSliceableTypeList(new ArrayList<>());

        for (Field field : cls.getDeclaredFields()) {
            Class<?> fieldType = field.getType();

            JavaBeanFieldMetadata javaBeanFieldMetadata = buildFieldMetadata(field, fieldType);
            javaBeanFieldMetadata.setRawBeanMetadata(javaBeanMetadata);

            javaBeanMetadata.getFieldMetadataList().add(javaBeanFieldMetadata);
            javaBeanMetadata.getFieldMapping().put(field.getName(), javaBeanFieldMetadata);
            if (JavaBeanMetadataUtils.isSliceableType(fieldType)) {
                javaBeanMetadata.getSliceableTypeList().add(javaBeanFieldMetadata);
            }
        }
        return javaBeanMetadata;
    }

    private static JavaBeanFieldMetadata buildFieldMetadata(Field field, Class<?> fieldType) {
        JavaBeanFieldMetadata javaBeanFieldMetadata = new JavaBeanFieldMetadata();
        javaBeanFieldMetadata.setField(field);
        javaBeanFieldMetadata.setFieldType(fieldType);
        javaBeanFieldMetadata.setGenericType(new ArrayList<>());

        Type genericType = field.getGenericType();

        if (genericType instanceof ParameterizedType) {
            for (Type actualTypeArgument : ((ParameterizedType) genericType).getActualTypeArguments()) {
                javaBeanFieldMetadata.getGenericType().add((Class<?>) actualTypeArgument);
            }
        }
        return javaBeanFieldMetadata;
    }
}
