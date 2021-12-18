package io.github.hylexus.jt.jt808.samples.mixedversion.entity.req;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import lombok.Data;
import lombok.experimental.Accessors;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.STRING;

@Data
@Accessors(chain = true)
@Jt808RequestBody
public class TerminalAuthMsgV2011 {

    @RequestField(order = 1, startIndex = 0, dataType = STRING, lengthExpression = "#ctx.msgBodyLength()")
    private String authCode;

}
