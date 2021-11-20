package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.req.BuiltinAuthRequestMsgBody;

import java.util.Optional;
import java.util.Set;

/**
 * createdAt 2019/2/1
 *
 * @author hylexus
 * @see BuiltinAuthRequestMsgBody
 * @see BuiltinAuthRequestMsgV2011BodyConverter
 * @see BuiltinAuthRequestMsgV2019BodyConverter
 * @deprecated 使用 {@link BuiltinAuthRequestMsgV2011BodyConverter} 代替
 **/
@Deprecated
@BuiltinComponent
public class BuiltinAuthRequestMsgBodyConverter extends AbstractBuiltinRequestMsgBodyConverter<BuiltinAuthRequestMsgBody> {

    @Override
    public Set<Jt808ProtocolVersion> getSupportedProtocolVersion() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2011();
    }

    @Override
    public Optional<BuiltinAuthRequestMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        byte[] bytes = metadata.getBodyBytes();
        BuiltinAuthRequestMsgBody body = new BuiltinAuthRequestMsgBody().setAuthCode(ProtocolUtils.bytes2String(bytes, 0, bytes.length));
        body.setHeader(metadata.getHeader());
        return Optional.of(body);
    }
}
