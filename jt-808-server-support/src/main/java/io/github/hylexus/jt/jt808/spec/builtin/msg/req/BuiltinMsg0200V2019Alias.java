package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.SlicedFrom;
import io.github.hylexus.jt.jt808.support.data.type.byteseq.ByteArrayContainer;
import io.github.hylexus.jt.utils.BitOperator;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg0200V2019Alias {
    // (1). byte[0,4)  DWORD 报警标志
    @RequestFieldAlias.Dword(order = 1)
    private long alarmFlag;
    // @RequestFieldAlias.Dword(order = 1)
    // private BitOperator bitOperator;

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
    // @RequestField(order = 9, startIndex = 28, dataType = LIST, lengthExpression = "#request.msgBodyLength() - 28")
    @RequestFieldAlias.List(order = 9, lengthExpression = "#ctx.msgBodyLength() - 28")
    private List<ExtraItem> extraItemList;

    @Data
    public static class ExtraItem {
        // 附加信息ID
        @RequestFieldAlias.Byte(order = 0)
        private int id;

        // 附加信息长度
        @RequestFieldAlias.Byte(order = 1)
        private int contentLength;

        // 附加信息内容
        @RequestFieldAlias.Bytes(order = 3, lengthExpression = "#this.contentLength")
        // private byte[] content;
        private ByteArrayContainer content;
        // private ByteBufContainer content;
    }
}
