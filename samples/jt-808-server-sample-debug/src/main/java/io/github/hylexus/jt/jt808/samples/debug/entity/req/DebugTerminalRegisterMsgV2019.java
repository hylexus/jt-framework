package io.github.hylexus.jt.jt808.samples.debug.entity.req;

import io.github.hylexus.jt.jt808.spec.Jt808MsgHeader;
import io.github.hylexus.jt.jt808.support.annotation.msg.Jt808RequestMsgBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.basic.BasicField;
import io.github.hylexus.jt.jt808.support.data.Jt808HeaderSpecAware;
import lombok.Data;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

@Data
@Jt808RequestMsgBody
public class DebugTerminalRegisterMsgV2019 implements Jt808HeaderSpecAware {
    private Jt808MsgHeader header;

    // 1. [0-2) WORD 省域ID
    @BasicField(order = 1, startIndex = 0, dataType = WORD)
    private int provinceId;

    // 2. [2-4) WORD 省域ID
    @BasicField(order = 2, startIndex = 2, dataType = WORD)
    private int cityId;

    // 3. [4-15) BYTE[11] 制造商ID
    @BasicField(order = 3, startIndex = 4, dataType = STRING, length = 11)
    private String manufacturerId;

    // 4. [15-45) BYTE[30] 终端型号
    @BasicField(order = 4, startIndex = 15, dataType = STRING, length = 30)
    private String terminalType;

    // 5. [45-75) BYTE[30] 终端型号
    @BasicField(order = 5, startIndex = 45, dataType = STRING, length = 30)
    private String terminalId;

    // 6. [75,76)   BYTE    车牌颜色
    @BasicField(order = 6, startIndex = 75, dataType = BYTE)
    private byte color;

    // 7. [76,n)   String    车辆标识
    @BasicField(order = 7, startIndex = 76, dataType = STRING, lengthMethod = "carIdentifierLength")
    private String carIdentifier;

    public int carIdentifierLength() {
        return header.msgBodyLength() - 76;
    }

    @Override
    public void setHeader(Jt808MsgHeader header) {
        this.header = header;
    }
}
