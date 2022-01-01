package io.github.hylexus.jt808.samples.annotation.msg.req;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.SlicedFrom;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808RequestBody
public class LocationUploadMsgV2019 {
    // (1). byte[0,4)  DWORD 报警标志
    @RequestField(order = 1, startIndex = 0, dataType = DWORD)
    private long alarmFlag;

    // (2). byte[4,8) DWORD 状态
    @RequestField(order = 2, startIndex = 4, dataType = DWORD)
    private int status;

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
    @RequestField(order = 3, startIndex = 8, dataType = DWORD)
    private long lat;

    // (4). byte[12,16) DWORD 经度
    @RequestField(order = 4, startIndex = 12, dataType = DWORD)
    private long lng;

    // (5). byte[16,18) WORD 高度
    @RequestField(order = 5, startIndex = 16, dataType = WORD)
    private Integer height;

    // (6). byte[18,20) WORD 速度
    @RequestField(order = 6, startIndex = 18, dataType = WORD)
    private int speed;

    // (7). byte[20,22) WORD 方向
    @RequestField(order = 7, startIndex = 20, dataType = WORD)
    private Integer direction;

    // (8). byte[22,28) BCD[6] 时间
    @RequestField(order = 8, startIndex = 22, dataType = BCD, length = 6)
    private String time;

    // (9). byte[28,n) 附加项列表
    // @RequestField(order = 9, startIndex = 28, dataType = LIST, lengthExpression = "#request.msgBodyLength() - 28")
    @RequestField(order = 9, startIndex = 28, dataType = LIST, lengthExpression = "#ctx.msgBodyLength() - 28")
    private List<ExtraItem> extraItemList;

    @Data
    public static class ExtraItem {
        // 附加信息ID
        @RequestField(order = 0, startIndex = 0, dataType = BYTE)
        private int id;

        // 附加信息长度
        @RequestField(order = 1, startIndex = 1, dataType = BYTE)
        private int contentLength;

        // 附加信息内容
        @RequestField(order = 3, startIndex = 2, lengthExpression = "#this.contentLength", dataType = BYTES)
        private byte[] content;
    }
}