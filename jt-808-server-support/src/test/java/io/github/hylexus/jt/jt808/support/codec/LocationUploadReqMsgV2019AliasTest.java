package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.SlicedFrom;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hylexus
 */
@Slf4j
@Data
@Accessors(chain = true)
//@Jt808ReqMsgBody(msgType = 0x0200, version = Jt808ProtocolVersion.VERSION_2019)
// public class LocationUploadReqMsgV2019Test implements Jt808HeaderSpecAware {
public class LocationUploadReqMsgV2019AliasTest {

    //    @ToString.Exclude
    //    private Jt808MsgHeaderSpec header;

    //    public void setHeader(Jt808MsgHeaderSpec headerSpec) {
    //        this.header = headerSpec;
    //    }

    // (1). 报警标志
    // @RequestField(order = 0, startIndex = 0, dataType = DWORD)
    @RequestFieldAlias.Dword(order = 0)
    private long alarmFlag;
    // (2). 状态
    // @RequestField(order = 1, startIndex = 4, dataType = DWORD)
    @RequestFieldAlias.Dword(order = 1)
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
    // (3). 纬度(尚未除以 10^6)
    // @RequestField(order = 2, startIndex = 8, dataType = DWORD)
    // @RequestFields.DWORD(order = 2, startIndex = 8)
    // private Long intLat;

    @RequestFieldAlias.GeoPoint(order = 2)
    // 地理位置(经纬度)支持: long/Long, double/Double, BigDecimal
    private Double intLat;
    // private BigDecimal intLat;

    // (4). 经度(尚未除以 10^6)
    // @RequestField(order = 3, startIndex = 12, dataType = DWORD)
    @RequestFieldAlias.Dword(order = 3)
    private Long intLng;
    // (5). 高度
    // @RequestField(order = 4, startIndex = 16, dataType = WORD)
    @RequestFieldAlias.Word(order = 4)
    private Integer height;
    // (6). 速度
    // @RequestField(order = 5, startIndex = 18, dataType = WORD)
    @RequestFieldAlias.Word(order = 5)
    private int speed;
    // (7). 方向
    // @RequestField(order = 6, startIndex = 20, dataType = WORD)
    @RequestFieldAlias.Word(order = 6)
    private Integer direction;

    // (8). 时间
    // 1. 解析为 String
    // @RequestField(order = 7, startIndex = 22, dataType = BCD, length = 6)
    // @RequestFields.BCD(order = 7, startIndex = 22, length = 6)
    // private String time;

    // 2. 解析为 LocalDateTime
    @RequestFieldAlias.BcdDateTime(order = 7)
    // @BcdDateTime(order = 7, pattern = "yyMMddHHmmss")
    private LocalDateTime time;

    // 3. 解析为 java.util.Date
    // @RequestFields.BCD_TO_DATE(order = 7, startIndex = 22)
    // private Date time;

    // @BasicField(order = 8, startIndex = 28, dataType = LIST, byteCountMethod = "getExtraInfoLength")
    // @BasicField(order = 8, startIndex = 28, dataType = LIST, lengthExpression = "getExtraInfoLength()")
    // @RequestField(order = 8, startIndex = 28, dataType = LIST, lengthExpression = "#ctx.msgBodyLength() - 28")
    @RequestFieldAlias.List(order = 8, lengthExpression = "#ctx.msgBodyLength() - 28")
    private List<ExtraItem> extraItemList;

    @Data
    public static class ExtraItem {
        // @RequestField(order = 0, startIndex = 0, dataType = BYTE)
        @RequestFieldAlias.Byte(order = 0)
        private byte id;
        // @RequestField(order = 1, startIndex = 1, dataType = BYTE)
        @RequestFieldAlias.Byte(order = 1)
        private byte contentLength;

        // @RequestField(order = 3, startIndex = 2, lengthExpression = "#this.contentLength", dataType = BYTES)
        @RequestFieldAlias.Bytes(order = 3, lengthExpression = "#this.contentLength")
        private byte[] content;

        public int getContentLengthMethod() {
            return contentLength;
        }
    }

}
