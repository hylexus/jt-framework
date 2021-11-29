package io.github.hylexus.jt.jt808.support.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hylexus
 */
@Data
@ToString(of = {"sourceDataType", "targetClass"})
@EqualsAndHashCode(of = {"sourceDataType", "targetClass"})
public class ConvertibleMetadata {

    private final MsgDataType sourceDataType;
    private final Class<?> targetClass;

    public ConvertibleMetadata(MsgDataType sourceDataType, Class<?> targetClass) {
        this.sourceDataType = sourceDataType;
        this.targetClass = targetClass;
    }

    public static ConvertibleMetadata forJt808MsgDataType(MsgDataType sourceDataType, Class<?> targetType) {
        return new ConvertibleMetadata(sourceDataType, targetType);
    }
}
