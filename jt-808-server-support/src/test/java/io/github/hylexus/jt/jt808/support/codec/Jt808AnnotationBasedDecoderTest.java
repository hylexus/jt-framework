package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.spec.Jt808MsgEncryptionHandler;
import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ProtocolVersionDetector;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ProtocolVersionDetectorRegistry;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.codec.entity.DebugTerminalRegisterMsgV2019;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.data.deserialize.DefaultJt808FieldDeserializerRegistry;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Jt808AnnotationBasedDecoderTest {

    private final Jt808MsgDecoder jt808MsgDecoder = new DefaultJt808MsgDecoder(
            new BuiltinJt808MsgTypeParser(),
            new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT),
            new DefaultJt808ProtocolVersionDetectorRegistry(new DefaultJt808ProtocolVersionDetector()),
            Jt808MsgEncryptionHandler.NO_OPS
    );

    private Jt808AnnotationBasedDecoder decoder;

    @BeforeEach
    void setUp() {
        this.decoder = new Jt808AnnotationBasedDecoder(new DefaultJt808FieldDeserializerRegistry(true));
    }

    @Test
    void testDecode1() {
        final Jt808Request jt808Request = buildJt808Request("010040560100000000013912344321007B000B0000313233"
                + "35393132333539313132333435363738393031323334353637383930313233343536373839306964313233343536373839"
                + "3031323334353637383930313233343536373801B8CA4A2D3635343332313C");

        DebugTerminalRegisterMsgV2019 entity = this.decoder.decode(jt808Request, DebugTerminalRegisterMsgV2019.class);
        System.out.println(entity);

        jt808Request.release();
    }

    @Test
    public void testDecode() {

        final Jt808AnnotationBasedDecoder decoder = new Jt808AnnotationBasedDecoder(new DefaultJt808FieldDeserializerRegistry(true));

        final String hexString = "02004086010000000001893094655200E4000000000000000101D907F2073D336C00000000000021112411"
                + "4808010400000026030200003001153101002504000000001404000000011504000000FA160400000000170200001803000000E"
                + "A10FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF02020000EF0400000000F31B017118000000000000000000"
                + "00000000000000000000000000000056";
        System.out.println(hexString);
        final Jt808Request request1 = buildJt808Request(hexString);
        final LocationUploadReqMsgV2019Test instance = decoder.decode(request1, LocationUploadReqMsgV2019Test.class);
        request1.release();

        final Jt808Request request2 = buildJt808Request(hexString);
        final LocationUploadReqMsgV2019AliasTest instance2 = decoder.decode(request2, LocationUploadReqMsgV2019AliasTest.class);
        request2.release();

        System.out.println("-----------junit--------");
        System.out.println(instance);
        System.out.println(instance2);
    }

    private Jt808Request buildJt808Request(String hexString) {
        final ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer().writeBytes(HexStringUtils.hexString2Bytes(hexString));
        return jt808MsgDecoder.decode(byteBuf);
    }
}
