package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.codec.entity.req.LocationUploadRequestMsgBodyForDebug;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.oaks.utils.IntBitOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static io.github.hylexus.jt.config.Jt808ProtocolVersion.VERSION_2011;

/**
 * @author hylexus
 * Created At 2019-09-23 9:57 下午
 */
public class SampleDecoderTest {

    private final Decoder decoder = new Decoder(new BytesEncoder.DefaultBytesEncoder());

    @Test
    public void bugFix7() {
        // 7E00020000978290002147029E7D017E
        String hex = "00020000978290002147029E7D01";
        BytesEncoder bytesEncoder = new BytesEncoder.DefaultBytesEncoder();
        byte[] data = HexStringUtils.hexString2Bytes(hex);
        byte[] bytes = bytesEncoder.doEscapeForReceive(data, 0, data.length - 1);
        System.out.println(hex);
        System.out.println(HexStringUtils.bytes2HexString(bytes));
    }

    @Test
    public void testDecodeLocationUploadMsg() throws Exception {
        String hex = "020000E9015193700002001700000000000C00010157B4BC06CA54C20000000000DE190909174317010400000001020203F22504000000002A0200002B040000000030"
                     + "0116310100E3060064055F0000F3A30002020352000302139700040235BF0005040000025000060203E700070203E70008013C000902007D000B020000000C020028"
                     + "000D02A822000E019E000F018001000201E501010400000250010202033C010304000003F201040203E7010D020000010E0213BF010F02007D01100235C701120200"
                     + "00011302000001160200000050113141314A433534343452373235323336370051000052040000000C010C02079E34";

        RequestMsgMetadata metadata = decoder.parseMsgMetadata(VERSION_2011, HexStringUtils.hexString2Bytes(hex));
        byte[] bodyBytes = metadata.getBodyBytes();

        LocationUploadRequestMsgBodyForDebug y = decoder.decodeRequestMsgBody(LocationUploadRequestMsgBodyForDebug.class, bodyBytes, metadata);
        System.out.println(y);
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
