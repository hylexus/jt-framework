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
public class ConvertibleMetadata<S, T> {

    private final S sourceDataType;
    private final T targetClass;

    public ConvertibleMetadata(S sourceDataType, T targetClass) {
        this.sourceDataType = sourceDataType;
        this.targetClass = targetClass;
    }

    public static RequestMsgConvertibleMetadata forJt808RequestMsgDataType(MsgDataType sourceDataType, Class<?> targetType) {
        return new RequestMsgConvertibleMetadata(sourceDataType, targetType);
    }

    public static ResponseMsgConvertibleMetadata forJt808ResponseMsgDataType(Class<?> cls, MsgDataType targetType) {
        return new ResponseMsgConvertibleMetadata(cls, targetType);
    }
}
