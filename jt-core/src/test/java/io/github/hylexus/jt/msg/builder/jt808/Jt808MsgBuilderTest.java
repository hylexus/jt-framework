package io.github.hylexus.jt.msg.builder.jt808;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.utils.HexStringUtils;
import org.junit.Test;

public class Jt808MsgBuilderTest {

    @Test
    public void testBuildQueryTerminalProperties() {
        final byte[] bytes = Jt808MsgBuilder.builder()
                .header(jt808MsgHeaderBuilder -> jt808MsgHeaderBuilder
                        .withMsgId(0x0107)
                        .withMsgBodyPropsSpec(msgBodyPropsSpecBuilder -> msgBodyPropsSpecBuilder
                                .withEncryptionType(0b000)
                                .withSubPackage(false)
                                .withVersion(Jt808ProtocolVersion.VERSION_2011)
                                .withReversedBit15(0)
                                .build()
                        )
                        .withTerminalId("13912344321")
                        .withFlowId(1)
                        .build()
                )
                .body(messageBuilder -> messageBuilder
                        .appendWord(1)
                        .appendBytes("12345".getBytes(JtProtocolConstant.JT_808_STRING_ENCODING))
                        .build()
                )
                .build();
        System.out.println(HexStringUtils.bytes2HexString(bytes));
    }

    @Test
    public void testBuildTerminalCommonReplyMsg() {
        final byte[] bytes = Jt808MsgBuilder.builder()
                .header(jt808MsgHeaderBuilder -> jt808MsgHeaderBuilder
                        .withMsgId(0x0001)
                        .withMsgBodyPropsSpec(msgBodyPropsSpecBuilder -> msgBodyPropsSpecBuilder
                                .withEncryptionType(0b000)
                                .withSubPackage(false)
                                .withVersion(Jt808ProtocolVersion.VERSION_2019)
                                .withReversedBit15(0)
                                .build()
                        )
                        .withTerminalId("0000000013912344321")
                        .withFlowId(1)
                        .build()
                )
                .body(messageBuilder -> messageBuilder
                        .appendWord(1) // 应答流水号 word 对应的平台消息的流水号
                        .appendWord(0x8103) // 应答ID 对应的平台消息的ID
                        .appendByte(0) // 结果
                        .build()
                )
                .build();
        System.out.println(HexStringUtils.bytes2HexString(bytes));
    }
}