package io.github.hylexus.jt.jt808.spec.builtin.msg.extension.location;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 苏标-表-4-18 胎压监测系统报警信息数据格式
 *
 * @author hylexus
 */
@Slf4j
@Data
public class BuiltinMsg66Alias {

    // offset[0,4) DWORD 报警 ID: 按照报警先后，从0开始循环累加，不区分报警类型。
    @RequestFieldAlias.Dword(order = 10)
    private long alarmId;

    // offset[4,5) BYTE 标志状态
    // 0x00：不可用
    // 0x01：开始标志
    // 0x02：结束标志
    // 该字段仅适用于有开始和结束标志类型的报警或事件，报警类型或事件类型无开始和结束标志，则该位不可用，填入0x00即可
    @RequestFieldAlias.Byte(order = 20)
    private short status;

    // offset[12,13) BYTE 车速
    @RequestFieldAlias.Byte(order = 100)
    private short speed;

    // offset[13,15) WORD 高程
    @RequestFieldAlias.Word(order = 110)
    private int height;

    // offset[15,19) DWORD 纬度
    @RequestFieldAlias.Dword(order = 120)
    private long latitude;

    // offset[19,23) DWORD 经度
    @RequestFieldAlias.Dword(order = 130)
    private long longitude;

    // offset[23,29) BCD[6] 日期时间
    @RequestFieldAlias.BcdDateTime(order = 140)
    private LocalDateTime datetime;

    // offset[29,31] WORD 车辆状态
    @RequestFieldAlias.Word(order = 150)
    private int vehicleStatus;
    // offset[31,31+16) BYTE[16] 报警标识号
    // 报警识别号定义见表4-16
    @RequestFieldAlias.Object(order = 160, length = 16)
    private AlarmIdentifierAlias alarmIdentifier;

    // offset[39, 40) BYTE 报警/事件列表总数
    @RequestFieldAlias.Byte(order = 170)
    private short eventItemCount;

    // 报警/事件信息列表
    @RequestFieldAlias.List(order = 180, lengthExpression = "#ctx.msgBodyLength() - 40", conditionalOn = "#this.getEventItemCount() > 0")
    private List<EventItem> eventItemList;

    @Data
    public static class EventItem {
        // 胎压报警位置 BYTE 报警轮胎位置编号（从左前轮开始以Z字形从00依次编号，编号与是否安装TPMS无关）
        @RequestFieldAlias.Byte(order = 10)
        private short offset0;

        // 报警/事件类型 WORD 0表示无报警，1表示有报警
        // bit0：胎压（定时上报）
        // bit1：胎压过高报警
        // bit2：胎压过低报警
        // bit3：胎温过高报警
        // bit4：传感器异常报警
        // bit5：胎压不平衡报警
        // bit6：慢漏气报警
        // bit7：电池电量低报警
        // bit8~bit15：自定义
        @RequestFieldAlias.Word(order = 20)
        private int offset2;

        // 胎压 WORD 单位 Kpa
        @RequestFieldAlias.Word(order = 30)
        private int offset4;

        // 胎温 WORD 单位 ℃
        @RequestFieldAlias.Word(order = 40)
        private int offset6;

        // 电池电量 WORD 单位 %
        @RequestFieldAlias.Word(order = 50)
        private int offset8;
    }
}
