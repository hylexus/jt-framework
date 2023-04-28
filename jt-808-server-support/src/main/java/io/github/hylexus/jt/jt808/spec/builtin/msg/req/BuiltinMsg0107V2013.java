package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0107V2013 {

    // bit0，0:不适用客运车辆，1:适用客运车辆;
    // bit1，0:不适用危险品车辆，1:适用危险品车辆;
    // bit2，0:不适用普通货运车辆，1:适用普通货运车辆;
    // bit3，0:不适用出租车辆，1:适用出租车辆;
    // bit6，0:不支持硬盘录像，1:支持硬盘录像;
    // bit7，0:一体机，1:分体机。
    @RequestFieldAlias.Word(order = 10, desc = "终端类型")
    private int terminalType;

    // BYTE[5]
    @RequestFieldAlias.Bytes(order = 20, length = 5, desc = "终端制造商编码")
    private String manufacturerId;

    // BYTE[20]
    @RequestFieldAlias.Bytes(order = 30, length = 20, desc = "终端型号")
    private String terminalModel;

    // BYTE[7]
    @RequestFieldAlias.Bytes(order = 40, length = 7, desc = "终端 ID")
    private String terminalId;

    @RequestFieldAlias.Bcd(order = 50, length = 10, desc = "终端 SIM 卡 ICCID")
    private String sim;

    @RequestFieldAlias.Byte(order = 60, desc = "终端硬件版本号长度")
    private int hardwareVersionLength;

    @RequestFieldAlias.String(order = 70, lengthExpression = "#this.getHardwareVersionLength()", desc = "终端硬件版本号")
    private String hardwareVersion;

    @RequestFieldAlias.Byte(order = 80, desc = "终端固件版本号长度")
    private int firmwareVersionLength;

    @RequestFieldAlias.String(order = 90, lengthExpression = "#this.getFirmwareVersionLength()", desc = "终端固件版本号")
    private String firmwareVersion;

    @RequestFieldAlias.Byte(order = 100, desc = "GNSS 模块属性")
    private byte gnssModuleProps;

    @RequestFieldAlias.Byte(order = 110, desc = "通信模块属性")
    private byte communicationModuleProps;
}
