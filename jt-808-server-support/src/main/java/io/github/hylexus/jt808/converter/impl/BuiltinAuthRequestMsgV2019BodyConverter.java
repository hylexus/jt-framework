package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.req.BuiltinAuthRequestMsgV2011;
import io.github.hylexus.jt808.msg.req.BuiltinAuthRequestMsgV2019;
import io.github.hylexus.oaks.utils.Bytes;

import java.util.Optional;
import java.util.Set;

/**
 * @author hylexus
 * createdAt 2019/2/1
 * @see BuiltinAuthRequestMsgV2011
 **/
@BuiltinComponent
public class BuiltinAuthRequestMsgV2019BodyConverter extends AbstractBuiltinRequestMsgBodyConverter<BuiltinAuthRequestMsgV2019> {

    @Override
    public Set<Jt808ProtocolVersion> getSupportedProtocolVersion() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2019();
    }

    @Override
    public Optional<BuiltinAuthRequestMsgV2019> convert2Entity(RequestMsgMetadata metadata) {
        final byte[] bytes = metadata.getBodyBytes();
        final byte authCodeLength = bytes[0];
        final BuiltinAuthRequestMsgV2019 body = new BuiltinAuthRequestMsgV2019()
                .setHeader(metadata.getHeader())
                .setAuthCodeLength(authCodeLength)
                .setAuthCode(ProtocolUtils.bytes2String(bytes, 1, authCodeLength))
                .setImei(Bytes.subSequence(bytes, authCodeLength + 1, 15))
                .setSoftwareVersion(Bytes.subSequence(bytes, authCodeLength + 16, 20));
        return Optional.of(body);
    }

}
