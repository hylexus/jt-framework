package io.github.hylexus.jt.jt808.support.data.deserialize.extension;

import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2011;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2013;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2019;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.deserialize.impl.LongFieldDeserializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808AnnotationArgumentResolveException;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @see RequestFieldAlias.GeoPoint
 * @see BuiltinMsg0200V2011#getLat()
 * @see BuiltinMsg0200V2011#getLng()
 * @see BuiltinMsg0200V2013#getLat()
 * @see BuiltinMsg0200V2013#getLng()
 * @see BuiltinMsg0200V2019#getLat()
 * @see BuiltinMsg0200V2019#getLng()
 */
public class ExtendedJt808FieldDeserializerGeoPoint extends AbstractExtendedJt808FieldDeserializer<Object> {
    private final LongFieldDeserializer delegate = new LongFieldDeserializer();

    @Override
    public Object deserialize(ByteBuf byteBuf, MsgDataType msgDataType, int start, int length, Context context) {
        final Long dword = this.delegate.deserialize(byteBuf, msgDataType, start, length);
        final Class<?> targetClass = context.fieldMetadata().getFieldType();

        if (Long.class.isAssignableFrom(targetClass) || long.class.isAssignableFrom(targetClass)) {
            return dword;
        } else if (Double.class.isAssignableFrom(targetClass) || double.class.isAssignableFrom(targetClass)) {
            return dword * 1.0 / 1_000_000;
        } else if (BigDecimal.class.isAssignableFrom(targetClass)) {
            return new BigDecimal(String.valueOf(dword)).setScale(6, RoundingMode.UP).divide(new BigDecimal("1000000"), RoundingMode.UP);
        }
        throw new Jt808AnnotationArgumentResolveException("Cannot convert DataType from " + msgDataType + " to " + targetClass);
    }

}
