package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.codec.decode.FieldDecoder;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgHeaderAware;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgMetadataAware;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

/**
 * createdAt 2019/1/28
 *
 * @author hylexus
 **/
@Slf4j
public class Decoder {

    private final FieldDecoder fieldDecoder = new FieldDecoder();
    private final RequestMsgMetadataDecoder requestMsgMetadataDecoder;

    public Decoder(RequestMsgMetadataDecoder requestMsgMetadataDecoder) {
        this.requestMsgMetadataDecoder = requestMsgMetadataDecoder;
    }

    public RequestMsgMetadata parseMsgMetadata(Jt808ProtocolVersion version, byte[] bytes) {
        return requestMsgMetadataDecoder.parseMsgMetadata(version, bytes);
    }

    public <T> T decodeRequestMsgBody(Class<T> cls, byte[] bytes, RequestMsgMetadata metadata)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {

        T instance = cls.newInstance();

        processAwareMethod(cls, instance, metadata);

        fieldDecoder.decode(instance, bytes);

        return instance;
    }

    private <T> void processAwareMethod(Class<T> cls, Object instance, RequestMsgMetadata metadata) {
        if (instance instanceof RequestMsgHeaderAware) {
            ((RequestMsgHeaderAware) instance).setRequestMsgHeader(metadata.getHeader());
        }

        if (instance instanceof RequestMsgMetadataAware) {
            ((RequestMsgMetadataAware) instance).setRequestMsgMetadata(metadata);
        }
    }

}
