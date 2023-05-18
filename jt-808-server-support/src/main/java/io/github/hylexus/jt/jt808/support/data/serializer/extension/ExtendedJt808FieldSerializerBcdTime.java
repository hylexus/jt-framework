package io.github.hylexus.jt.jt808.support.data.serializer.extension;

import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.serializer.impl.StringFieldSerializer;
import io.github.hylexus.jt.jt808.support.exception.Jt808FieldSerializerException;
import io.netty.buffer.ByteBuf;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.DEFAULT_DATE_TIME_FORMAT;

public class ExtendedJt808FieldSerializerBcdTime extends AbstractExtendedJt808FieldSerializer {
    private final StringFieldSerializer delegate = new StringFieldSerializer();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

    @Override
    public void serialize(Object object, MsgDataType msgDataType, ByteBuf byteBuf, Context context) throws Jt808FieldSerializerException {
        final String bcd;

        if (object instanceof LocalDateTime) {
            bcd = ((LocalDateTime) object).format(dateTimeFormatter);
        } else if (object instanceof Date) {
            bcd = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT).format(object);
        } else if (object instanceof String) {
            bcd = (String) object;
        } else {
            throw new Jt808FieldSerializerException("Can not serializer field: " + context.fieldMetadata().getField());
        }

        this.delegate.serialize(bcd, MsgDataType.BCD, byteBuf, context);
    }
}
