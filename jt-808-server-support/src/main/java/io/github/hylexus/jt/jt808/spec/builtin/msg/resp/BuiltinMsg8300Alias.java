package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x8300)
public class BuiltinMsg8300Alias {

    @ResponseFieldAlias.Byte(order = 0)
    private byte flag;

    @ResponseFieldAlias.String(order = 1)
    private String message;
}
