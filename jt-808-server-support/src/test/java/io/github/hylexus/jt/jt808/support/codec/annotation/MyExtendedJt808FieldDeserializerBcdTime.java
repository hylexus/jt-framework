package io.github.hylexus.jt.jt808.support.codec.annotation;

import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.deserialize.extension.AbstractExtendedJt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.data.deserialize.impl.BcdFieldDeserializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.netty.buffer.ByteBuf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MyExtendedJt808FieldDeserializerBcdTime extends AbstractExtendedJt808FieldDeserializer<Object> {
    private final BcdFieldDeserializer delegate = new BcdFieldDeserializer();

    @Override
    public Object deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length, Context context) {
        final Class<?> targetClass = context.fieldMetadata().getFieldType();
        final String bcd = this.delegate.deserialize(byteBuf, msgDataType, start, length);
        final BcdDateTime annotation = context.fieldMetadata().getAnnotation(BcdDateTime.class);
        final String pattern = annotation.pattern();

        if (LocalDateTime.class.isAssignableFrom(targetClass)) {
            return LocalDateTime.parse(bcd, DateTimeFormatter.ofPattern(pattern));
        } else if (Date.class.isAssignableFrom(targetClass)) {
            try {
                return new SimpleDateFormat(pattern).parse(bcd);
            } catch (ParseException e) {
                throw new Jt808FieldSerializerException(e);
            }
        } else if (String.class.isAssignableFrom(targetClass)) {
            return bcd;
        }
        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to " + targetClass);
    }
}