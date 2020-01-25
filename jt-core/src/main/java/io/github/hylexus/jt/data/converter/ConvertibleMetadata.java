package io.github.hylexus.jt.data.converter;

import io.github.hylexus.jt.data.MsgDataType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hylexus
 * Created At 2019-10-21 11:34 下午
 */
@Data
@ToString(of = {"sourceClass","targetClass","sourceDataType"})
@EqualsAndHashCode(of = {"sourceClass", "sourceDataType", "targetClass"})
public class ConvertibleMetadata {

    private Class<?> sourceClass;
    private MsgDataType sourceDataType;
    private Class<?> targetClass;

    public ConvertibleMetadata(Class<?> sourceClass, Class<?> targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    public static ConvertibleMetadata forJt808EncodeMsgDataType(Class<?> sourceType, MsgDataType targetDataType) {
        ConvertibleMetadata convertibleMetadata = new ConvertibleMetadata(sourceType, byte[].class);
        return convertibleMetadata;
    }

    public static ConvertibleMetadata forJt808MsgDataType(MsgDataType sourceDataType, Class<?> targetType) {
        ConvertibleMetadata instance = new ConvertibleMetadata(byte[].class, targetType);
        instance.setSourceDataType(sourceDataType);
        return instance;
    }
}
