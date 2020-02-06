package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.req.BuiltinAuthRequestMsgBody;

import java.util.Optional;

/**
 * @author hylexus
 * createdAt 2019/2/1
 * @see BuiltinAuthRequestMsgBody
 **/
@BuiltinComponent
public class BuiltinAuthRequestMsgBodyConverter extends AbstractBuiltinRequestMsgBodyConverter<BuiltinAuthRequestMsgBody> {

    @Override
    public Optional<BuiltinAuthRequestMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        byte[] bytes = metadata.getBodyBytes();
        BuiltinAuthRequestMsgBody body = new BuiltinAuthRequestMsgBody().setAuthCode(ProtocolUtils.bytes2String(bytes, 0, bytes.length));
        body.setHeader(metadata.getHeader());
        return Optional.of(body);
    }
}
