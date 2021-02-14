package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.codec.msg.RegisterMsgForTest;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import org.junit.Test;

public class DecoderTest {

    private final BytesEncoder bytesEncoder = new BytesEncoder.DefaultBytesEncoder();
    private final Decoder decoder = new Decoder(bytesEncoder);

    @Test
    public void parseMsgMetadata() throws Exception {
        String msg = "010000290131660488880025000B000031313131314D5239383034000000000000000000000000000031333136363034043132333448";
        byte[] bytes = HexStringUtils.hexString2Bytes(msg);
        RequestMsgMetadata metadata = decoder.parseMsgMetadata(Jt808ProtocolVersion.AUTO_DETECTION, bytes);
        RequestMsgHeader header = metadata.getHeader();
        System.out.println(header);

        final RegisterMsgForTest msgBody = decoder.decodeRequestMsgBody(RegisterMsgForTest.class, bytes, metadata);
        System.out.println(msgBody);
    }

    @Test
    public void decodeRequestMsgBody() {
    }
}