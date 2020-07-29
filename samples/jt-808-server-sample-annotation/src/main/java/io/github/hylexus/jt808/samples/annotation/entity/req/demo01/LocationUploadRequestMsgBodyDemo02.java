package io.github.hylexus.jt808.samples.annotation.entity.req.demo01;

import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import io.github.hylexus.jt.annotation.msg.req.extra.ExtraField;
import io.github.hylexus.jt.annotation.msg.req.extra.ExtraMsgBody;
import io.github.hylexus.jt.annotation.msg.req.slice.SlicedFrom;
import io.github.hylexus.jt.data.converter.req.entity.LngLatReqMsgFieldConverter;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgMetadataAware;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import static io.github.hylexus.jt.data.MsgDataType.*;

/**
 * 7E02000024013523339527000100000000000C00020158656006CA8CA900360008000020070214542931010BE003EE01024D7E
 *
 * @author hylexus
 * Created At 2020-01-29 12:03 下午
 */
@Slf4j
@Data
@Accessors(chain = true)
//@Jt808ReqMsgBody(msgType = 0x0200)
public class LocationUploadRequestMsgBodyDemo02 implements RequestMsgBody, RequestMsgMetadataAware {

    @ToString.Exclude
    private RequestMsgMetadata requestMsgMetadata;
    // 报警标志
    @BasicField(startIndex = 0, dataType = DWORD)
    private int alarmFlag;

    // 状态
    @BasicField(startIndex = 4, dataType = DWORD)
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

    // 纬度(使用转换器除以10^6转为Double类型)
    @BasicField(startIndex = 8, dataType = DWORD, customerDataTypeConverterClass = LngLatReqMsgFieldConverter.class)
    private Double lat;
    // 经度(尚未除以 10^6)

    // 经度(使用转换器除以10^6转为Double类型)
    @BasicField(startIndex = 12, dataType = DWORD, customerDataTypeConverterClass = LngLatReqMsgFieldConverter.class)
    private Double lng;

    // 高度
    @BasicField(startIndex = 16, dataType = WORD)
    private Integer height;
    // 速度
    @BasicField(startIndex = 18, dataType = WORD)
    private int speed;
    // 方向
    @BasicField(startIndex = 20, dataType = WORD)
    private Integer direction;

    @BasicField(startIndex = 22, dataType = BCD, length = 6)
    private String time;

    @ExtraField(
            // 消息体中第28字节开始
            startIndex = 28,
            // 附加项长度用getExtraInfoLength返回值表示
            byteCountMethod = "getExtraInfoLength"
    )
    private ExtraInfo extraInfo;

    @Override
    public void setRequestMsgMetadata(RequestMsgMetadata metadata) {
        this.requestMsgMetadata = metadata;
    }


    public int getExtraInfoLength() {
        int totalLength = this.requestMsgMetadata.getHeader().getMsgBodyLength();
        return totalLength - 28;
    }

    @Data
    // 切记@ExtraMsgBody注解不能丢
    @ExtraMsgBody(
            byteCountOfMsgId = 1, // 消息Id用1个字节表示
            byteCountOfContentLength = 1 // 附加项长度字段用1个字节表示
    )
    public static class ExtraInfo {
        @ExtraField.NestedFieldMapping(msgId = 0x31, dataType = BYTE)
        private byte field0x31;

        @ExtraField.NestedFieldMapping(msgId = 0xe0, dataType = BYTES)
        private byte[] field0xe0Bytes;

        @ExtraField.NestedFieldMapping(msgId = 0xEE, dataType = BYTE)
        private byte field0xEE;
    }

}
