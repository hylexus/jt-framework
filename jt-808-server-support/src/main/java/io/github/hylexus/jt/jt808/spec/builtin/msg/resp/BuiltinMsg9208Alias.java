package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.spec.builtin.msg.extension.location.AlarmIdentifierAlias;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x9208)
public class BuiltinMsg9208Alias {

    // BYTE
    @ResponseFieldAlias.Byte(order = 10)
    private short attachmentServerIpLength;

    // STRING
    @ResponseFieldAlias.String(order = 20)
    private String attachmentServerIp;

    // WORD
    @ResponseFieldAlias.Word(order = 30)
    private int attachmentServerPortTcp;

    // WORD
    @ResponseFieldAlias.Word(order = 40)
    private int attachmentServerPortUdp;

    // BYTE[16]
    @ResponseFieldAlias.Object(order = 50)
    private AlarmIdentifierAlias alarmIdentifier;

    // BYTE[32]
    @ResponseFieldAlias.Bytes(order = 60)
    private String alarmNo;

    // BYTE[16]
    @ResponseFieldAlias.Bytes(order = 70)
    private String reservedByte16 = "0000000000000000";
}
