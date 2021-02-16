# 报文构建器

```java
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.utils.HexStringUtils;
import org.junit.Test;

import java.nio.charset.Charset;

public class Jt808MsgBuilderTest {

    private static final Charset CHARSET = JtProtocolConstant.JT_808_STRING_ENCODING;

    @Test
    public void builder() {
        final Jt808MsgBuilder builder = Jt808MsgBuilder.builder()
                // 消息头
                .header(msgHeaderSpecBuilder -> msgHeaderSpecBuilder
                        // 消息ID
                        .withMsgId(BuiltinJt808MsgType.CLIENT_REGISTER)
                        // 消息体属性(消息体长度自动计算)
                        .withMsgBodyPropsSpec(msgBodyPropsSpecBuilder -> msgBodyPropsSpecBuilder
                                .withEncryptionType(0b000)
                                .withSubPackage(false)
                                .withReversedBit14AndBit15(0)
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
                        .appendString("13160466666")
                        .build()
                );

        System.out.println(HexStringUtils.bytes2HexString(builder.build(true)));
        System.out.println(HexStringUtils.bytes2HexString(builder.build(false)));
    }

    @Test
    public void testBuildQueryTerminalProperties() {
        final byte[] bytes = Jt808MsgBuilder.builder()
                .header(jt808MsgHeaderBuilder -> jt808MsgHeaderBuilder
                        .withMsgId(0x0107)
                        .withMsgBodyPropsSpec(msgBodyPropsSpecBuilder -> msgBodyPropsSpecBuilder
                                .withEncryptionType(0b000)
                                .withSubPackage(false)
                                .withReversedBit14AndBit15(0)
                                .build()
                        )
                        .withTerminalId("13912344321")
                        .withFlowId(1)
                        .build()
                )
                .body(messageBuilder -> messageBuilder
                        .appendWord(1)
                        .appendBytes("12345".getBytes(CHARSET))
                        .build()
                )
                .build();
        System.out.println(HexStringUtils.bytes2HexString(bytes));
    }
}
```
