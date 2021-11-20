package io.github.hylexus.jt.msg.builder.jt808;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.utils.HexStringUtils;
import org.junit.Test;

import java.nio.charset.Charset;

public class ClientRegisterMsgTest {
    private static final Charset CHARSET = JtProtocolConstant.JT_808_STRING_ENCODING;

    @Test
    public void testBuildClientRegisterMsgV2011() {
        final Jt808MsgBuilder builder = Jt808MsgBuilder.builder()
                // 消息头
                .header(msgHeaderSpecBuilder -> msgHeaderSpecBuilder
                        // 消息ID
                        .withMsgId(BuiltinJt808MsgType.CLIENT_REGISTER)
                        // 消息体属性(消息体长度自动计算)
                        .withMsgBodyPropsSpec(msgBodyPropsSpecBuilder -> msgBodyPropsSpecBuilder
                                .withEncryptionType(0b000)
                                .withSubPackage(false)
                                .withVersion(Jt808ProtocolVersion.VERSION_2011)
                                .withReversedBit15(0)
                                .build()
                        )
                        // 终端ID
                        .withTerminalId("13912344321")
                        // 流水号
                        .withFlowId(123)
                        .build()
                )
                // 消息体
                .body(msgBodyBuilder -> msgBodyBuilder
                        .appendWord(11) //省域 ID [WORD]
                        .appendWord(0)// 市县域 ID [WORD]
                        .appendBytes("12359".getBytes(CHARSET)) // 制造商 ID [BYTE[5]]
                        .appendBytes("12345678901234567890".getBytes(CHARSET)) // 终端型号 [BYTE[20]]
                        .appendBytes("id12345".getBytes(CHARSET)) // 终端 ID [BYTE[7]]
                        .appendByte(1) // 车牌颜色 [BYTE]
                        .appendString("甘A-123456")
                        .build()
                );

        System.out.println(HexStringUtils.bytes2HexString(builder.build(true)));
        System.out.println(HexStringUtils.bytes2HexString(builder.build(false)));
    }


    @Test
    public void testBuildClientRegisterReplyMsgV2011() {
        final Jt808MsgBuilder builder = Jt808MsgBuilder.builder()
                // 消息头
                .header(msgHeaderSpecBuilder -> msgHeaderSpecBuilder
                        // 消息ID
                        .withMsgId(BuiltinJt808MsgType.CLIENT_REGISTER_REPLY)
                        // 消息体属性(消息体长度自动计算)
                        .withMsgBodyPropsSpec(msgBodyPropsSpecBuilder -> msgBodyPropsSpecBuilder
                                .withEncryptionType(0b000)
                                .withSubPackage(false)
                                .withVersion(Jt808ProtocolVersion.VERSION_2011)
                                .withReversedBit15(0)
                                .build()
                        )
                        // 终端ID
                        .withTerminalId("013912344321")
                        // 流水号
                        .withFlowId(123)
                        .build()
                )
                // 消息体
                .body(msgBodyBuilder -> msgBodyBuilder
                        .appendWord(123) //应答流水号 [WORD]
                        .appendByte(0)// 结果 ID [WORD]
                        .appendString("admin-admin") // 鉴权码 STRING
                        .build()
                );

        System.out.println(HexStringUtils.bytes2HexString(builder.build(true)));
        System.out.println(HexStringUtils.bytes2HexString(builder.build(false)));
    }


    @Test
    public void testBuildClientRegisterMsgV2019() {
        final Jt808MsgBuilder builder = Jt808MsgBuilder.builder()
                // 消息头
                .header(msgHeaderSpecBuilder -> msgHeaderSpecBuilder
                        // 消息ID
                        .withMsgId(BuiltinJt808MsgType.CLIENT_REGISTER)
                        // 消息体属性(消息体长度自动计算)
                        .withMsgBodyPropsSpec(msgBodyPropsSpecBuilder -> msgBodyPropsSpecBuilder
                                .withEncryptionType(0b000)
                                .withSubPackage(false)
                                .withVersion(Jt808ProtocolVersion.VERSION_2019)
                                .withReversedBit15(0)
                                .build()
                        )
                        // 终端ID
                        .withTerminalId("00000000013912344321")
                        // 流水号
                        .withFlowId(123)
                        .build()
                )
                // 消息体
                .body(msgBodyBuilder -> msgBodyBuilder
                        .appendWord(11) //省域 ID [WORD]
                        .appendWord(0)// 市县域 ID [WORD]
                        .appendBytes("12359123591".getBytes(CHARSET)) // 制造商 ID [BYTE[11]]
                        .appendBytes("123456789012345678901234567890".getBytes(CHARSET)) // 终端型号 [BYTE[30]]
                        .appendBytes("id1234567890123456789012345678".getBytes(CHARSET)) // 终端 ID [BYTE[30]]
                        .appendByte(1) // 车牌颜色 [BYTE]
                        .appendString("甘J-654321")
                        .build()
                );

        System.out.println(HexStringUtils.bytes2HexString(builder.build(true)));
        System.out.println(HexStringUtils.bytes2HexString(builder.build(false)));
    }

    @Test
    public void testBuildClientRegisterReplyMsgV2019() {
        final Jt808MsgBuilder builder = Jt808MsgBuilder.builder()
                // 消息头
                .header(msgHeaderSpecBuilder -> msgHeaderSpecBuilder
                        // 消息ID
                        .withMsgId(BuiltinJt808MsgType.CLIENT_REGISTER_REPLY)
                        // 消息体属性(消息体长度自动计算)
                        .withMsgBodyPropsSpec(msgBodyPropsSpecBuilder -> msgBodyPropsSpecBuilder
                                .withEncryptionType(0b000)
                                .withSubPackage(false)
                                .withVersion(Jt808ProtocolVersion.VERSION_2019)
                                .withReversedBit15(0)
                                .build()
                        )
                        // 终端ID
                        .withTerminalId("00000000013912344321")
                        // 流水号
                        .withFlowId(123)
                        .build()
                )
                // 消息体
                .body(msgBodyBuilder -> msgBodyBuilder
                        .appendWord(123) //应答流水号 [WORD]
                        .appendByte(0)// 结果 ID [WORD]
                        .appendString("admin-admin") // 鉴权码 STRING
                        .build()
                );

        System.out.println(HexStringUtils.bytes2HexString(builder.build(true)));
        System.out.println(HexStringUtils.bytes2HexString(builder.build(false)));
    }

}
