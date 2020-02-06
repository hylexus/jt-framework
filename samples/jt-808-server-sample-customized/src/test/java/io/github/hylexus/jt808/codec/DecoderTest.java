package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.codec.entity.req.LocationUploadRequestMsgBodyForDebug;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.oaks.utils.IntBitOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

/**
 * @author hylexus
 * Created At 2019-09-23 9:57 下午
 */
public class DecoderTest {

    private Decoder decoder = new Decoder();

    @Test
    public void testDecodeLocationUploadMsg() throws Exception {
        //String hex = "0200003C01371786195514780000000000000002026161D806EE1828000000000000140806024007010"
        //      + "40000000033182A4D30302C31352C31303430323130383736353433323123B6";

        //        String hex = "020000E0013184610090000600000000000400010157B43706CA54D30000000000001811251334570104000000012504000"
        //                + "000002A0200002B0400000000300119310100E306006438CA0000F39E0002020000000302000000040238CA0005040000000000060200000007"
        //                + "020000000801000009020014000B020000000C020000000D020000000E0100000F010001000200000101040000000001020200000103040000000001"
        //                + "04020000010D020000010E020000010F0200140110022EE801120200000113"
        //                + "02000001160200000050114C53474B45353448584857303530313230005"
        //                + "100005204000000016C";

        String hex = "020000E9015193700002001700000000000C00010157B4BC06CA54C20000000000DE190909174317010400000001020203F22504000000002A0200002B040000000030"
                + "0116310100E3060064055F0000F3A30002020352000302139700040235BF0005040000025000060203E700070203E70008013C000902007D000B020000000C020028000D02A8"
                + "22000E019E000F018001000201E501010400000250010202033C010304000003F201040203E7010D020000010E0213BF010F02007D01100235C701120200000113020000011"
                + "60200000050113141314A433534343452373235323336370051000052040000000C010C02079E34";

        RequestMsgMetadata metadata = decoder.parseMsgMetadata(HexStringUtils.hexString2Bytes(hex));
        byte[] bodyBytes = metadata.getBodyBytes();

        LocationUploadRequestMsgBodyForDebug y = decoder.decodeRequestMsgBody(LocationUploadRequestMsgBodyForDebug.class, bodyBytes, metadata);
        System.out.println(y);
        //ExtraEntity extraEntity = y.getExtraEntity();
        //System.out.println(extraEntity);
        //System.out.println(extraEntity.getObdData());
        //        System.out.println("Integer.toBinaryString(y.getStatus()) = " + Integer.toBinaryString(y.getStatus()));
        //        System.out.println(y.getStatusInfo());
        //        System.out.println("y.getAcc() = " + y.getAccStatus());
        //        System.out.println("y.getAcc() = " + y.getAccStatus1());
        //        System.out.println("y.getBit8to9() = " + y.getBit8to9());
        //        System.out.println("y.getBit18() = " + y.getBit18());
        //        System.out.println(y);
        // System.out.println(JSON.toJSONString(extraEntity.getObdData(), true));
        //        Optional<LocationUploadMsgBody> body = new LocationUploadMsgBodyConverter().convert2Entity(metadata);
        //        System.out.println(body.get());
        //        Optional<LocationUploadMsgBody> x = new LocationUploadMsgBodyConverter2().convert2Entity(metadata);
        //        System.out.println(x.get());
        //        List<AdditionalItemEntity> additionalItemEntity = y.getAdditionalInfo();
        //        additionalItemEntity.forEach(msg -> {
        //            System.out.println(HexStringUtils.int2HexString(msg.getMsgId(), 4, true) + " --> " + msg);
        //        });
        //System.out.println(JSON.toJSONString(y.getExtraMsg(), true));
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
