package io.github.hylexus.jt.jt808.samples.debug.entity.req;

import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.jt.jt808.spec.Jt808RequestHeaderAware;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import lombok.Data;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

@Data
@Jt808RequestBody
public class DebugTerminalRegisterMsgV2019 implements Jt808RequestHeaderAware {
    private Jt808RequestHeader header;

    // 1. [0-2) WORD 省域ID
    @RequestField(order = 1, dataType = WORD)
    private int provinceId;

    // 2. [2-4) WORD 省域ID
    @RequestField(order = 2, dataType = WORD)
    private int cityId;

    // 3. [4-15) BYTE[11] 制造商ID
    @RequestField(order = 3, dataType = STRING, length = 11)
    private String manufacturerId;

    // 4. [15-45) BYTE[30] 终端型号
    @RequestField(order = 4, dataType = STRING, length = 30)
    private String terminalType;

    // 5. [45-75) BYTE[30] 终端型号
    @RequestField(order = 5, dataType = STRING, length = 30)
    private String terminalId;

    // 6. [75,76)   BYTE    车牌颜色
    @RequestField(order = 6, dataType = BYTE)
    private byte color;

    // 7. [76,n)   String    车辆标识
    @RequestField(order = 7, dataType = STRING, lengthMethod = "carIdentifierLength")
    private String carIdentifier;

    public int carIdentifierLength() {
        return header.msgBodyLength() - 76;
    }

    @Override
    public void setHeader(Jt808RequestHeader header) {
        this.header = header;
    }
}
