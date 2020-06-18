package io.github.hylexus.jt808.samples.annotation.entity.resp;

import io.github.hylexus.jt.annotation.msg.resp.CommandField;
import io.github.hylexus.jt.annotation.msg.resp.Jt808RespMsgBody;
import lombok.Data;

import static io.github.hylexus.jt.data.MsgDataType.*;

/**
 * Created At 2020-06-12 17:30
 *
 * @author hylexus
 */
@Data
@Jt808RespMsgBody(respMsgId = 0x8100)
public class RegisterReplyMsgBody {
    @CommandField(order = 0, targetMsgDataType = WORD)
    private int flowId;

    @CommandField(order = 1, targetMsgDataType = BYTE)
    private byte result;

    @CommandField(order = 2, targetMsgDataType = STRING)
    private String authCode;

    public RegisterReplyMsgBody() {
    }

    public RegisterReplyMsgBody(int flowId, byte result, String authCode) {
        this.flowId = flowId;
        this.result = result;
        this.authCode = authCode;
    }
}
