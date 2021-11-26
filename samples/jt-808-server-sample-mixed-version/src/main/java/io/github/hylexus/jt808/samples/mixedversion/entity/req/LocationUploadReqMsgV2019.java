package io.github.hylexus.jt808.samples.mixedversion.entity.req;

import io.github.hylexus.jt.annotation.msg.req.Jt808ReqMsgBody;
import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import io.github.hylexus.jt.annotation.msg.req.extra.ExtraField;
import io.github.hylexus.jt.annotation.msg.req.extra.ExtraMsgBody;
import io.github.hylexus.jt.annotation.msg.req.slice.SlicedFrom;
import io.github.hylexus.jt.annotation.msg.req.slice.SplittableField;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
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
 * @author hylexus
 * Created At 2020-01-29 12:03 下午
 */
@Slf4j
@Data
@Accessors(chain = true)
@Jt808ReqMsgBody(msgType = 0x0200, version = Jt808ProtocolVersion.VERSION_2019)
public class LocationUploadReqMsgV2019 implements RequestMsgBody, RequestMsgMetadataAware {

    @ToString.Exclude
    private RequestMsgMetadata requestMsgMetadata;
    // (1). 报警标志
    @BasicField(startIndex = 0, dataType = DWORD)
    private int alarmFlag;
    // (2). 状态
    @BasicField(startIndex = 4, dataType = DWORD)
    @SplittableField(splitPropertyValueIntoNestedBeanField = "statusInfo")
    private int status;
    private LocationUploadStatus statusInfo;

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
    // 纬度(尚未除以 10^6)
    @BasicField(startIndex = 8, dataType = DWORD)
    private Integer intLat;
    // (3). 纬度(使用转换器除以10^6转为Double类型)
    @BasicField(startIndex = 8, dataType = DWORD, customerDataTypeConverterClass = LngLatReqMsgFieldConverter.class)
    private Double lat;
    // (4). 经度(尚未除以 10^6)
    @BasicField(startIndex = 12, dataType = DWORD)
    private Integer intLng;
    // 经度(使用转换器除以10^6转为Double类型)
    @BasicField(startIndex = 12, dataType = DWORD, customerDataTypeConverterClass = LngLatReqMsgFieldConverter.class)
    private Double lng;
    // 经度(startIndexMethod使用示例)
    @BasicField(startIndexMethod = "getLngStartIndex", dataType = DWORD, customerDataTypeConverterClass = LngLatReqMsgFieldConverter.class)
    private Double lngByStartIndexMethod;
    // (5). 高度
    @BasicField(startIndex = 16, dataType = WORD)
    private Integer height;
    // (6). 速度
    @BasicField(startIndex = 18, dataType = WORD)
    private int speed;
    // (7). 方向
    @BasicField(startIndex = 20, dataType = WORD)
    private Integer direction;

    // (8). 时间
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

    // a sample for https://github.com/hylexus/jt-framework/issues/9
    public int getLngStartIndex() {
        log.info("消息体总长度:{}", this.requestMsgMetadata.getHeader().getMsgBodyLength());
        return 12;
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
        @ExtraField.NestedFieldMapping(msgId = 0x01, dataType = DWORD)
        private Integer field0x01; //里程，DWORD，1/10km，对应车上里程表读数

        @ExtraField.NestedFieldMapping(msgId = 0x2b, dataType = DWORD)
        private Integer field0x2b; //模拟量，bit0-15，AD0;bit16-31，AD1。

        @ExtraField.NestedFieldMapping(msgId = 0x30, dataType = BYTE)
        private byte field0x30; //BYTE，无线通信网络信号强度

        @ExtraField.NestedFieldMapping(msgId = 0x31, dataType = BYTE)
        private byte field0x31; //GNSS 定位卫星数

        @ExtraField.NestedFieldMapping(msgId = 0x65, isNestedExtraField = true)
        private Extra0x65 field0x65;

    }

    @Data
    @ExtraMsgBody
    public static class Extra0x65 {

        // 1.0.6以及之前都不支持这种写法 --> ExtraMsgBody里的BasicField不会被解析
        @BasicField(startIndex = 0, dataType = DWORD)
        private Integer warningId;

        @BasicField(startIndex = 4, dataType = BYTE)
        private Integer status;

        @BasicField(startIndex = 5, dataType = BYTE)
        private Integer field5;

        @BasicField(startIndex = 6, dataType = BYTE)
        private Integer field6;

        @BasicField(startIndex = 15, dataType = DWORD, customerDataTypeConverterClass = LngLatReqMsgFieldConverter.class)
        private Double lat;

        @BasicField(startIndex = 19, dataType = DWORD, customerDataTypeConverterClass = LngLatReqMsgFieldConverter.class)
        private Double lng;

        @BasicField(startIndex = 23, dataType = BCD, length = 6)
        private String time;
    }

    @Data
    public static class LocationUploadStatus {
        @SplittableField.BitAt(bitIndex = 0)
        private boolean accStatus; // acc开?

        @SplittableField.BitAt(bitIndex = 1)
        private int bit1; //1:定位, 0:未定位

        @SplittableField.BitAt(bitIndex = 2)
        private Boolean isSouthLat;// 是否南纬? 0:北纬 1:南纬

        // 0:东经 1:西经
        @SplittableField.BitAt(bitIndex = 3)
        private Integer lngType;

        // 0: 运营  1:停运
        @SplittableField.BitAt(bitIndex = 4)
        private int operationStatus;

        // 0: 经纬度未经过保密插件加密  1:经纬度已经保密插件加密
        @SplittableField.BitAt(bitIndex = 5)
        private int latLngEncryptStatus;

        // 1: 紧急刹车系统采集的前撞预警
        @SplittableField.BitAt(bitIndex = 6)
        private int bit6;

        // 扯到偏移预警
        @SplittableField.BitAt(bitIndex = 7)
        private int bit7;
    }
}
