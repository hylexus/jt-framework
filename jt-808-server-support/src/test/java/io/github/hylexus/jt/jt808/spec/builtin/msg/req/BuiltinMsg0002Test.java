package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import org.junit.jupiter.api.Test;

class BuiltinMsg0002Test extends BaseReqRespMsgTest {

    @Test
    void testResp() {
        @Jt808ResponseBody(msgId = -1)
        class Empty {
        }

        final String hex = encode(
                new Empty(),
                builder -> builder.msgId(BuiltinJt808MsgType.CLIENT_HEART_BEAT)
                        .version(Jt808ProtocolVersion.VERSION_2019)
                        .terminalId(terminalId2019)
        );
        System.out.println(hex);
        System.out.println(Empty.class);
        final BuiltinMsg0002 decode = decode(hex, BuiltinMsg0002.class);
        System.out.println(decode);
    }
}