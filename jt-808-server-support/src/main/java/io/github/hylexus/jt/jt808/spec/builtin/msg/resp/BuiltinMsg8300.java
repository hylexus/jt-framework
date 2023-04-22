package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x8300, maxPackageSize = 1024)
public class BuiltinMsg8300 {

    @ResponseField(order = 0, dataType = MsgDataType.BYTE)
    private byte flag;

    @ResponseField(order = 1, dataType = MsgDataType.STRING)
    private String message;
}
