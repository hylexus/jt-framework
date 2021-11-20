package io.github.hylexus.jt808.samples.mixedversion.converter;

import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgConverter;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.samples.mixedversion.entity.req.RegisterRequestMsgV2011;
import io.github.hylexus.oaks.utils.Bytes;

import java.nio.charset.Charset;
import java.util.Optional;
import java.util.Set;

import static io.github.hylexus.oaks.utils.IntBitOps.intFromBytes;

/**
 * @author hylexus
 * Created At 2021-11-14 6:12 下午
 */
@Jt808RequestMsgConverter(msgType = 0x0100)
public class RegisterMsgV2011MsgBodyConverter implements RequestMsgBodyConverter<RegisterRequestMsgV2011> {

    @Override
    public Optional<RegisterRequestMsgV2011> convert2Entity(RequestMsgMetadata metadata) {
        byte[] bytes = metadata.getBodyBytes();
        RegisterRequestMsgV2011 body = new RegisterRequestMsgV2011();
        body.setProvinceId(intFromBytes(bytes, 0, 2));
        body.setCityId(intFromBytes(bytes, 2, 2));
        body.setManufacturerId(Bytes.subSequence(bytes, 4, 5));
        body.setTerminalType(Bytes.subSequence(bytes, 9, 20));
        body.setTerminalId(Bytes.subSequence(bytes, 29, 7));
        body.setColor(bytes[36]);
        body.setCarIdentifier(new String(Bytes.subSequence(bytes, 37, metadata.getHeader().getMsgBodyLength() - 37), Charset.forName("GBK")));
        return Optional.of(body);
    }

    @Override
    public Set<Jt808ProtocolVersion> getSupportedProtocolVersion() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2011();
    }
}
