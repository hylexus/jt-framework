package io.github.hylexus.jt.jt808.samples.debug.entity.req;

import io.github.hylexus.jt.jt808.spec.Jt808MsgHeader;
import io.github.hylexus.jt.jt808.support.annotation.msg.Jt808RequestMsgBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.basic.BasicField;
import io.github.hylexus.jt.jt808.support.data.Jt808HeaderSpecAware;
import lombok.Data;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

@Data
@Jt808RequestMsgBody
public class DebugTerminalRegisterMsgV2011 implements Jt808HeaderSpecAware {
    private Jt808MsgHeader header;

    // 1. [0-2) WORD 省域ID
    @BasicField(order = 1, startIndex = 0, dataType = WORD)
    private int provinceId;

    // 2. [2-4) WORD 省域ID
    @BasicField(order = 2, startIndex = 2, dataType = WORD)
    private int cityId;

    // 3. [4-9) BYTE[5] 制造商ID
    @BasicField(order = 3, startIndex = 4, dataType = STRING, length = 5)
    private String manufacturerId;

    // 4. [9-29) BYTE[20] 终端型号
    @BasicField(order = 4, startIndex = 9, dataType = STRING, length = 20)
    private String terminalType;

    // 5. [29-36) BYTE[7] 终端型号
    @BasicField(order = 5, startIndex = 29, dataType = STRING, length = 7)
    private String terminalId;

    // 6. [36,37)   BYTE    车牌颜色
    @BasicField(order = 6, startIndex = 36, dataType = BYTE)
    private byte color;

    // 7. [37,n)   String    车辆标识
    @BasicField(order = 7, startIndex = 37, dataType = STRING, lengthMethod = "carIdentifierLength")
    private String carIdentifier;

    public int carIdentifierLength() {
        return header.msgBodyLength() - 37;
    }

    @Override
    public void setHeader(Jt808MsgHeader header) {
        this.header = header;
    }
}
