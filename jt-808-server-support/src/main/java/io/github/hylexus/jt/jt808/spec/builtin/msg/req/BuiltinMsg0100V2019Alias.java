package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import lombok.Data;

/**
 * 内置终端注册消息体(2019)
 *
 * @author hylexus
 */
@Data
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0100V2019Alias {
    // 1. [0-2) WORD 省域ID
    // WORD 类型固定长度就是2字节 所以无需指定length
    @RequestFieldAlias.Word(order = 1)
    private int provinceId;

    // 2. [2-4) WORD 市县域ID
    @RequestFieldAlias.Word(order = 2)
    private int cityId;

    // 3. [4-15) BYTE[11] 制造商ID
    @RequestFieldAlias.Bytes(order = 3, length = 11)
    private String manufacturerId;

    // 4. [15-45) BYTE[30] 终端型号
    @RequestFieldAlias.Bytes(order = 4, length = 30)
    private String terminalType;

    // 5. [45-75) BYTE[30] 终端ID
    @RequestFieldAlias.Bytes(order = 5, length = 30)
    private String terminalId;

    // 6. [75]   BYTE    车牌颜色
    @RequestFieldAlias.Byte(order = 6)
    private byte color;

    // 7. [76,n)   String    车辆标识
    // 使用 SpEL 计算消息长度(上下文中的消息体总长度减去前面消费掉的字节数)
    @RequestFieldAlias.String(order = 7, lengthExpression = "#ctx.msgBodyLength() - 76")
    private String carIdentifier;
}
