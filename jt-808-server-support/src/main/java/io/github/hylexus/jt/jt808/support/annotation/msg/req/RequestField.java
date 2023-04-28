package io.github.hylexus.jt.jt808.support.annotation.msg.req;

import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestField {

    /**
     * @return 赋值顺序，值越小优先级越高(不要求连续，只比较大小)
     * @see <a href="https://stackoverflow.com/questions/5001172/java-reflection-getting-fields-and-methods-in-declaration-order">java-reflection-getting-fields-and-methods-in-declaration-order</a>
     */
    int order();

    /**
     * 字段偏移量获取顺序：
     * <ol>
     *     <li>{@code startIndex}</li>
     *     <li>{@link #startIndexExpression()}</li>
     *     <li>{@link #startIndexMethod()}</li>
     * </ol>
     */
    int startIndex() default -1;

    /**
     * 以 SpEL(Spring Expression Language) 语法指定字段偏移量
     *
     * @since 2.0.0
     */
    String startIndexExpression() default "";

    String startIndexMethod() default "";

    /**
     * 字段长度获取顺序：
     * <ol>
     *     <li>{@link MsgDataType#getByteCount() dataType() }</li>
     *     <li>{@code length()}</li>
     *     <li>{@link #lengthExpression()}</li>
     *     <li>{@link #lengthMethod()}</li>
     * </ol>
     *
     * @return 该字段的字节数
     * @see MsgDataType#getByteCount()
     */
    int length() default -1;

    /**
     * 以 SpEL(Spring Expression Language) 语法指定字段长度
     *
     * @since 2.0.0
     */
    String lengthExpression() default "";

    /**
     * @return 表示字段长度的方法的名称(返回类型必须为int或Integer)
     */
    String lengthMethod() default "";

    MsgDataType dataType();

    Class<? extends Jt808FieldDeserializer<?>> customerFieldDeserializerClass() default Jt808FieldDeserializer.PlaceholderFieldDeserializer.class;

    String desc() default "";
}
