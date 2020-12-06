package io.github.hylexus.jt.codec.decode;

import io.github.hylexus.jt.builder.jt808.Jt808MsgBuilder;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.samples.annotation.config.Jt808MsgType;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class MsgBuilderTest {

    @Test
    public void testBuildQueryTerminalProperties() {
        final byte[] bytes = Jt808MsgBuilder.newBuilder()
                .body(messageBuilder -> messageBuilder
                        .appendWord(1)
                        .appendBytes("12345".getBytes(StandardCharsets.UTF_8))
                )
                .header(jt808MsgHeaderBuilder -> jt808MsgHeaderBuilder
                        .withMsgId(Jt808MsgType.INSTRUCTION_APPLY_TERMINAL_PROPERTIES)
                        .withTerminalId("13166048888")
                        .withFlowId(1)
                )
                .build();
        System.out.println(HexStringUtils.bytes2HexString(bytes));
    }
}
