package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.req.AuthRequestMsgBody;

import java.util.Optional;

/**
 * @author hylexus
 * createdAt 2019/2/1
 * @see AuthRequestMsgBody
 **/
@BuiltinComponent
public class AuthRequestMsgBodyConverter implements RequestMsgBodyConverter<AuthRequestMsgBody> {

    @Override
    public int getOrder() {
        return BUILTIN_COMPONENT_ORDER;
    }

    @Override
    public Optional<AuthRequestMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        byte[] bytes = metadata.getBodyBytes();
        AuthRequestMsgBody body = new AuthRequestMsgBody().setAuthCode(ProtocolUtils.bytes2String(bytes, 0, bytes.length));
        body.setHeader(metadata.getHeader());
        return Optional.of(body);
    }
}