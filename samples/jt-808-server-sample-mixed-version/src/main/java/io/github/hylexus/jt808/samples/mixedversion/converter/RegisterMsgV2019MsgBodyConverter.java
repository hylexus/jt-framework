package io.github.hylexus.jt808.samples.mixedversion.converter;

import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgConverter;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.samples.mixedversion.entity.req.RegisterRequestMsgV2019;
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
public class RegisterMsgV2019MsgBodyConverter implements RequestMsgBodyConverter<RegisterRequestMsgV2019> {

    @Override
    public Optional<RegisterRequestMsgV2019> convert2Entity(RequestMsgMetadata metadata) {
        byte[] bytes = metadata.getBodyBytes();
        RegisterRequestMsgV2019 body = new RegisterRequestMsgV2019();
        body.setProvinceId(intFromBytes(bytes, 0, 2));
        body.setCityId(intFromBytes(bytes, 2, 2));
        body.setManufacturerId(Bytes.subSequence(bytes, 4, 11));
        body.setTerminalType(Bytes.subSequence(bytes, 15, 30));
        body.setTerminalId(Bytes.subSequence(bytes, 45, 30));
        body.setColor(bytes[75]);
        body.setCarIdentifier(new String(Bytes.subSequence(bytes, 76, metadata.getHeader().getMsgBodyLength() - 76), Charset.forName("GBK")));
        return Optional.of(body);
    }

    @Override
    public Set<Jt808ProtocolVersion> getSupportedProtocolVersion() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2019();
    }
}
