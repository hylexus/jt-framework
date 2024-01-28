package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import io.github.hylexus.jt.jt808.spec.builtin.msg.extension.location.AlarmIdentifierAlias;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class BuiltinMsg9208AliasTest extends BaseReqRespMsgTest {
    @Test
    void test() {

        final String serverIp = "192.168.71.119";
        final BuiltinMsg9208Alias msg = new BuiltinMsg9208Alias()
                .setAttachmentServerIpLength((short) serverIp.getBytes(StandardCharsets.UTF_8).length)
                .setAttachmentServerIp(serverIp)
                .setAttachmentServerPortTcp(6808)
                .setAttachmentServerPortUdp(0)
                .setAlarmIdentifier(new AlarmIdentifierAlias()
                        .setTerminalId("id00003")
                        .setTime(LocalDateTime.of(2024, 1, 1, 12, 12, 12))
                        .setSequence((short) 0)
                        .setAttachmentCount((short) 4)
                )
                .setAlarmNo("lbt9kpl93jlq6krtlvwqi8b7bmpt9111");

        final String hexString = this.encode(msg, builder ->
                builder.version(Jt808ProtocolVersion.VERSION_2019)
                        .terminalId("00000000014499999999")
                        .msgId(BuiltinJt808MsgType.SERVER_MSG_9208)
        );
        Assertions.assertEquals("7E92084053010000000001449999999900000E3139322E3136382E37312E3131391A980000696430303030332401011212120004006C6274396B706C39336A6C71366B72746C76777169386237626D70743931313130303030303030303030303030303030507E", hexString);
    }
}
