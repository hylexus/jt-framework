package io.github.hylexus.jt.jt808.support.codec;

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
//@Jt808ReqMsgBody(msgType = 0x0200, version = Jt808ProtocolVersion.VERSION_2019)
// public class LocationUploadReqMsgV2019Test implements Jt808HeaderSpecAware {
public class LocationUploadReqMsgV2019Test {

    //    @ToString.Exclude
    //    private Jt808MsgHeaderSpec header;

    //    public void setHeader(Jt808MsgHeaderSpec headerSpec) {
    //        this.header = headerSpec;
    //    }

    // (1). 报警标志
    @RequestField(order = 0, dataType = DWORD)
    private int alarmFlag;
    // (2). 状态
    @RequestField(order = 1, dataType = DWORD)
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
    @RequestField(order = 2, dataType = DWORD)
    private Integer intLat;
    // (4). 经度(尚未除以 10^6)
    @RequestField(order = 3, dataType = DWORD)
    private Integer intLng;
    // (5). 高度
    @RequestField(order = 4, dataType = WORD)
    private Integer height;
    // (6). 速度
    @RequestField(order = 5, dataType = WORD)
    private int speed;
    // (7). 方向
    @RequestField(order = 6, dataType = WORD)
    private Integer direction;

    // (8). 时间
    @RequestField(order = 7, dataType = BCD, length = 6)
    private String time;

    // @BasicField(order = 8, dataType = LIST, byteCountMethod = "getExtraInfoLength")
    // @BasicField(order = 8, dataType = LIST, lengthExpression = "getExtraInfoLength()")
    @RequestField(order = 8, dataType = LIST, lengthExpression = "#ctx.msgBodyLength() - 28")
    private List<ExtraItem> extraItemList;

    @Data
    public static class ExtraItem {
        @RequestField(order = 0, dataType = BYTE)
        private byte id;
        @RequestField(order = 1, dataType = BYTE)
        private byte contentLength;

        // @BasicField(order = 3, lengthMethod = "getContentLengthMethod", dataType = BYTES)
        @RequestField(order = 3, lengthExpression = "#this.contentLength", dataType = BYTES)
        private byte[] content;

        public int getContentLengthMethod() {
            return contentLength;
        }
    }

}
