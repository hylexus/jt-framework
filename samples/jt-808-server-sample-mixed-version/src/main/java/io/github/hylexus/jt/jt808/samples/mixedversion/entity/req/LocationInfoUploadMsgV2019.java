package io.github.hylexus.jt.jt808.samples.mixedversion.entity.req;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.SlicedFrom;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

/**
 * @author hylexus
 */
@Slf4j
@Data
@Accessors(chain = true)
@Jt808RequestBody
public class LocationInfoUploadMsgV2019 {
    // (1). 报警标志
    @RequestField(order = 1, startIndex = 0, dataType = DWORD)
    private int alarmFlag;
    // (2). 状态
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
    // (3). 纬度(尚未除以 10^6)
    @RequestField(order = 3, startIndex = 8, dataType = DWORD)
    private Integer intLat;
    // (4). 经度(尚未除以 10^6)
    @RequestField(order = 4, startIndex = 12, dataType = DWORD)
    private Integer intLng;
    // (5). 高度
    @RequestField(order = 5, startIndex = 16, dataType = WORD)
    private Integer height;
    // (6). 速度
    @RequestField(order = 6, startIndex = 18, dataType = WORD)
    private int speed;
    // (7). 方向
    @RequestField(order = 7, startIndex = 20, dataType = WORD)
    private Integer direction;

    // (8). 时间
    @RequestField(order = 8, startIndex = 22, dataType = BCD, length = 6)
    private String time;

    // (9). 附加项列表
    @RequestField(order = 9, startIndex = 28, dataType = LIST, lengthExpression = "#request.msgBodyLength() - 28")
    private List<ExtraItem> extraItemList;

    @Data
    public static class ExtraItem {
        @RequestField(order = 0, startIndex = 0, dataType = BYTE)
        private byte id;
        @RequestField(order = 1, startIndex = 1, dataType = BYTE)
        private byte contentLength;

        @RequestField(order = 3, startIndex = 2, lengthExpression = "#this.contentLength", dataType = BYTES)
        private byte[] content;
    }
}
