package io.github.hylexus.jt808.samples.annotation.entity.req;

import io.github.hylexus.jt.annotation.msg.req.Jt808ReqMsgBody;
import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * @date 2020/12/5 9:32 下午
 */
@Data
@Accessors(chain = true)
@Jt808ReqMsgBody(msgType = 0x0107)
public class Msg0107 implements RequestMsgBody {

    @BasicField(startIndex = 0, dataType = MsgDataType.WORD)
    private int terminalType;

    @BasicField(startIndex = 2, dataType = MsgDataType.BYTES, length = 5)
    private byte[] manufacturerId;
}
