package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.spec.builtin.msg.extension.location.BuiltinMsg64Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.extension.location.BuiltinMsg65Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.extension.location.BuiltinMsg66Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.extension.location.BuiltinMsg67Alias;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.SlicedFrom;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.extensions.ValueDescriptor;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.extensions.KeyValueMapping;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.type.byteseq.ByteArrayContainer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0200V2013AliasV2 {
    // (1). byte[0,4)  DWORD 报警标志
    @RequestFieldAlias.Dword(order = 1)
    private long alarmFlag;

    // (2). byte[4,8) DWORD 状态
    @RequestFieldAlias.Dword(order = 2)
    private long status;

    // 将上面的 status 字段的第0位取出转为 int 类型
    @SlicedFrom(sourceFieldName = "status", bitIndex = 0)
    private int accIntStatus;
    // 将上面的 status 字段的第0位取出转为 boolean 类型
    @SlicedFrom(sourceFieldName = "status", bitIndex = 0)
    private Boolean accBooleanStatus;
    // 0 北纬;1 南纬
    // 将上面的 status 字段的第2位取出转为 int 类型
    @SlicedFrom(sourceFieldName = "status", bitIndex = 2)
    private int latType;

    // (3). byte[8,12) DWORD 纬度
    @RequestFieldAlias.Dword(order = 3)
    private long lat;

    // (4). byte[12,16) DWORD 经度
    @RequestFieldAlias.Dword(order = 4)
    private long lng;

    // (5). byte[16,18) WORD 高度
    @RequestFieldAlias.Word(order = 5)
    private Integer height;

    // (6). byte[18,20) WORD 速度
    @RequestFieldAlias.Word(order = 6)
    private int speed;

    // (7). byte[20,22) WORD 方向
    @RequestFieldAlias.Word(order = 7)
    private Integer direction;

    // (8). byte[22,28) BCD[6] 时间
    @RequestFieldAlias.Bcd(order = 8, length = 6)
    private String time;

    // (9). byte[28,n) 附加项列表
    @RequestFieldAlias.LocationMsgExtraItemMapping(
            order = 9,
            lengthExpression = "#ctx.msgBodyLength() - 28",
            keyValueMappings = {
                    // 基础类型
                    @KeyValueMapping(key = 0x01, value = @ValueDescriptor(source = MsgDataType.DWORD, target = Long.class), desc = "里程，DWORD，1/10km，对应车上里程表读数"),
                    @KeyValueMapping(key = 0x02, value = @ValueDescriptor(source = MsgDataType.WORD, target = Integer.class), desc = "油量，WORD，1/10L，对应车上油量表读数"),
                    @KeyValueMapping(key = 0x03, value = @ValueDescriptor(source = MsgDataType.WORD, target = Integer.class), desc = "行驶记录功能获取的速度，WORD，1/10km/h"),
                    @KeyValueMapping(key = 0x04, value = @ValueDescriptor(source = MsgDataType.WORD, target = Integer.class), desc = "需要人工确认报警事件的 ID，WORD，从 1 开始计数"),
                    @KeyValueMapping(key = 0x11, value = @ValueDescriptor(source = MsgDataType.BYTES, target = byte[].class), desc = "长度1或5；超速报警附加信息见 表 28"),
                    @KeyValueMapping(key = 0x25, value = @ValueDescriptor(source = MsgDataType.DWORD, target = Integer.class), desc = "扩展车辆信号状态位，定义见 表 31"),
                    @KeyValueMapping(key = 0x2A, value = @ValueDescriptor(source = MsgDataType.WORD, target = Integer.class), desc = "IO状态位，定义见 表 32"),
                    @KeyValueMapping(key = 0x2B, value = @ValueDescriptor(source = MsgDataType.DWORD, target = Long.class), desc = "模拟量，bit0-15，AD0;bit16-31，AD1"),
                    @KeyValueMapping(key = 0x30, value = @ValueDescriptor(source = MsgDataType.BYTE, target = Short.class), desc = "BYTE，无线通信网络信号强度"),
                    @KeyValueMapping(key = 0x31, value = @ValueDescriptor(source = MsgDataType.BYTE, target = Integer.class), desc = "BYTE，GNSS 定位卫星数"),
                    // 嵌套类型
                    @KeyValueMapping(key = 0x64, value = @ValueDescriptor(source = MsgDataType.OBJECT, target = BuiltinMsg64Alias.class), desc = "苏标: 高级驾驶辅助报警信息，定义见表 4-15"),
                    // 嵌套类型
                    @KeyValueMapping(key = 0x65, value = @ValueDescriptor(source = MsgDataType.OBJECT, target = BuiltinMsg65Alias.class), desc = "苏标: 驾驶员状态监测系统报警信息，定义见表 4-17"),
                    // 嵌套类型
                    @KeyValueMapping(key = 0x66, value = @ValueDescriptor(source = MsgDataType.OBJECT, target = BuiltinMsg66Alias.class), desc = "苏标: 胎压监测系统报警信息，定义见表 4-18"),
                    // 嵌套类型
                    @KeyValueMapping(key = 0x67, value = @ValueDescriptor(source = MsgDataType.OBJECT, target = BuiltinMsg67Alias.class), desc = "苏标: 盲区监测系统报警信息，定义见表 4-20"),
            },
            // keyValueMappings 中没有指定的key, 都会以该属性描述符指定的格式解析
            defaultKeyValueMapping = @ValueDescriptor(source = MsgDataType.BYTES, target = ByteArrayContainer.class)
    )
    private Map<Integer, Object> extraItemMap;
}
