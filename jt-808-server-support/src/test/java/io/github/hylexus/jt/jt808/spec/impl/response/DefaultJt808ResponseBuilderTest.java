package io.github.hylexus.jt.jt808.spec.impl.response;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808MsgEncryptionHandler;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.spec.session.DefaultJt808FlowIdGenerator;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.CompositeJt808ResponseSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * @author hylexus
 */
public class DefaultJt808ResponseBuilderTest {

    @Test
    public void testBuildMsg0005() {
        final Jt808Response response = Jt808Response.newBuilder()
                .version(Jt808ProtocolVersion.VERSION_2019)
                .msgId(BuiltinJt808MsgType.CLIENT_RETRANSMISSION)
                .terminalId("00000000013912344329")
                .flowId(1)
                .body(writer -> writer
                        // 第一包的流水号
                        .writeWord(3)
                        // 包总数
                        .writeWord(2)
                        .writeWord(1)
                        .writeWord(2)
                )
                .build();
        final ByteBuf byteBuf = encode(response);
        System.out.println(HexStringUtils.byteBufToString(byteBuf));
    }

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
    public void testBuildTerminalCommonReplyV2013() {
        final Jt808Response response = Jt808Response.newBuilder()
                .version(Jt808ProtocolVersion.VERSION_2013)
                .msgId(BuiltinJt808MsgType.CLIENT_COMMON_REPLY)
                .terminalId("013912344323")
                .flowId(123)
                .body(writer -> writer
                        // 1. 应答流水号 WORD    对应的平台消息的流水号
                        .writeWord(0)
                        // 2. 应答id WORD     对应的平台消息的 ID
                        .writeWord(0x8103)
                        // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
                        .writeByte(0)
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

    @Test
    public void testBuildRegisterMsgV2019WithSubPackage() {
        final Jt808Response response = Jt808Response.newBuilder()
                .version(Jt808ProtocolVersion.VERSION_2019)
                .msgId(BuiltinJt808MsgType.CLIENT_REGISTER)
                .terminalId("00000000013912344329")
                .maxPackageSize(60)
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
        return new DefaultJt808MsgEncoder(
                ByteBufAllocator.DEFAULT, new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT),
                new CompositeJt808ResponseSubPackageEventListener(new ArrayList<>()),
                Jt808ResponseSubPackageStorage.NO_OPS_STORAGE,
                Jt808MsgEncryptionHandler.NO_OPS
        ).encode(response, new DefaultJt808FlowIdGenerator());
    }
}
