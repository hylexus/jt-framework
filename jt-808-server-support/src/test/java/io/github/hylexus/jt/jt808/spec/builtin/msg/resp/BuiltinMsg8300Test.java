package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuiltinMsg8300Test extends BaseReqRespMsgTest {

    @Test
    void testBuild0x8300() {
        final String hexString = this.encode(
                new BuiltinMsg8300Alias()
                        .setFlag((byte) 0b101)
                        .setMessage("message .......... msg"),
                builder ->
                        builder.version(Jt808ProtocolVersion.VERSION_2013)
                                .terminalId("013912344323")
                                .msgId(BuiltinJt808MsgType.SERVER_TEXT_MESSAGE_DISTRIBUTION)
        );
        assertEquals("7E830000170139123443230000056D657373616765202E2E2E2E2E2E2E2E2E2E206D7367FD7E", hexString);

        // ........
        final String hexString2 = this.encode(
                new BuiltinMsg8300().setFlag((byte) 0b101).setMessage("message .......... msg"),
                builder -> builder.version(Jt808ProtocolVersion.VERSION_2013)
                        .terminalId("013912344323")
                        .msgId(BuiltinJt808MsgType.SERVER_TEXT_MESSAGE_DISTRIBUTION)
        );

        assertEquals(hexString, hexString2);
    }
}