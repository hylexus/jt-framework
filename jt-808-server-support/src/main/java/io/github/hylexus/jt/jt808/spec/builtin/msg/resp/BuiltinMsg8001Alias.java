package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x8001)
public class BuiltinMsg8001Alias {
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
    @ResponseFieldAlias.Word(order = 0)
    int clientFlowId;

    // 2. 应答id WORD     对应的终端消息的 ID
    @ResponseFieldAlias.Word(order = 1)
    int clientMsgType;

    // 3. 结果  byte 0:成功/确认;1:失败;2:消息有误;3:不支持
    @ResponseFieldAlias.Byte(order = 2)
    int result;

    public static BuiltinMsg8001Alias success(int clientMsgType, int clientFlowId) {
        return new BuiltinMsg8001Alias().setResult(RESULT_SUCCESS)
                .setClientMsgType(clientMsgType)
                .setClientFlowId(clientFlowId);
    }
}
