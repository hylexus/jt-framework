package io.github.hylexus.jt.jt808.support.annotation.msg.resp;

import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseField {

    /**
     * @return 赋值顺序，值越小优先级越高
     */
    int order();

    MsgDataType dataType();

    Class<? extends Jt808FieldSerializer<?>> customerFieldSerializerClass() default Jt808FieldSerializer.PlaceholderFiledSerializer.class;

}
