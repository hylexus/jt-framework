package io.github.hylexus.jt808.samples.annotation.entity.req;
// 该源码来自 https://github.com/hylexus/jt-framework/issues/8

import io.github.hylexus.jt.annotation.msg.req.Jt808ReqMsgBody;
import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import io.github.hylexus.jt.annotation.msg.req.extra.ExtraField;
import io.github.hylexus.jt.annotation.msg.req.extra.ExtraMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgMetadataAware;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import static io.github.hylexus.jt.data.MsgDataType.*;

/**
 * 数据上行透传(0x0900)
 *
 * @author wsy
 * @version 1.0
 * @since 2020-03-11 14:04
 **/

@Data
@Accessors(chain = true)
@Jt808ReqMsgBody(msgType = 0x0900)
public class PassthroughPack implements RequestMsgBody, RequestMsgMetadataAware {

    @ToString.Exclude
    private RequestMsgMetadata requestMsgMetadata;
    @BasicField(startIndex = 0, dataType = BYTE)
    private int msgType;
    @BasicField(startIndex = 1, dataType = BCD, length = 6)
    private String time;
    /**
     * 数据类型
     * 0x00：实时数据；
     * 0x01：补传数据
     */
    @BasicField(startIndex = 7, dataType = BYTE)
    private int dataType;
    /**
     * 车辆类型
     * 0x01：商用车；
     * 0x02：乘用车
     */
    @BasicField(startIndex = 8, dataType = BYTE)
    private int vehicleType;
    /**
     * 消息子类
     * 0x01：OBD数据流上报；
     * 0x02：故障码数据上报；
     * 0x03：告警数据及驾驶行为数据上报；
     * 0x04：行程报告数据上报；
     * 0x05：终端下位机日志数据上报；
     * 0x06：CAN学习采集数据上传；
     * 0x07：支持数据流ID列表上报；
     * 0x08：支持车辆控制ID列表上报；
     * 0x09：支持告警及驾驶行为数据ID列表上报
     * 0x0A：车辆关键数据上传
     * 0x0B：VIN码上报
     * 0x0C：体检数据上报
     * 0x0D：设备自检数据上报
     */
    @BasicField(startIndex = 9, dataType = BYTE)
    private int childMsgType;
    /**
     * 数据总数
     */
    @BasicField(startIndex = 10, dataType = BYTE)
    private int totalMsg;
    /**
     * 透传消息内容(子内容)
     */
    @BasicField(startIndex = 11, dataType = BYTES, byteCountMethod = "getContentLength")
    private byte[] content;
    @ExtraField(
            // 消息体中第11字节开始
            startIndex = 11,
            // 附加项长度用getExtraInfoLength返回值表示
            byteCountMethod = "getContentLength"
    )
    private ExtraData extraData;

    @Override
    public void setRequestMsgMetadata(RequestMsgMetadata metadata) {
        this.requestMsgMetadata = metadata;
    }

    public int getContentLength() {
        int totalLength = this.requestMsgMetadata.getHeader().getMsgBodyLength();
        return totalLength - 11;
    }

    @Data
    // 切记@ExtraMsgBody注解不能丢
    @ExtraMsgBody(
            byteCountOfMsgId = 2, // 消息Id用2个字节表示
            byteCountOfContentLength = 1 // 附加项长度字段用1个字节表示
    )
    public static class ExtraData {
        /**
         * 电压 mV
         */
        @ExtraField.NestedFieldMapping(msgId = 0x0530, dataType = WORD)
        private int field0x0530;
        /**
         * 累计里程 km  y=x/10；精度：0.1
         */
        @ExtraField.NestedFieldMapping(msgId = 0x0546, dataType = DWORD)
        private int field0x0546;

        @ExtraField.NestedFieldMapping(msgId = 0x0545, dataType = DWORD)
        private int field0x0545;

    }
}