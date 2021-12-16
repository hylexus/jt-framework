package io.github.hylexus.jt.jt808.samples.debug.entity.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import lombok.Data;
import lombok.experimental.Accessors;

@Jt808ResponseBody(msgId = 0x001)
@Data
@Accessors(chain = true)
public class DemoServerCommonReplyRespMsg {
    @ResponseField(order = 1, dataType = MsgDataType.WORD)
    private int flowId;

    @ResponseField(order = 1, dataType = MsgDataType.WORD)
    private int msgId;

    @ResponseField(order = 3, dataType = MsgDataType.BYTE)
    private int result;
}
