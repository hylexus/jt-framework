package io.github.hylexus.jt808.samples.client.debug.client1.entity;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import lombok.Data;
import lombok.experimental.Accessors;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x0100)
public class ClientRegisterMsgV2013 {
    // 1. [0-2) WORD 省域ID
    @ResponseField(order = 1, dataType = WORD)
    private int provinceId;

    // 2. [2-4) WORD 省域ID
    @ResponseField(order = 2, dataType = WORD)
    private int cityId;

    // 3. [4-9) BYTE[5] 制造商ID
    @ResponseField(order = 3, dataType = STRING)
    private String manufacturerId;

    // 4. [9-29) BYTE[20] 终端型号
    @ResponseField(order = 4, dataType = STRING)
    private String terminalType;

    // 5. [29-36) BYTE[7] 终端ID
    @ResponseField(order = 5, dataType = STRING)
    private String terminalId;

    // 6. [36]   BYTE    车牌颜色
    @ResponseField(order = 6, dataType = BYTE)
    private byte color;

    // 7. [37,n)   String    车辆标识
    @ResponseField(order = 7, dataType = STRING)
    private String carIdentifier;
}