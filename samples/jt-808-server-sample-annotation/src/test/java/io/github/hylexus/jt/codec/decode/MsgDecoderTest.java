package io.github.hylexus.jt.codec.decode;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.codec.Decoder;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.samples.annotation.entity.req.Msg0104;
import io.github.hylexus.jt808.samples.annotation.entity.req.demo01.LocationUploadRequestMsgBodyDemo01;
import io.github.hylexus.oaks.utils.IntBitOps;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created At 2020-07-29 12:23
 *
 * @author hylexus
 */
public class MsgDecoderTest {

    private final BytesEncoder bytesEncoder = new BytesEncoder.DefaultBytesEncoder();
    private final Decoder decoder = new Decoder(bytesEncoder);

    @Test
    public void testDecodeLocationUpload() throws Exception {
        String locationUploadMsg = "020000AE0153123556160E3600000000000C000101EB1472071E23D400B000000000200706164830"
                + "01040000001F03020000140400000000150400000000160400000000170200001803000000250400000000"
                + "2B0400000000300103310115652F06AAE14502040100000100000000B001EB1472071E23D420070616483000013"
                + "1303030303100200706164805000500B70400000000320150B1040B030100B40100B6020000BA020002BD0E"
                + "0000000000000000000000000000ED";

        byte[] bytes = HexStringUtils.hexString2Bytes(locationUploadMsg);
        RequestMsgMetadata metadata = decoder.parseMsgMetadata(Jt808ProtocolVersion.AUTO_DETECTION, bytes);
        RequestMsgHeader header = metadata.getHeader();

        Assert.assertEquals(Jt808ProtocolVersion.VERSION_2011, header.getVersion());
        Assert.assertEquals(0x0200, header.getMsgId());
        Assert.assertEquals(174, header.getMsgBodyPropsField());
        Assert.assertEquals(174, header.getMsgBodyLength());
        Assert.assertEquals(0, header.getEncryptionType());
        Assert.assertEquals("15312355616", header.getTerminalId());
        Assert.assertEquals(0x0e36, header.getFlowId());
        Assert.assertFalse(header.isHasSubPackage());

        final byte[] bodyBytes = metadata.getBodyBytes();
        Assert.assertEquals(bodyBytes.length, header.getMsgBodyLength());

        LocationUploadRequestMsgBodyDemo01 msgBody = decoder.decodeRequestMsgBody(LocationUploadRequestMsgBodyDemo01.class, bodyBytes, metadata);

        Assert.assertEquals(0, msgBody.getAlarmFlag());
        Assert.assertEquals(0x000C0001, msgBody.getStatus());
        Assert.assertArrayEquals(msgBody.getStatusBytes(), IntBitOps.intTo4Bytes(msgBody.getStatus()));

        LocationUploadRequestMsgBodyDemo01.LocationUploadStatus statusInfo = msgBody.getStatusInfo();
        Assert.assertTrue(statusInfo.isAccStatus());
        Assert.assertFalse(statusInfo.getIsSouthLat());
        Assert.assertEquals((Integer) 0, statusInfo.getLngType());

        Assert.assertEquals((Integer) 0x01EB1472, msgBody.getIntLat());
        Assert.assertEquals((Double) (0x01EB1472 * 1.0 / 100_0000), msgBody.getLat());
        Assert.assertEquals((Integer) 0x071E23D4, msgBody.getIntLng());
        Assert.assertEquals((Double) (0x071E23D4 * 1.0 / 100_0000), msgBody.getLng());

        Assert.assertEquals((Integer) 0x00B0, msgBody.getHeight());
        Assert.assertEquals(0, msgBody.getSpeed());
        Assert.assertEquals((Integer) 0, msgBody.getDirection());
        Assert.assertEquals("200706164830", msgBody.getTime());

        LocationUploadRequestMsgBodyDemo01.ExtraInfo extraInfo = msgBody.getExtraInfo();
        Assert.assertEquals((Integer) 0x0000001F, extraInfo.getField0x01());
        Assert.assertEquals((Integer) 0, extraInfo.getField0x2b());
        Assert.assertEquals((byte) 3, extraInfo.getField0x30());
        Assert.assertEquals((byte) 0x15, extraInfo.getField0x31());

        // 06AAE14502040100000100000000B001EB1472071E23D4200706164830000131303030303100200706164805000500
        // 000000B0 01EB1472 071E23D4 200706164830 000131303030303100200706164805000500
        LocationUploadRequestMsgBodyDemo01.Extra0x65 field0x65 = extraInfo.getField0x65();
        Assert.assertEquals((Integer) 0x06AAE145, field0x65.getWarningId());
        Assert.assertEquals((Integer) 0x02, field0x65.getStatus());
        Assert.assertEquals((Integer) 0x04, field0x65.getField5());
        Assert.assertEquals((Integer) 0x01, field0x65.getField6());
        Assert.assertEquals(0x01EB1472, (int) (field0x65.getLat() * 100_0000));
        Assert.assertEquals(0x071E23D4, (int) (field0x65.getLng() * 100_0000));
        Assert.assertEquals("200706164830", field0x65.getTime());
    }

    @Test
    public void testDecodeMsg0104() throws Exception {
        // 瞎编的一条报文
        String str =
                "010401BA014594642815048C0002320000000104000000B40000000104000000B8"
                        + "0000000204000000050000000304000000030000000404000000000000000504000000000000000604000000000000000704000000000000001005434D4E4554"
                        + "00000011000000001200000000130E6A74312E6767687970742E6E6574000000140000000015000000001600000000170E6A74322E6767687970742E6E657400000"
                        + "0180400001B60000000190400001B60000000200400000000000000210400000000000000220400000000000000280400000000000000270400000078000000"
                        + "29040000001E0000002C040000000000000050040000000000000052040000000000000053040000000000000055040000006400000056040000000A0000005704"
                        + "000038400000005804000000000000005904000004B00000005A04000000000000005B0200320000005C0207080000007004000000000000007104000000000000"
                        + "0072040000000000000073040000000000000074040000000000000080040000008C00000081020035000000820209C4000000830CD4C6474353313233000000000"
                        + "0000084010200000090010300001018010100001022040801000500001023010082";
        byte[] bytes = HexStringUtils.hexString2Bytes(str);
        RequestMsgMetadata metadata = decoder.parseMsgMetadata(Jt808ProtocolVersion.AUTO_DETECTION, bytes);
        RequestMsgHeader header = metadata.getHeader();
        System.out.println(header.getTerminalId());

        final byte[] bodyBytes = metadata.getBodyBytes();
        Assert.assertEquals(bodyBytes.length, header.getMsgBodyLength());

        Msg0104 msgBody = decoder.decodeRequestMsgBody(Msg0104.class, bodyBytes, metadata);

        System.out.println(msgBody);
    }
}
