package io.github.hylexus.jt.jt808.support.annotation.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.Padding;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;

import java.lang.annotation.*;

/**
 * @author hylexus
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseField {

    /**
     * @return 序列化的顺序，值越小优先级越高(不要求连续，只比较大小)
     */
    int order();

    MsgDataType dataType();

    /**
     * @return SpEL(Spring Expression Language); 解析之后的结果为true时才会序列化该字段。
     */
    String conditionalOn() default "";

    /**
     * 仅仅在下面两个场景下生效:
     * <ol>
     *     <li>从 {@link java.lang.String} 到 {@link MsgDataType#STRING} </li>
     *     <li>从 {@link java.lang.String} 到 {@link MsgDataType#BYTES} </li>
     * </ol>
     *
     * @see io.github.hylexus.jt.jt808.support.data.serializer.impl.StringFieldSerializer
     * @since 2.1.1
     */
    String charset() default "GBK";

    /**
     * com.google.common.base.Strings#padEnd(java.lang.String, int, char)
     * <p>
     * 仅仅在下面两个场景下生效:
     * <ol>
     *     <li>从 {@link java.lang.String} 到 {@link MsgDataType#STRING} </li>
     *     <li>从 {@link java.lang.String} 到 {@link MsgDataType#BYTES} </li>
     * </ol>
     *
     * @see io.github.hylexus.jt.jt808.support.data.serializer.impl.StringFieldSerializer
     * @since 2.1.1
     */
    Padding paddingRight() default @Padding(minLength = 0);

    /**
     * com.google.common.base.Strings#padStart(java.lang.String, int, char)
     * <p>
     * 仅仅在下面两个场景下生效:
     * <ol>
     *     <li>从 {@link java.lang.String} 到 {@link MsgDataType#STRING} </li>
     *     <li>从 {@link java.lang.String} 到 {@link MsgDataType#BYTES} </li>
     * </ol>
     *
     * @see io.github.hylexus.jt.jt808.support.data.serializer.impl.StringFieldSerializer
     * @since 2.1.1
     */
    Padding paddingLeft() default @Padding(minLength = 0);

    Class<? extends Jt808FieldSerializer<?>> customerFieldSerializerClass() default Jt808FieldSerializer.PlaceholderFiledSerializer.class;

    String desc() default "";
}
