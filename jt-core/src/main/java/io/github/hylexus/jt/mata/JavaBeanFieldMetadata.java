package io.github.hylexus.jt.mata;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author hylexus
 * Created At 2019-09-28 7:21 下午
 */
@Data
@Accessors(chain = true)
public class JavaBeanFieldMetadata {
    private Field field;
    private Class<?> fieldType;
    private List<Class<?>> genericType;

    public <T extends Annotation> boolean isAnnotationPresent(Class<T> annotationClass) {
        return field.getAnnotation(annotationClass) != null;
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
    }
}
