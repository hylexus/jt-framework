package io.github.hylexus.jt.jt808.support.annotation.msg.req;

import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestField {

    /**
     * @return 赋值顺序，值越小优先级越高
     */
    int order();

    int startIndex() default -1;

    String startIndexExpression() default "";

    String startIndexMethod() default "";

    /**
     * 1. {@link #dataType()}
     * 2. {@code length()}
     * 3. {@link #lengthMethod()}
     *
     * @return 该字段的字节数
     * @see MsgDataType#getByteCount()
     */
    int length() default -1;

    String lengthExpression() default "";

    /**
     * 1. {@link #dataType()}
     * 2. {@link #length()}
     * 3. {@link #lengthExpression()}
     * 4. {@code lengthMethod()}
     *
     * @return 表示字节数长度的字段, 必须为int或Integer
     */
    String lengthMethod() default "";

    MsgDataType dataType();

    Class<? extends Jt808FieldDeserializer<?>> customerFieldDeserializerClass() default Jt808FieldDeserializer.PlaceholderFieldDeserializer.class;

}
