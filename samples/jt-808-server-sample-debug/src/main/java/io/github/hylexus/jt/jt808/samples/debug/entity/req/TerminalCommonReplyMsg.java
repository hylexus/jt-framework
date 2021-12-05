package io.github.hylexus.jt.jt808.samples.debug.entity.req;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestMsgBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import lombok.Data;

@Data
@Jt808RequestMsgBody(msgType = 0x0001)
public class TerminalCommonReplyMsg {

    // bytes[0,2)   WORD    应答流水号 对应的平台消息的流水号
    @RequestField(order = 1, startIndex = 0, dataType = MsgDataType.WORD)
    private int flowId;

    // bytes[2,4)   WORD    应答ID  对应的平台消息的 ID
    @RequestField(order = 2, startIndex = 1, dataType = MsgDataType.WORD)
    private int replyId;

    // byte[5]  BYTE    结果  0:成功/确认;1:失败;2:消息有误;3:不支持
    @RequestField(order = 3, dataType = MsgDataType.BYTE)
    private byte result;
}
