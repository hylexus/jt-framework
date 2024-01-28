package io.github.hylexus.jt.jt808.support.data.deserialize.extension;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanFieldMetadata;
import io.github.hylexus.jt.jt808.support.utils.JavaBeanMetadataUtils;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.oaks.utils.BcdOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Consumer;

class ExtendedJt808FieldDeserializerBcdTimeTest {

    @Data
    static class Placeholder {
        @RequestFieldAlias.BcdDateTime(order = 10)
        private LocalDateTime localDateTime;

        @RequestFieldAlias.BcdDateTime(order = 20)
        private Date date;

        @RequestFieldAlias.BcdDateTime(order = 30)
        private String dateString;
    }

    @Test
    void test() {
        final ExtendedJt808FieldDeserializerBcdTime deserializer = new ExtendedJt808FieldDeserializerBcdTime();

        doWithBcd("230707202633", byteBuf -> {
            final LocalDateTime result = (LocalDateTime) deserializer.deserialize(byteBuf, MsgDataType.BCD, 0, 6, createContext("localDateTime"));
            Assertions.assertEquals(2023, result.getYear());
            Assertions.assertEquals(7, result.getMonthValue());
            Assertions.assertEquals(7, result.getDayOfMonth());
            Assertions.assertEquals(20, result.getHour());
            Assertions.assertEquals(26, result.getMinute());
            Assertions.assertEquals(33, result.getSecond());
        });

        doWithBcd("230707202633", byteBuf -> {
            final Date date = (Date) deserializer.deserialize(byteBuf, MsgDataType.BCD, 0, 6, createContext("date"));
            Assertions.assertEquals("2023-07-07 20:26:33", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        });

        doWithBcd("230707202633", byteBuf -> {
            final String dateString = (String) deserializer.deserialize(byteBuf, MsgDataType.BCD, 0, 6, createContext("dateString"));
            Assertions.assertEquals("230707202633", dateString);
        });
    }

    void doWithBcd(String hex, Consumer<ByteBuf> consumer) {
        ByteBuf byteBuf = null;
        try {
            byteBuf = ByteBufAllocator.DEFAULT.buffer().writeBytes(BcdOps.bcdString2bytes(hex));
            consumer.accept(byteBuf);
        } finally {
            if (byteBuf != null) {
                JtProtocolUtils.release(byteBuf);
            }
        }
    }

    private static Jt808FieldDeserializer.Context createContext(String field) {
        final JavaBeanFieldMetadata fieldMetadata = JavaBeanMetadataUtils.getBeanMetadata(Placeholder.class).getFieldMapping().get(field);
        return new Jt808FieldDeserializer.DefaultInternalDecoderContext(fieldMetadata, null, null);
    }

}
