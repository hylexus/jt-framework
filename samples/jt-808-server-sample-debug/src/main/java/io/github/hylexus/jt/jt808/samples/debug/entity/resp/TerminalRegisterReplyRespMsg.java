package io.github.hylexus.jt.jt808.samples.debug.entity.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.Jt808ResponseMsgBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.basic.BasicField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808ResponseMsgBody(respMsgId = 0x8100)
public class TerminalRegisterReplyRespMsg {

    // 1. byte[0,2) WORD 对应的终端注册消息的流水号
    @BasicField(order = 0, dataType = MsgDataType.WORD)
    private int flowId;
    // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
    @BasicField(order = 1, dataType = MsgDataType.BYTE)
    private byte result;
    // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
    @BasicField(order = 3, dataType = MsgDataType.STRING)
    private String authCode;
}
