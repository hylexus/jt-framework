package io.github.hylexus.jt.jt808.support.data.deserialize.extension;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.deserialize.impl.BcdFieldDeserializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.netty.buffer.ByteBuf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.DEFAULT_DATE_TIME_FORMAT;

/**
 * @see RequestFieldAlias.BcdDateTime
 * @see io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2011#getTime()
 * @see io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2013#getTime()
 * @see io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2019#getTime()
 */
public class ExtendedJt808FieldDeserializerBcdTime extends AbstractExtendedJt808FieldDeserializer<Object> {
    private final BcdFieldDeserializer delegate = new BcdFieldDeserializer();
    private final DateTimeFormatter pattern = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

    @Override
    public Object deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length, Context context) {
        final Class<?> targetClass = context.fieldMetadata().getFieldType();
        final String bcd = this.delegate.deserialize(byteBuf, msgDataType, start, length);

        if (LocalDateTime.class.isAssignableFrom(targetClass)) {
            return LocalDateTime.parse(bcd, pattern);
        } else if (Date.class.isAssignableFrom(targetClass)) {
            try {
                return new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT).parse(bcd);
            } catch (ParseException e) {
                throw new Jt808FieldSerializerException(e);
            }
        } else if (String.class.isAssignableFrom(targetClass)) {
            return bcd;
        }
        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to " + targetClass);
    }
}
