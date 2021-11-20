package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.req.BuiltinAuthRequestMsgV2011;

import java.util.Optional;
import java.util.Set;

/**
 * @author hylexus
 * createdAt 2019/2/1
 * @see BuiltinAuthRequestMsgV2011
 **/
@BuiltinComponent
public class BuiltinAuthRequestMsgV2011BodyConverter extends AbstractBuiltinRequestMsgBodyConverter<BuiltinAuthRequestMsgV2011> {

    @Override
    public Set<Jt808ProtocolVersion> getSupportedProtocolVersion() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2011();
    }

    @Override
    public Optional<BuiltinAuthRequestMsgV2011> convert2Entity(RequestMsgMetadata metadata) {
        final byte[] bytes = metadata.getBodyBytes();
        final BuiltinAuthRequestMsgV2011 body = new BuiltinAuthRequestMsgV2011().setAuthCode(ProtocolUtils.bytes2String(bytes, 0, bytes.length));
        body.setHeader(metadata.getHeader());
        return Optional.of(body);
    }

}
