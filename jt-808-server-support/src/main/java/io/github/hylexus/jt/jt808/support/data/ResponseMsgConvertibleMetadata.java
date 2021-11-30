package io.github.hylexus.jt.jt808.support.data;

/**
 * @author hylexus
 */
public class ResponseMsgConvertibleMetadata extends ConvertibleMetadata<Class<?>, MsgDataType> {

    public ResponseMsgConvertibleMetadata(Class<?> sourceDataType, MsgDataType targetClass) {
        super(sourceDataType, targetClass);
    }
}
