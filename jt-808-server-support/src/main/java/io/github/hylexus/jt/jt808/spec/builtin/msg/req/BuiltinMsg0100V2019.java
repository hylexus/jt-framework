package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import lombok.Data;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

/**
 * 内置终端注册消息体(2019)
 *
 * @author hylexus
 */
@Data
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0100V2019 {
    // 1. [0-2) WORD 省域ID
    // WORD 类型固定长度就是2字节 所以无需指定length
    @RequestField(order = 1, dataType = WORD)
    private int provinceId;

    // 2. [2-4) WORD 省域ID
    @RequestField(order = 2, dataType = WORD)
    private int cityId;

    // 3. [4-15) BYTE[11] 制造商ID
    @RequestField(order = 3, dataType = BYTES, length = 11)
    private String manufacturerId;

    // 4. [15-45) BYTE[30] 终端型号
    @RequestField(order = 4, dataType = BYTES, length = 30)
    private String terminalType;

    // 5. [45-75) BYTE[30] 终端ID
    @RequestField(order = 5, dataType = BYTES, length = 30)
    private String terminalId;

    // 6. [75]   BYTE    车牌颜色
    @RequestField(order = 6, dataType = BYTE)
    private byte color;

    // 7. [76,n)   String    车辆标识
    // 使用 SpEL 计算消息长度(上下文中的消息体总长度减去前面消费掉的字节数)
    @RequestField(order = 7, dataType = STRING, lengthExpression = "#ctx.msgBodyLength() - 76")
    private String carIdentifier;
}
