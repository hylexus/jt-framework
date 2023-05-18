package io.github.hylexus.jt.jt808.support.codec.annotation;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestField(dataType = MsgDataType.BCD, length = 6, customerFieldDeserializerClass = MyExtendedJt808FieldDeserializerBcdTime.class, order = -1)
public @interface BcdDateTime {
    @AliasFor(annotation = RequestField.class, attribute = "order")
    int order();

    @AliasFor(annotation = RequestField.class, attribute = "desc")
    java.lang.String desc() default "";

    // 也可以自己扩展注解的属性
    String pattern() default "yyMMddHHmmss";
}