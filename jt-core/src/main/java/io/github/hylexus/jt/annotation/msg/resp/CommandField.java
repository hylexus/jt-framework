package io.github.hylexus.jt.annotation.msg.resp;

import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.converter.resp.entity.RespMsgFieldConverter;

import java.lang.annotation.*;

import static io.github.hylexus.jt.data.MsgDataType.UNKNOWN;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommandField {

    /**
     * @return 字段的处理顺序，值越小优先级越高
     */
    int order();

    MsgDataType targetMsgDataType() default UNKNOWN;

    boolean isNestedCommandField() default false;

    Class<? extends RespMsgFieldConverter> customerDataTypeConverterClass() default RespMsgFieldConverter.NoOpsConverter.class;
}