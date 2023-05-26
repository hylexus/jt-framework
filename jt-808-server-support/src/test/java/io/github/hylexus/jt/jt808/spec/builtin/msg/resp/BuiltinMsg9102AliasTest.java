package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BuiltinMsg9102AliasTest extends BaseReqRespMsgTest {

    @Test
    void test() {

        final BuiltinMsg9102Alias msg = new BuiltinMsg9102Alias()
                .setChannelNumber((byte) 3)
                .setCommand((byte) 0)
                .setMediaTypeToClose((byte) 0)
                .setStreamType((byte) 0);

        final String hexString = this.encode(msg, builder ->
                builder.version(Jt808ProtocolVersion.VERSION_2013)
                        .terminalId("013912344323")
                        .msgId(BuiltinJt808MsgType.SERVER_RTM_CONTROL)
        );
        Assertions.assertEquals("7E91020004013912344323000003000000EA7E", hexString);
    }

}