package io.github.hylexus.jt.jt808.msg.resp;


import io.github.hylexus.jt.jt808.msg.RespMsgBody;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
@Data
@Accessors(chain = true)
public class CommonReplyMsgBody implements RespMsgBody {

    // 1. 应答流水号 WORD terminal flowId
    private int replyFlowId;
    // 2. 应答id WORD 0x0102 ...
    private int replyMsgId;
    // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
    private byte result = SUCCESS;

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }
}
