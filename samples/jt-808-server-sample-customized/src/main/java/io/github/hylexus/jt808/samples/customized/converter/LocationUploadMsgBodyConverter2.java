package io.github.hylexus.jt808.samples.customized.converter;

import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgConverter;
import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.samples.customized.msg.req.LocationUploadRequestMsgBody;
import io.github.hylexus.oaks.utils.BcdOps;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.github.hylexus.oaks.utils.IntBitOps.intFromBytes;

/**
 * @author hylexus
 * Created At 2019-09-24 11:12 下午
 */
@Jt808RequestMsgConverter(msgType = 0x0200)
public class LocationUploadMsgBodyConverter2 implements RequestMsgBodyConverter<LocationUploadRequestMsgBody> {

    @Override
    public Optional<LocationUploadRequestMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        byte[] bytes = metadata.getBodyBytes();
        LocationUploadRequestMsgBody body = new LocationUploadRequestMsgBody();
        body.setWarningFlag(intFromBytes(bytes, 0, 4));
        body.setStatus(intFromBytes(bytes, 4, 4));
        body.setLat(intFromBytes(bytes, 8, 4) * 1.0 / 100_0000);
        body.setLng(intFromBytes(bytes, 12, 4) * 1.0 / 100_0000);
        body.setHeight((short) intFromBytes(bytes, 16, 2));
        body.setSpeed((short) intFromBytes(bytes, 18, 2));
        body.setDirection((short) intFromBytes(bytes, 20, 2));
        body.setTime(BcdOps.bytes2BcdString(bytes, 22, 6));
        return Optional.of(body);
    }

}
