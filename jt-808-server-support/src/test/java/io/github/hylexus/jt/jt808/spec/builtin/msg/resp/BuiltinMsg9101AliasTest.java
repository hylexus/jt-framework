package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.JtProtocolConstant;
import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BuiltinMsg9101AliasTest extends BaseReqRespMsgTest {
    @Test
    void test() {

        final BuiltinMsg9101Alias msg = new BuiltinMsg9101Alias().setServerIp("127.0.0.1")
                .setServerPortTcp(1078)
                .setServerPortUdp(1078)
                .setChannelNumber((byte) 3)
                .setDataType((byte) 0)
                .setStreamType((byte) 0);
        msg.setServerIpLength((byte) msg.getServerIp().getBytes(JtProtocolConstant.JT_808_STRING_ENCODING).length);

        final String hexString = this.encode(msg, builder ->
                builder.version(Jt808ProtocolVersion.VERSION_2013)
                        .terminalId("013912344323")
                        .msgId(BuiltinJt808MsgType.SERVER_RTM_REQUEST)
        );
        Assertions.assertEquals("7E910100110139123443230000093132372E302E302E3104360436030000DE7E", hexString);
    }

}