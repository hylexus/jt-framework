package io.github.hylexus.jt.data.converter;

import io.github.hylexus.jt.data.MsgDataType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hylexus
 * Created At 2019-10-21 11:34 下午
 */
@Data
@EqualsAndHashCode(of = {"sourceType", "sourceDataType", "targetType"})
public class ConvertibleMetadata {

    private Class<?> sourceType;
    private MsgDataType sourceDataType;
    private Class<?> targetType;

    public ConvertibleMetadata(Class<?> sourceType, Class<?> targetType) {
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    public static ConvertibleMetadata forJt808MsgDataType(MsgDataType sourceDataType, Class<?> targetType) {
        ConvertibleMetadata instance = new ConvertibleMetadata(byte[].class, targetType);
        instance.setSourceDataType(sourceDataType);
        return instance;
    }
}
