package io.github.hylexus.jt.annotation.msg.req.basic;

import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.converter.req.entity.ReqMsgFieldConverter;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2019-09-18 8:48 下午
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BasicField {

    int startIndex() default 0;

    MsgDataType dataType();

    /**
     * 1. {@link #dataType()}
     * 2. {@code length()}
     * 3. {@link #byteCountMethod()}
     *
     * @return 该字段的字节数
     * @see MsgDataType#getByteCount()
     */
    int length() default 0;

    /**
     * 1. {@link #dataType()}
     * 2. {@link #length()}
     * 3. {@code byteCountMethod()}
     *
     * @return 表示字节数长度的字段, 必须为int或Integer
     */
    String byteCountMethod() default "";

    Class<? extends ReqMsgFieldConverter> customerDataTypeConverterClass() default ReqMsgFieldConverter.NoOpsConverter.class;

}
