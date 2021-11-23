package io.github.hylexus.jt808.samples.customized.converter;

import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.samples.customized.msg.req.LocationUploadRequestMsgBody;
import io.github.hylexus.oaks.utils.BcdOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-09-24 11:12 下午
 */
public class LocationUploadMsgBodyConverter implements RequestMsgBodyConverter<LocationUploadRequestMsgBody> {

    @Override
    public Optional<LocationUploadRequestMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        // 使用ByteBuf读取字节的时候注意顺序！！！
        // 使用ByteBuf读取字节的时候注意顺序！！！
        // 使用ByteBuf读取字节的时候注意顺序！！！
        ByteBuf buf = Unpooled.wrappedBuffer(metadata.getBodyBytes());
        LocationUploadRequestMsgBody body = new LocationUploadRequestMsgBody();
        body.setWarningFlag(buf.readInt());
        body.setStatus(buf.readInt());
        body.setLat(buf.readInt() * 1.0 / 100_0000);
        body.setLng(buf.readInt() * 1.0 / 100_0000);
        body.setHeight(buf.readShort());
        body.setSpeed(buf.readShort());
        body.setDirection(buf.readShort());
        byte[] tmp = new byte[6];
        buf.readBytes(tmp);
        body.setTime(BcdOps.bytes2BcdStringV2(tmp, 0, 6));
        return Optional.of(body);
    }

}
