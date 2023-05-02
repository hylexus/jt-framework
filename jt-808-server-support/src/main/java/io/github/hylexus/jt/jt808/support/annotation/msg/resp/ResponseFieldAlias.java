package io.github.hylexus.jt.jt808.support.annotation.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.Padding;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;
import io.github.hylexus.jt.jt808.support.data.serializer.extension.ExtendedJt808FieldSerializerBcdTime;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author hylexus
 * @since 2.1.1
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseFieldAlias {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ResponseField(dataType = MsgDataType.WORD, order = -1)
    @interface Word {
        @AliasFor(annotation = ResponseField.class, attribute = "order")
        int order();

        @AliasFor(annotation = ResponseField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = ResponseField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ResponseField(dataType = MsgDataType.DWORD, order = -1)
    @interface Dword {
        @AliasFor(annotation = ResponseField.class, attribute = "order")
        int order();

        @AliasFor(annotation = ResponseField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = ResponseField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ResponseField(dataType = MsgDataType.BCD, order = -1)
    @interface Bcd {
        @AliasFor(annotation = ResponseField.class, attribute = "order")
        int order();

        @AliasFor(annotation = ResponseField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = ResponseField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ResponseField(dataType = MsgDataType.BCD, order = -1, customerFieldSerializerClass = ExtendedJt808FieldSerializerBcdTime.class)
    @interface BcdDateTime {
        @AliasFor(annotation = ResponseField.class, attribute = "order")
        int order();

        @AliasFor(annotation = ResponseField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = ResponseField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ResponseField(dataType = MsgDataType.LIST, order = -1)
    @interface List {
        @AliasFor(annotation = ResponseField.class, attribute = "order")
        int order();

        @AliasFor(annotation = ResponseField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = ResponseField.class, attribute = "customerFieldSerializerClass")
        Class<? extends Jt808FieldSerializer<?>> customerFieldSerializerClass() default Jt808FieldSerializer.PlaceholderFiledSerializer.class;

        @AliasFor(annotation = ResponseField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ResponseField(dataType = MsgDataType.BYTE, order = -1)
    @interface Byte {
        @AliasFor(annotation = ResponseField.class, attribute = "order")
        int order();

        @AliasFor(annotation = ResponseField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = ResponseField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ResponseField(dataType = MsgDataType.BYTES, order = -1)
    @interface Bytes {
        @AliasFor(annotation = ResponseField.class, attribute = "order")
        int order();

        @AliasFor(annotation = ResponseField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = ResponseField.class, attribute = "desc")
        java.lang.String desc() default "";

        /**
         * com.google.common.base.Strings#padEnd(java.lang.String, int, char)
         */
        @AliasFor(annotation = ResponseField.class, attribute = "paddingRight")
        Padding paddingRight() default @Padding(minLength = 0);

        /**
         * com.google.common.base.Strings#padStart(java.lang.String, int, char)
         */
        @AliasFor(annotation = ResponseField.class, attribute = "paddingLeft")
        Padding paddingLeft() default @Padding(minLength = 0);

        @AliasFor(annotation = ResponseField.class, attribute = "charset")
        java.lang.String charset() default "GBK";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ResponseField(dataType = MsgDataType.STRING, order = -1)
    @interface String {
        @AliasFor(annotation = ResponseField.class, attribute = "order")
        int order();

        @AliasFor(annotation = ResponseField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = ResponseField.class, attribute = "desc")
        java.lang.String desc() default "";

        @AliasFor(annotation = ResponseField.class, attribute = "paddingRight")
        Padding paddingRight() default @Padding(minLength = 0);

        /**
         * com.google.common.base.Strings#padStart(java.lang.String, int, char)
         */
        @AliasFor(annotation = ResponseField.class, attribute = "paddingLeft")
        Padding paddingLeft() default @Padding(minLength = 0);

        @AliasFor(annotation = ResponseField.class, attribute = "charset")
        java.lang.String charset() default "GBK";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ResponseField(dataType = MsgDataType.OBJECT, order = -1)
    @interface Object {
        @AliasFor(annotation = ResponseField.class, attribute = "order")
        int order();

        @AliasFor(annotation = ResponseField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = ResponseField.class, attribute = "customerFieldSerializerClass")
        Class<? extends Jt808FieldSerializer<?>> customerFieldSerializerClass() default Jt808FieldSerializer.PlaceholderFiledSerializer.class;

        @AliasFor(annotation = ResponseField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

}
