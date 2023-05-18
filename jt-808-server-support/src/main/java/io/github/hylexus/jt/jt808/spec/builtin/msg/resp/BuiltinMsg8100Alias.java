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
@Jt808ResponseBody(msgId = 0x8100)
public class BuiltinMsg8100Alias {
    // 1. byte[0,2) WORD 对应的终端注册消息的流水号
    // @ResponseField(order = 0, dataType = MsgDataType.WORD)
    @ResponseFieldAlias.Word(order = 0)
    private int terminalFlowId;
    // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
    // @ResponseField(order = 1, dataType = MsgDataType.BYTE)
    @ResponseFieldAlias.Byte(order = 10)
    private byte result;
    // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
    // @ResponseField(order = 3000, dataType = MsgDataType.STRING, conditionalOn = "result == 0")
    @ResponseFieldAlias.String(order = 3000, conditionalOn = "result == 0")
    private String authCode;
}
