package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

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
@Jt808ResponseBody(msgId = 0x8001)
public class BuiltinServerCommonReplyMsg {
    // 成功/确认
    public static final int RESULT_SUCCESS = 0;
    // 失败
    public static final int RESULT_FAILURE = 1;
    // 消息有误
    public static final int RESULT_MSG_ERROR = 2;
    // 不支持
    public static final int RESULT_UNSUPPORTED = 3;
    // 报警处理确认
    public static final int RESULT_ALARM_MSG_ACK = 4;

    // 1. 应答流水号 WORD    对应的终端消息的流水号
    @ResponseField(order = 0, dataType = WORD)
    int clientFlowId;

    // 2. 应答id WORD     对应的终端消息的 ID
    @ResponseField(order = 1, dataType = WORD)
    int clientMsgType;

    // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
    @ResponseField(order = 2, dataType = BYTE)
    int result;

    public static BuiltinServerCommonReplyMsg success(int clientMsgType, int clientFlowId) {
        return new BuiltinServerCommonReplyMsg().setResult(RESULT_SUCCESS)
                .setClientMsgType(clientMsgType)
                .setClientFlowId(clientFlowId);
    }
}
