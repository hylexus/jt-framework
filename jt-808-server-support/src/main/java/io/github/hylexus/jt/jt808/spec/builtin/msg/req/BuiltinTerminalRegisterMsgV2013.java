package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import lombok.Data;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

/**
 * 内置终端注册消息体(2013)
 *
 * @author hylexus
 */
@Data
@Jt808RequestBody
public class BuiltinTerminalRegisterMsgV2013 {
    // 1. [0-2) WORD 省域ID
    @RequestField(order = 1, startIndex = 0, dataType = WORD)
    private int provinceId;

    // 2. [2-4) WORD 省域ID
    @RequestField(order = 2, startIndex = 2, dataType = WORD)
    private int cityId;

    // 3. [4-9) BYTE[5] 制造商ID
    @RequestField(order = 3, startIndex = 4, dataType = STRING, length = 5)
    private String manufacturerId;

    // 4. [9-29) BYTE[20] 终端型号
    @RequestField(order = 4, startIndex = 9, dataType = STRING, length = 20)
    private String terminalType;

    // 5. [29-36) BYTE[7] 终端型号
    @RequestField(order = 5, startIndex = 17, dataType = STRING, length = 7)
    private String terminalId;

    // 6. [36]   BYTE    车牌颜色
    @RequestField(order = 6, startIndex = 36, dataType = BYTE)
    private byte color;

    // 7. [37,n)   String    车辆标识
    @RequestField(order = 7, startIndex = 25, dataType = STRING, lengthExpression = "#header.msgBodyLength() - 37")
    private String carIdentifier;
}
