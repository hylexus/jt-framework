package io.github.hylexus.jt.jt808.adapter.xtreamcodec.entity;

import io.github.hylexus.jt.jt808.support.annotation.msg.DrivenBy;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

// 默认: drivenBy = @DrivenBy(DrivenBy.Type.DEFAULT)
@Jt808RequestBody(drivenBy = @DrivenBy(DrivenBy.Type.DEFAULT))
public class Message0100JtFramework {
    // 1. [0-2) WORD 省域ID
    // WORD 类型固定长度就是2字节 所以无需指定length
    @RequestField(order = 1, dataType = WORD)
    public int provinceId;

    // 2. [2-4) WORD 省域ID
    @RequestField(order = 2, dataType = WORD)
    public int cityId;

    // 3. [4-15) BYTE[11] 制造商ID
    @RequestField(order = 3, dataType = BYTES, length = 11)
    public String manufacturerId;

    // 4. [15-45) BYTE[30] 终端型号
    @RequestField(order = 4, dataType = BYTES, length = 30)
    public String terminalType;

    // 5. [45-75) BYTE[30] 终端ID
    @RequestField(order = 5, dataType = BYTES, length = 30)
    public String terminalId;

    // 6. [75]   BYTE    车牌颜色
    @RequestField(order = 6, dataType = BYTE)
    public byte color;

    // 7. [76,n)   String    车辆标识
    // 使用 SpEL 计算消息长度(上下文中的消息体总长度减去前面消费掉的字节数)
    @RequestField(order = 7, dataType = STRING, lengthExpression = "#ctx.msgBodyLength() - 76")
    public String carIdentifier;
}
