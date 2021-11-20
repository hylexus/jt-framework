package io.github.hylexus.jt.msg.builder.jt808;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.utils.HexStringUtils;
import org.junit.Test;

import java.nio.charset.Charset;

public class ClientAuthMsgTest {
    private static final Charset CHARSET = JtProtocolConstant.JT_808_STRING_ENCODING;

    @Test
    public void testBuildClientAuthMsgV2011() {
        final Jt808MsgBuilder builder = Jt808MsgBuilder.builder()
                // 消息头
                .header(msgHeaderSpecBuilder -> msgHeaderSpecBuilder
                        // 消息ID
                        .withMsgId(BuiltinJt808MsgType.CLIENT_AUTH)
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
                .body(msgBodyBuilder -> msgBodyBuilder.appendString("admin-123456").build());

        System.out.println(HexStringUtils.bytes2HexString(builder.build(true)));
        System.out.println(HexStringUtils.bytes2HexString(builder.build(false)));
    }

    @Test
    public void testBuildClientAuthMsgV2019() {
        final Jt808MsgBuilder builder = Jt808MsgBuilder.builder()
                // 消息头
                .header(msgHeaderSpecBuilder -> msgHeaderSpecBuilder
                        // 消息ID
                        .withMsgId(BuiltinJt808MsgType.CLIENT_AUTH)
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
                        .appendByte(12) // 鉴权码长度
                        .appendString("admin-654321") // 鉴权码内容
                        .appendBytes("112233445566778".getBytes(CHARSET))// 终端IMEI [byte[15]]
                        .appendBytes("11223344556677889901".getBytes(CHARSET)) // 软件版本号 byte[20]
                        .build()
                );

        System.out.println(HexStringUtils.bytes2HexString(builder.build(true)));
        System.out.println(HexStringUtils.bytes2HexString(builder.build(false)));
    }
}
