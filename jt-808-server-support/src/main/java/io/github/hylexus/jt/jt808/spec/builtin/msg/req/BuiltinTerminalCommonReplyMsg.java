package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.github.hylexus.jt.utils.FormatUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.BYTE;
import static io.github.hylexus.jt.jt808.support.data.MsgDataType.WORD;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinTerminalCommonReplyMsg {
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

    // 1. 应答流水号 WORD    对应的平台消息的流水号
    @RequestField(order = 0, dataType = WORD)
    int serverFlowId;

    // 2. 应答id WORD     对应的平台消息的 ID
    @RequestField(order = 1, dataType = WORD)
    int serverMsgId;

    // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
    @RequestField(order = 2, dataType = BYTE)
    int result;

    public static BuiltinTerminalCommonReplyMsg success(int serverMsgId, int serverFlowId) {
        return new BuiltinTerminalCommonReplyMsg().setServerMsgId(serverMsgId).setServerFlowId(serverFlowId).setResult(RESULT_SUCCESS);
    }

    @Override
    public String toString() {
        return "BuiltinTerminalCommonReplyMsg{"
                + "serverFlowId=" + serverFlowId
                + ", serverMsgId=" + serverMsgId + "(0x" + FormatUtils.toHexString(serverMsgId) + ")"
                + ", result=" + result
                + '}';
    }
}
