package io.github.hylexus.jt.data.converter;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hylexus
 * Created At 2019-10-21 11:34 下午
 */
@Data
@EqualsAndHashCode(of = {"sourceType", "targetType"})
public class ConvertibleMetadata {

    private Class<?> sourceType;
    private Class<?> targetType;

    public ConvertibleMetadata(Class<?> sourceType, Class<?> targetType) {
        this.sourceType = sourceType;
        this.targetType = targetType;
    }
}
