package io.github.hylexus.jt.jt808.samples.mixedversion.entity.req;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestMsgBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import lombok.Data;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

@Data
@Jt808RequestMsgBody
public class TerminalRegisterMsgV2019 {

    // 1. [0-2) WORD 省域ID
    @RequestField(order = 1, startIndex = 0, dataType = WORD)
    private int provinceId;

    // 2. [2-4) WORD 省域ID
    @RequestField(order = 2, startIndex = 2, dataType = WORD)
    private int cityId;

    // 3. [4-15) BYTE[11] 制造商ID
    @RequestField(order = 3, startIndex = 4, dataType = STRING, length = 11)
    private String manufacturerId;

    // 4. [15-45) BYTE[30] 终端型号
    @RequestField(order = 4, startIndex = 15, dataType = STRING, length = 30)
    private String terminalType;

    // 5. [45-75) BYTE[30] 终端型号
    @RequestField(order = 5, startIndex = 45, dataType = STRING, length = 30)
    private String terminalId;

    // 6. [75,76)   BYTE    车牌颜色
    @RequestField(order = 6, startIndex = 75, dataType = BYTE)
    private byte color;

    // 7. [76,n)   String    车辆标识
    @RequestField(order = 7, startIndex = 76, dataType = STRING, lengthExpression = "#header.msgBodyLength() - 76")
    private String carIdentifier;

}
