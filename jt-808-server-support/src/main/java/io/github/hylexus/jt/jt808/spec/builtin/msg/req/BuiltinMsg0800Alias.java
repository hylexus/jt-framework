package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0800Alias {

    @RequestFieldAlias.Dword(order = 10)
    private long multiMediaDataId;

    @RequestFieldAlias.Byte(order = 20)
    private byte multiMediaType;

    @RequestFieldAlias.Byte(order = 30)
    private byte multiMediaFormat;
    /**
     * 事件项编码
     */
    @RequestFieldAlias.Byte(order = 40)
    private byte itemNo;

    @RequestFieldAlias.Byte(order = 50)
    private int channelNumber;

}
