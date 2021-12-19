package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ProtocolVersionDetector;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ProtocolVersionDetectorRegistry;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.data.deserialize.DefaultJt808FieldDeserializerRegistry;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

public class Jt808AnnotationBasedDecoderTest {

    private final Jt808MsgDecoder jt808MsgDecoder = new DefaultJt808MsgDecoder(
            new BuiltinJt808MsgTypeParser(),
            new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT),
            new DefaultJt808ProtocolVersionDetectorRegistry(new DefaultJt808ProtocolVersionDetector())
    );

    @Test
    public void testDecode() {
        final Jt808Request request = buildJt808Request();

        final Class<LocationUploadReqMsgV2019Test> cls = LocationUploadReqMsgV2019Test.class;
        final Jt808AnnotationBasedDecoder decoder = new Jt808AnnotationBasedDecoder(new DefaultJt808FieldDeserializerRegistry(true));
        final LocationUploadReqMsgV2019Test instance = decoder.decode(request, cls);
        System.out.println("-----------junit--------");
        System.out.println(instance);
    }

    private Jt808Request buildJt808Request() {
        final String str =
                "02004086010000000001893094655200E4000000000000000101D907F2073D336C00000000000021112411480801040000002603020000300115310100250400000000140"
                + "4000000011504000000FA160400000000170200001803000000EA10FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF02020000EF0400000000F31B017118000000000000000000"
                + "00000000000000000000000000000056";
        final ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer().writeBytes(HexStringUtils.hexString2Bytes(str));
        return jt808MsgDecoder.decode(Jt808ProtocolVersion.VERSION_2019, byteBuf);
    }
}