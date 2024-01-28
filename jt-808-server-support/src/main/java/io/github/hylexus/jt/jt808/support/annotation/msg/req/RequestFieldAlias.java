package io.github.hylexus.jt.jt808.support.annotation.msg.req;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.extensions.ValueDescriptor;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.extensions.KeyValueMapping;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.deserialize.extension.ExtendedJt808FieldDeserializerBcdTime;
import io.github.hylexus.jt.jt808.support.data.deserialize.extension.ExtendedJt808FieldDeserializerGeoPoint;
import io.github.hylexus.jt.jt808.support.data.deserialize.extension.ExtendedJt808FieldDeserializerLocationExtraItem;
import io.github.hylexus.jt.jt808.support.data.type.byteseq.ByteArrayContainer;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author hylexus
 * @since 2.1.1
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestFieldAlias {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.WORD, order = -1)
    @interface Word {
        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.DWORD, order = -1)
    @interface Dword {
        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.BCD, order = -1)
    @interface Bcd {
        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = RequestField.class, attribute = "length")
        int length() default -1;

        @AliasFor(annotation = RequestField.class, attribute = "lengthExpression")
        java.lang.String lengthExpression() default "";

        @AliasFor(annotation = RequestField.class, attribute = "lengthMethod")
        java.lang.String lengthMethod() default "";

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";

    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.BCD, length = 6, customerFieldDeserializerClass = ExtendedJt808FieldDeserializerBcdTime.class, order = -1)
    @interface BcdDateTime {
        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.LIST, order = -1)
    @interface List {
        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "length")
        int length() default -1;

        @AliasFor(annotation = RequestField.class, attribute = "lengthExpression")
        java.lang.String lengthExpression() default "";

        @AliasFor(annotation = RequestField.class, attribute = "lengthMethod")
        java.lang.String lengthMethod() default "";

        @AliasFor(annotation = RequestField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.BYTE, order = -1)
    @interface Byte {
        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.BYTES, order = -1)
    @interface Bytes {
        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = RequestField.class, attribute = "length")
        int length() default -1;

        @AliasFor(annotation = RequestField.class, attribute = "lengthExpression")
        java.lang.String lengthExpression() default "";

        @AliasFor(annotation = RequestField.class, attribute = "lengthMethod")
        java.lang.String lengthMethod() default "";

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.STRING, order = -1)
    @interface String {

        @AliasFor(annotation = RequestField.class, attribute = "charset")
        java.lang.String charset() default "GBK";

        @AliasFor(annotation = RequestField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "length")
        int length() default -1;

        @AliasFor(annotation = RequestField.class, attribute = "lengthExpression")
        java.lang.String lengthExpression() default "";

        @AliasFor(annotation = RequestField.class, attribute = "lengthMethod")
        java.lang.String lengthMethod() default "";

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.OBJECT, order = -1)
    @interface Object {
        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "length")
        int length() default -1;

        @AliasFor(annotation = RequestField.class, attribute = "lengthExpression")
        java.lang.String lengthExpression() default "";

        @AliasFor(annotation = RequestField.class, attribute = "lengthMethod")
        java.lang.String lengthMethod() default "";

        @AliasFor(annotation = RequestField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.DWORD, order = -1, customerFieldDeserializerClass = ExtendedJt808FieldDeserializerGeoPoint.class)
    @interface GeoPoint {
        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";
    }

    /**
     * 位置附加项
     *
     * @since 2.1.4
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @RequestField(dataType = MsgDataType.OBJECT, order = -1, customerFieldDeserializerClass = ExtendedJt808FieldDeserializerLocationExtraItem.class)
    @interface LocationMsgExtraItemMapping {

        @AliasFor(annotation = RequestField.class, attribute = "order")
        int order();

        @AliasFor(annotation = RequestField.class, attribute = "conditionalOn")
        java.lang.String conditionalOn() default "";

        @AliasFor(annotation = RequestField.class, attribute = "length")
        int length() default -1;

        @AliasFor(annotation = RequestField.class, attribute = "lengthExpression")
        java.lang.String lengthExpression() default "";

        @AliasFor(annotation = RequestField.class, attribute = "lengthMethod")
        java.lang.String lengthMethod() default "";

        @AliasFor(annotation = RequestField.class, attribute = "desc")
        java.lang.String desc() default "";

        KeyValueMapping[] keyValueMappings();

        ValueDescriptor defaultKeyValueMapping() default @ValueDescriptor(source = MsgDataType.BYTES, target = ByteArrayContainer.class);

    }
}
