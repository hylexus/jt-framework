package io.github.hylexus.jt808.server.converter;

import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.server.msg.req.LocationUploadMsgBody;
import io.github.hylexus.oaks.utils.BcdOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-09-24 11:12 下午
 */
public class LocationUploadMsgBodyConverter implements RequestMsgBodyConverter<LocationUploadMsgBody> {

    @Override
    public Optional<LocationUploadMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        ByteBuf buf = Unpooled.wrappedBuffer(metadata.getBodyBytes());
        LocationUploadMsgBody body = new LocationUploadMsgBody();
        body.setWarningFlag(buf.readInt());
        body.setStatus(buf.readInt());
        body.setLat(buf.readInt() * 1.0 / 100_0000);
        body.setLng(buf.readInt() * 1.0 / 100_0000);
        body.setHeight(buf.readShort());
        body.setSpeed(buf.readShort());
        body.setDirection(buf.readShort());
        byte[] tmp = new byte[6];
        buf.readBytes(tmp);
        body.setTime(BcdOps.bytes2BcdString(tmp, 0, 6));
        return Optional.of(body);
    }

}
