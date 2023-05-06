package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import lombok.Data;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.STRING;

/**
 * 内置的终端鉴权消息体(V2011)
 *
 * @author hylexus
 */
@Data
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0102V2011 {

    @RequestField(order = 1, dataType = STRING, lengthExpression = "#ctx.msgBodyLength()")
    private String authCode;

}
