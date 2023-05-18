package io.github.hylexus.jt.jt808.samples.debug.entity.req;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import lombok.Data;

@Data
@Jt808RequestBody
public class TerminalCommonReplyMsg {

    // bytes[0,2)   WORD    应答流水号 对应的平台消息的流水号
    @RequestField(order = 1, dataType = MsgDataType.WORD)
    private int serverFlowId;

    // bytes[2,4)   WORD    应答ID  对应的平台消息的 ID
    @RequestField(order = 2, dataType = MsgDataType.WORD)
    private int serverMsgId;

    // byte[4]  BYTE    结果  0:成功/确认;1:失败;2:消息有误;3:不支持
    @RequestField(order = 3, dataType = MsgDataType.BYTE)
    private int result;
}
