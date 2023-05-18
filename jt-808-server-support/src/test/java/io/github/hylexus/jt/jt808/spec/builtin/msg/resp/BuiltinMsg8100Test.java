package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BuiltinMsg8100Test extends BaseReqRespMsgTest {

    @Test
    void test() {
        final BuiltinMsg8100Alias msg = new BuiltinMsg8100Alias()
                .setTerminalFlowId(111)
                .setResult((byte) 0)
                .setAuthCode("Code-123456!!!哈哈");

        final String hexString = encode(
                msg,
                builder -> builder.version(Jt808ProtocolVersion.VERSION_2013)
                        .terminalId(terminalId2013)
                        .msgId(BuiltinJt808MsgType.CLIENT_REGISTER_REPLY)
        );
        Assertions.assertEquals("7E810000150139123443230000006F00436F64652D313233343536212121B9FEB9FEA37E", hexString);
    }

    @Test
    void test1() {
        final BuiltinMsg8100Alias msg = new BuiltinMsg8100Alias()
                .setTerminalFlowId(111)
                .setResult((byte) 1)
                .setAuthCode("Code-123456!!!哈哈");

        final String hexString = encode(
                msg,
                builder -> builder.version(Jt808ProtocolVersion.VERSION_2013)
                        .terminalId(terminalId2013)
                        .msgId(BuiltinJt808MsgType.CLIENT_REGISTER_REPLY)
        );
        Assertions.assertEquals("7E810000030139123443230000006F01927E", hexString);
    }

    @Test
    void test2() {
        final BuiltinMsg8100 msg = new BuiltinMsg8100()
                .setTerminalFlowId(111)
                .setResult((byte) 0)
                .setAuthCode("Code-123456!!!哈哈");

        final String hexString = encode(
                msg,
                builder -> builder.version(Jt808ProtocolVersion.VERSION_2013)
                        .terminalId(terminalId2013)
                        .msgId(BuiltinJt808MsgType.CLIENT_REGISTER_REPLY)
        );
        Assertions.assertEquals("7E810000150139123443230000006F00436F64652D313233343536212121B9FEB9FEA37E", hexString);
    }
}