package io.github.hylexus.jt.jt808.support.data;

/**
 * @author hylexus
 */
public class RequestMsgConvertibleMetadata extends ConvertibleMetadata<MsgDataType, Class<?>> {

    public RequestMsgConvertibleMetadata(MsgDataType sourceDataType, Class<?> targetClass) {
        super(sourceDataType, targetClass);
    }

}
