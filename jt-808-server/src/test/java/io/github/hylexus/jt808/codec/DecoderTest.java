package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.server.converter.LocationUploadMsgBodyConverter;
import io.github.hylexus.jt808.server.converter.LocationUploadMsgBodyConverter2;
import io.github.hylexus.jt808.server.msg.req.LocationUploadMsgBody;
import io.github.hylexus.oaks.utils.IntBitOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-09-23 9:57 下午
 */
public class DecoderTest {

    private Decoder decoder = new Decoder();

    @Test
    public void testDecodeLocationUploadMsg() throws Exception {
        String hex = "0200003C01371786195514780000000000000002026161D806EE1828000000000000140806024007010"
                + "40000000033182A4D30302C31352C31303430323130383736353433323123B6";

        RequestMsgMetadata metadata = decoder.parseMsgMetadata(HexStringUtils.hexString2Bytes(hex));
        byte[] bodyBytes = metadata.getBodyBytes();

        LocationUploadMsgBody y = decoder.decodeRequestMsgBody(LocationUploadMsgBody.class, bodyBytes, metadata);
        System.out.println(y);
        Optional<LocationUploadMsgBody> body = new LocationUploadMsgBodyConverter().convert2Entity(metadata);
        System.out.println(body.get());
        Optional<LocationUploadMsgBody> x = new LocationUploadMsgBodyConverter2().convert2Entity(metadata);
        System.out.println(x.get());
    }

    @Test
    public void test23() {
        byte[] bytes = {97, 97};
        System.out.println(IntBitOps.intFromBytes(bytes));
        System.out.println((short) IntBitOps.intFromBytes(bytes));

        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
        System.out.println(buf.readShort());
    }

}
