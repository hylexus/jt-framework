package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import lombok.Data;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

/**
 * 内置终端注册消息体(2011)
 *
 * @author hylexus
 */
@Data
@Jt808RequestBody
@BuiltinComponent
public class BuiltinTerminalRegisterMsgV2011 {
    // 1. [0-2) WORD 省域ID
    @RequestField(order = 1, startIndex = 0, dataType = WORD)
    private int provinceId;

    // 2. [2-4) WORD 省域ID
    @RequestField(order = 2, startIndex = 2, dataType = WORD)
    private int cityId;

    // 3. [4-9) BYTE[5] 制造商ID
    @RequestField(order = 3, startIndex = 4, dataType = STRING, length = 5)
    private String manufacturerId;

    // 4. [9-17) BYTE[8] 终端型号
    @RequestField(order = 4, startIndex = 9, dataType = STRING, length = 8)
    private String terminalType;

    // 5. [17-24) BYTE[7] 终端型号
    @RequestField(order = 5, startIndex = 17, dataType = STRING, length = 7)
    private String terminalId;

    // 6. [24]   BYTE    车牌颜色
    @RequestField(order = 6, startIndex = 24, dataType = BYTE)
    private byte color;

    // 7. [25,n)   String    车辆标识
    @RequestField(order = 7, startIndex = 25, dataType = STRING, lengthExpression = "#header.msgBodyLength() - 25")
    private String carIdentifier;
}
