package io.github.hylexus.jt808.msg.resp;

import io.github.hylexus.jt.annotation.msg.resp.CommandField;
import io.github.hylexus.jt.annotation.msg.resp.Jt808RespMsgBody;
import lombok.Value;

import static io.github.hylexus.jt.data.MsgDataType.BYTE;
import static io.github.hylexus.jt.data.MsgDataType.WORD;

/**
 * @author hylexus
 */
@Value
@Jt808RespMsgBody(respMsgId = 0x8001)
public class BuiltinServerCommonReplyRespMsgBody {
    // 1. 应答流水号 WORD terminal flowId
    @CommandField(order = 0, targetMsgDataType = WORD)
    int replyFlowId;
    // 2. 应答id WORD 0x0102 ...
    @CommandField(order = 1, targetMsgDataType = WORD)
    int replyMsgId;
    // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
    @CommandField(order = 2, targetMsgDataType = BYTE)
    byte result;

    /**
     * 成功/确认
     */
    public static final byte RESULT_SUCCESS = 0;
    /**
     * 失败
     */
    public static final byte RESULT_FAILURE = 1;
    /**
     * 消息有误
     */
    public static final byte RESULT_MSG_ERROR = 2;
    /**
     * 不支持
     */
    public static final byte RESULT_UNSUPPORTED = 3;
}
