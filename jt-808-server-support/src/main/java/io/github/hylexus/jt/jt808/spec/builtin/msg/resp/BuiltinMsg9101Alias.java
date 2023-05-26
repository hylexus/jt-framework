package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x9101)
public class BuiltinMsg9101Alias {
    @ResponseFieldAlias.Byte(order = 10)
    private byte serverIpLength;

    @ResponseFieldAlias.String(order = 20)
    private String serverIp;

    @ResponseFieldAlias.Word(order = 30)
    private int serverPortTcp;

    @ResponseFieldAlias.Word(order = 40)
    private int serverPortUdp;

    @ResponseFieldAlias.Byte(order = 50)
    private byte channelNumber;

    @ResponseFieldAlias.Byte(order = 60)
    private byte dataType;

    @ResponseFieldAlias.Byte(order = 70)
    private byte streamType;
}
