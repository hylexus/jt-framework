package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import lombok.Data;

/**
 * 内置终端注册消息体(2013)
 *
 * @author hylexus
 */
@Data
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0100V2013Alias {
    // 1. [0-2) WORD 省域ID
    @RequestFieldAlias.Word(order = 1)
    private int provinceId;

    // 2. [2-4) WORD 市县域ID
    @RequestFieldAlias.Word(order = 2)
    private int cityId;

    // 3. [4-9) BYTE[5] 制造商ID
    @RequestFieldAlias.Bytes(order = 3, length = 5)
    private String manufacturerId;

    // 4. [9-29) BYTE[20] 终端型号
    @RequestFieldAlias.Bytes(order = 4, length = 20)
    private String terminalType;

    // 5. [29-36) BYTE[7] 终端ID
    @RequestFieldAlias.Bytes(order = 5, length = 7)
    private String terminalId;

    // 6. [36]   BYTE    车牌颜色
    @RequestFieldAlias.Byte(order = 6)
    private byte color;

    // 7. [37,n)   String    车辆标识
    @RequestFieldAlias.String(order = 7, lengthExpression = "#ctx.msgBodyLength() - 37")
    private String carIdentifier;
}
