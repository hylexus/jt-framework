package io.github.hylexus.jt.jt808.spec.impl.response;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

/**
 * @author hylexus
 */
public class DefaultJt808ResponseBuilderTest {

    @Test
    public void testBuildRegisterMsgV2011() {
        final Jt808Response response = Jt808Response.newBuilder()
                .version(Jt808ProtocolVersion.VERSION_2011)
                .msgId(BuiltinJt808MsgType.CLIENT_REGISTER)
                .terminalId("013912344321")
                .flowId(123)
                .body(writer -> writer
                        // 省域ID WORD
                        .writeWord(11)
                        // 市县域ID WORD
                        .writeWord(2)
                        // 制造商ID byte[5]
                        .writeString("id123")
                        // 终端型号 byte[8]
                        .writeString("abcdefgh")
                        // 终端ID byte[7]
                        .writeString("ID12345")
                        .writeByte(1)
                        .writeString("甘J-123451")
                )
                .build();
        final ByteBuf byteBuf = encode(response);
        System.out.println(HexStringUtils.byteBufToString(byteBuf));
    }

    @Test
    public void testBuildRegisterMsgV2013() {
        final Jt808Response response = Jt808Response.newBuilder()
                .version(Jt808ProtocolVersion.VERSION_2013)
                .msgId(BuiltinJt808MsgType.CLIENT_REGISTER)
                .terminalId("013912344323")
                .flowId(123)
                .body(writer -> writer
                        // 省域ID WORD
                        .writeWord(11)
                        // 市县域ID WORD
                        .writeWord(2)
                        // 制造商ID byte[5]
                        .writeString("id123")
                        // 终端型号 byte[20]
                        .writeString("type1234567887654321")
                        // 终端ID byte[7]
                        .writeString("ID12345")
                        .writeByte(1)
                        .writeString("甘J-123453")
                )
                .build();
        final ByteBuf byteBuf = encode(response);
        System.out.println(HexStringUtils.byteBufToString(byteBuf));
    }

    @Test
    public void testBuildRegisterMsgV2019() {
        final Jt808Response response = Jt808Response.newBuilder()
                .version(Jt808ProtocolVersion.VERSION_2019)
                .msgId(BuiltinJt808MsgType.CLIENT_REGISTER)
                .terminalId("00000000013912344329")
                .flowId(123)
                .body(writer -> writer
                        // 省域ID WORD
                        .writeWord(11)
                        // 市县域ID WORD
                        .writeWord(2)
                        // 制造商ID byte[11]
                        .writeString("id987654321")
                        // 终端型号 byte[30]
                        .writeString("type00123456781234567887654321")
                        // 终端ID byte[30]
                        .writeString("ID0000123456781234567887654321")
                        .writeByte(1)
                        .writeString("甘J-123459")
                )
                .build();
        final ByteBuf byteBuf = encode(response);
        System.out.println(HexStringUtils.byteBufToString(byteBuf));
    }

    private ByteBuf encode(Jt808Response response) {
        return new DefaultJt808MsgEncoder(new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT)).encode(response);
    }
}