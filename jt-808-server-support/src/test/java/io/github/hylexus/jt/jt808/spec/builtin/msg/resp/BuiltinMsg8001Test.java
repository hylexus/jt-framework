package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BuiltinMsg8001Test extends BaseReqRespMsgTest {

    @Test
    void test() {
        final BuiltinMsg8001 msg = new BuiltinMsg8001()
                .setResult(101)
                .setClientFlowId(222)
                .setResult(BuiltinMsg8001.RESULT_ALARM_MSG_ACK);

        final String hex = encode(msg, builder -> builder.msgId(BuiltinJt808MsgType.SERVER_COMMON_REPLY)
                .terminalId(terminalId2013)
                .version(Jt808ProtocolVersion.VERSION_2013));
        Assertions.assertEquals("7E80010005013912344323000000DE000004207E", hex);
    }

    @Test
    void testAlias() {
        final BuiltinMsg8001Alias msg = new BuiltinMsg8001Alias()
                .setResult(101)
                .setClientFlowId(222)
                .setResult(BuiltinMsg8001.RESULT_ALARM_MSG_ACK);

        final String hex = encode(msg, builder -> builder.msgId(BuiltinJt808MsgType.SERVER_COMMON_REPLY)
                .terminalId(terminalId2013)
                .version(Jt808ProtocolVersion.VERSION_2013));
        Assertions.assertEquals("7E80010005013912344323000000DE000004207E", hex);
    }
}