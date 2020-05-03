package io.github.hylexus.jt808.samples.annotation.entity.resp;

import io.github.hylexus.jt.annotation.msg.resp.CommandField;
import io.github.hylexus.jt.annotation.msg.resp.Jt808RespMsgBody;
import lombok.Value;

import static io.github.hylexus.jt.data.MsgDataType.BYTE;
import static io.github.hylexus.jt.data.MsgDataType.WORD;

/**
 * @author hylexus
 * Created At 2020-05-03 19:08
 */
@Value
// MsgId 0x8001
@Jt808RespMsgBody(respMsgId = 0x8001)
public class ServerCommonReplyMsgBody {
    // 1. 应答流水号 WORD terminal flowId
    @CommandField(order = 0, targetMsgDataType = WORD)
    int replyFlowId;
    // 2. 应答id WORD 0x0102 ...
    @CommandField(order = 1, targetMsgDataType = WORD)
    int replyMsgId;
    // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
    @CommandField(order = 2, targetMsgDataType = BYTE)
    byte result;
}
