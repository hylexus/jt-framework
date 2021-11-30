package io.github.hylexus.jt.jt808.samples.debug.entity.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.Jt808ResponseMsgBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.basic.BasicField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import lombok.Data;
import lombok.experimental.Accessors;

@Jt808ResponseMsgBody(respMsgId = 0x001)
@Data
@Accessors(chain = true)
public class DemoServerCommonReplyRespMsg {
    @BasicField(order = 1, dataType = MsgDataType.WORD)
    private int flowId;

    @BasicField(order = 1, dataType = MsgDataType.WORD)
    private int msgId;

    @BasicField(order = 3, dataType = MsgDataType.BYTE)
    private int result;
}
