package io.github.hylexus.jt.jt808.support.annotation.msg.req;

import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.data.deserialize.impl.StringFieldDeserializer;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanFieldMetadata;

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
     * 从 2.1.1 开始，所有内置的解码器都不再依赖这个属性。
     * <p>
     * <p>
     * 字段偏移量获取顺序：
     * <p>
     * 从 2.1.1 开始，下面三个属性都是可选的.
     * <p>
     * 从 2.1.1 开始，下面三个属性都是可选的.
     * <p>
     * 从 2.1.1 开始，下面三个属性都是可选的.
     *
     * <ol>
     *     <li>{@code startIndex}</li>
     *     <li>{@link #startIndexExpression()}</li>
     *     <li>{@link #startIndexMethod()}</li>
     * </ol>
     *
     * @see io.github.hylexus.jt.jt808.support.data.meta.RequestFieldOffsetExtractor#createFor(JavaBeanFieldMetadata, RequestField)
     */
    int startIndex() default -1;

    /**
     * 从 2.1.1 开始，所有内置的解码器都不再依赖这个属性。
     * <p>
     * 以 SpEL(Spring Expression Language) 语法指定字段偏移量
     *
     * @see io.github.hylexus.jt.jt808.support.data.meta.RequestFieldOffsetExtractor#createFor(JavaBeanFieldMetadata, RequestField)
     * @since 2.0.0
     */
    String startIndexExpression() default "";

    /**
     * 从 2.1.1 开始，所有内置的解码器都不再依赖这个属性。
     *
     * @see io.github.hylexus.jt.jt808.support.data.meta.RequestFieldOffsetExtractor#createFor(JavaBeanFieldMetadata, RequestField)
     */
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

    /**
     * 仅仅在下面两个场景下生效:
     * <ol>
     * <li>从 {@link MsgDataType#STRING} 到 {@link java.lang.String}</li>
     * <li>从 {@link MsgDataType#BYTES} 到 {@link java.lang.String}</li>
     * </ol>
     *
     * @see StringFieldDeserializer
     * @since 2.1.1
     */
    String charset() default "GBK";

    Class<? extends Jt808FieldDeserializer<?>> customerFieldDeserializerClass() default Jt808FieldDeserializer.PlaceholderFieldDeserializer.class;

    String desc() default "";

    /**
     * @return SpEL(Spring Expression Language); 解析之后的结果为true时才会反序列化该字段。
     * @since 2.1.4
     */
    String conditionalOn() default "";
}
