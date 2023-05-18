package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import lombok.Data;

/**
 * 内置终端注册消息体(2011)
 *
 * @author hylexus
 */
@Data
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0100V2011Alias {
    // 1. [0-2) WORD 省域ID
    @RequestFieldAlias.Word(order = 1)
    private int provinceId;

    // 2. [2-4) WORD 省域ID
    @RequestFieldAlias.Word(order = 2)
    private int cityId;

    // 3. [4-9) BYTE[5] 制造商ID
    @RequestFieldAlias.Bytes(order = 3, length = 5)
    private String manufacturerId;

    // 4. [9-17) BYTE[8] 终端型号
    @RequestFieldAlias.Bytes(order = 4, length = 8)
    private String terminalType;

    // 5. [17-24) BYTE[7] 终端ID
    @RequestFieldAlias.Bytes(order = 5, length = 7)
    private String terminalId;

    // 6. [24]   BYTE    车牌颜色
    @RequestFieldAlias.Byte(order = 6)
    private byte color;

    // 7. [25,n)   String    车辆标识
    @RequestFieldAlias.String(order = 7, lengthExpression = "#ctx.msgBodyLength() - 25")
    private String carIdentifier;
    // private ByteArrayContainer carIdentifier;
    // private ByteBufContainer carIdentifier;
}
