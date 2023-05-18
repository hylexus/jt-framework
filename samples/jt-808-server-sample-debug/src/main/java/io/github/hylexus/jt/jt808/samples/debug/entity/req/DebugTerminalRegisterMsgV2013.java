package io.github.hylexus.jt.jt808.samples.debug.entity.req;

import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.jt.jt808.spec.Jt808RequestHeaderAware;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import lombok.Data;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

@Data
@Jt808RequestBody
public class DebugTerminalRegisterMsgV2013 implements Jt808RequestHeaderAware {
    private Jt808RequestHeader header;

    // 1. [0-2) WORD 省域ID
    @RequestField(order = 1, dataType = WORD)
    private int provinceId;

    // 2. [2-4) WORD 省域ID
    @RequestField(order = 2, dataType = WORD)
    private int cityId;

    // 3. [4-9) BYTE[5] 制造商ID
    @RequestField(order = 3, dataType = STRING, length = 5)
    private String manufacturerId;

    // 4. [9-29) BYTE[20] 终端型号
    @RequestField(order = 4, dataType = STRING, length = 20)
    private String terminalType;

    // 5. [29-36) BYTE[7] 终端型号
    @RequestField(order = 5, dataType = STRING, length = 7)
    private String terminalId;

    // 6. [36,37)   BYTE    车牌颜色
    @RequestField(order = 6, dataType = BYTE)
    private byte color;

    // 7. [37,n)   String    车辆标识
    @RequestField(order = 7, dataType = STRING, lengthMethod = "carIdentifierLength")
    private String carIdentifier;

    public int carIdentifierLength() {
        return header.msgBodyLength() - 37;
    }

    @Override
    public void setHeader(Jt808RequestHeader header) {
        this.header = header;
    }
}
