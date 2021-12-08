package io.github.hylexus.jt.jt808.samples.mixedversion.entity.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import lombok.Data;
import lombok.experimental.Accessors;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.BYTE;
import static io.github.hylexus.jt.jt808.support.data.MsgDataType.WORD;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808ResponseBody(respMsgId = 0x8001, desc = "平台通用应答")
public class ServerCommonReplyMsg {
    // 1. 应答流水号 WORD    对应的终端消息的流水号
    @ResponseField(order = 0, dataType = WORD)
    int replyFlowId;
    // 2. 应答id WORD     对应的终端消息的 ID
    @ResponseField(order = 1, dataType = WORD)
    int replyMsgId;
    // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
    @ResponseField(order = 2, dataType = BYTE)
    int result;
}