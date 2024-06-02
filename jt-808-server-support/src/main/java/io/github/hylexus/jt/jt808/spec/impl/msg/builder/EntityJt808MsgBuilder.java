package io.github.hylexus.jt.jt808.spec.impl.msg.builder;

import io.github.hylexus.jt.annotation.UnstableApi;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.impl.response.DefaultJt808Response;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.data.serializer.DefaultJt808FieldSerializerRegistry;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializerRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Nonnull;
import java.util.Objects;

import static io.github.hylexus.jt.utils.Assertions.assertThat;
import static io.github.hylexus.jt.utils.Assertions.requireNonNull;

/**
 * @author hylexus
 */
@UnstableApi
public class EntityJt808MsgBuilder extends AbstractJt808MsgBuilder<Object, EntityJt808MsgBuilder> {

    private final Jt808AnnotationBasedEncoder annotationBasedEncoder;

    public EntityJt808MsgBuilder(Jt808FlowIdGenerator flowIdGenerator, Jt808FieldSerializerRegistry registry) {
        this(new Jt808AnnotationBasedEncoder(registry), flowIdGenerator);
    }

    public EntityJt808MsgBuilder(Jt808FlowIdGenerator flowIdGenerator, Jt808MsgEncoder encoder) {
        super(flowIdGenerator, encoder);
        this.annotationBasedEncoder = new Jt808AnnotationBasedEncoder(new DefaultJt808FieldSerializerRegistry(true));
    }

    public EntityJt808MsgBuilder(Jt808FlowIdGenerator flowIdGenerator) {
        this(new Jt808AnnotationBasedEncoder(new DefaultJt808FieldSerializerRegistry(true)), flowIdGenerator);
    }

    public EntityJt808MsgBuilder(Jt808AnnotationBasedEncoder annotationBasedEncoder, Jt808FlowIdGenerator flowIdGenerator) {
        super(flowIdGenerator);
        this.annotationBasedEncoder = annotationBasedEncoder;
    }

    public EntityJt808MsgBuilder(Jt808FlowIdGenerator flowIdGenerator, ByteBufAllocator allocator) {
        this(new Jt808AnnotationBasedEncoder(new DefaultJt808FieldSerializerRegistry(true)), flowIdGenerator, allocator);
    }

    public EntityJt808MsgBuilder(Jt808AnnotationBasedEncoder annotationBasedEncoder, Jt808FlowIdGenerator flowIdGenerator, ByteBufAllocator allocator) {
        super(flowIdGenerator, allocator);
        this.annotationBasedEncoder = annotationBasedEncoder;
    }

    public EntityJt808MsgBuilder(
            Jt808FlowIdGenerator flowIdGenerator,
            ByteBufAllocator allocator,
            Jt808MsgBytesProcessor msgBytesProcessor) {
        this(new Jt808AnnotationBasedEncoder(new DefaultJt808FieldSerializerRegistry(true)), flowIdGenerator, allocator, msgBytesProcessor);
    }

    public EntityJt808MsgBuilder(
            Jt808AnnotationBasedEncoder annotationBasedEncoder,
            Jt808FlowIdGenerator flowIdGenerator,
            ByteBufAllocator allocator,
            Jt808MsgBytesProcessor msgBytesProcessor) {
        super(flowIdGenerator, allocator, msgBytesProcessor);
        this.annotationBasedEncoder = annotationBasedEncoder;
    }

    @Override
    protected Jt808Response toJt808Response() {
        final Class<?> entityClass = this.body.getClass();
        final Jt808ResponseBody annotation = getJt808ResponseBodyAnnotation(entityClass);

        final byte reversedBit15InHeader = this.reversedBit15InHeader == null ? annotation.reversedBit15InHeader() : this.reversedBit15InHeader;
        final int maxPackageSize = this.maxPackageSize == null ? annotation.maxPackageSize() : this.maxPackageSize;
        final int msgId = this.msgId == null ? annotation.msgId() : this.msgId;

        int encryptionType;
        if (this.encryptionType == 0) {
            encryptionType = annotation.encryptionType();
        } else {
            encryptionType = this.encryptionType;
        }

        final ByteBuf respBody = this.annotationBasedEncoder.encodeMsgBody(null, body, null);
        return new DefaultJt808Response(
                requireNonNull(version, "version() can not be null"),
                assertThat(msgId, id -> id > 0, "msgId() >= 0"),
                encryptionType,
                maxPackageSize,
                reversedBit15InHeader,
                requireNonNull(terminalId, "terminalId() can not be null"),
                -1,
                requireNonNull(respBody, "body() can not be null")
        );
    }

    @Nonnull
    private Jt808ResponseBody getJt808ResponseBodyAnnotation(Class<?> entityClass) {
        return Objects.requireNonNull(
                AnnotationUtils.findAnnotation(entityClass, Jt808ResponseBody.class),
                "[" + entityClass.getSimpleName() + "] should be marked by @" + Jt808ResponseBody.class.getSimpleName()
        );
    }

    @Override
    public ByteBuf build() {
        Jt808Response jt808Response = null;
        try {
            jt808Response = this.toJt808Response();
            return this.encoder.encode(jt808Response, this.flowIdGenerator);
        } catch (Exception e) {
            if (jt808Response != null) {
                jt808Response.release();
            }
            throw e;
        }
    }

    @Override
    public EntityJt808MsgBuilder release() {
        // do nothing
        return this;
    }
}
