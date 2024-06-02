package io.github.hylexus.jt.jt808.spec.builtin.msg;

import io.github.hylexus.jt.jt808.spec.Jt808MsgBuilder;
import io.github.hylexus.jt.jt808.spec.Jt808MsgEncryptionHandler;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ProtocolVersionDetector;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ProtocolVersionDetectorRegistry;
import io.github.hylexus.jt.jt808.spec.impl.msg.builder.EntityJt808MsgBuilder;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.data.deserialize.DefaultJt808FieldDeserializerRegistry;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.function.Consumer;

public class BaseReqRespMsgTest {
    protected String terminalId2013 = "013912344323";
    protected String terminalId2019 = "00000000013912344329";
    protected final Jt808MsgDecoder jt808MsgDecoder = new DefaultJt808MsgDecoder(
            new BuiltinJt808MsgTypeParser(),
            new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT),
            new DefaultJt808ProtocolVersionDetectorRegistry(new DefaultJt808ProtocolVersionDetector()),
            Jt808MsgEncryptionHandler.NO_OPS
    );

    protected final Jt808AnnotationBasedDecoder annotationBasedDecoder = new Jt808AnnotationBasedDecoder(new DefaultJt808FieldDeserializerRegistry(true));


    protected <T> T decode(String hex, Class<T> cls) {
        return this.decodeWithConsumer(hex, cls, msg -> {
        });
    }

    protected <T> T decodeWithConsumer(String hex, Class<T> cls, Consumer<T> msgConsumer) {
        ByteBuf byteBuf = null;
        Jt808Request request = null;
        try {
            byteBuf = ByteBufAllocator.DEFAULT.buffer().writeBytes(HexStringUtils.hexString2Bytes(hex));
            request = jt808MsgDecoder.decode(byteBuf.slice(1, byteBuf.readableBytes() - 1));
            final T msg = this.annotationBasedDecoder.decode(request, cls);
            msgConsumer.accept(msg);
            return msg;
        } finally {
            if (byteBuf != null) {
                byteBuf.release();
            }
            if (request != null) {
                request.release();
            }
        }
    }

    protected String encode(Object entity, Consumer<EntityJt808MsgBuilder> builder) {
        final EntityJt808MsgBuilder entityBuilder = Jt808MsgBuilder.newEntityBuilder(step -> 0);
        entityBuilder.body(entity);
        builder.accept(entityBuilder);

        ByteBuf byteBuf = null;
        try {
            byteBuf = entityBuilder.build();
            return HexStringUtils.byteBufToString(byteBuf);
        } finally {
            if (byteBuf != null) {
                byteBuf.release();
            }
        }
    }
}
