package io.github.hylexus.jt808.server.msg.req;

import io.github.hylexus.jt.annotation.msg.AdditionalField;
import io.github.hylexus.jt.annotation.msg.basic.BasicField;
import io.github.hylexus.jt.annotation.msg.basic.Jt808MsgBody;
import io.github.hylexus.jt.annotation.msg.extra.ExtraField;
import io.github.hylexus.jt.annotation.msg.slice.SlicedFrom;
import io.github.hylexus.jt.annotation.msg.slice.SplittableField;
import io.github.hylexus.jt.data.converter.LngLatDataTypeConverter;
import io.github.hylexus.jt.data.msg.AdditionalItemEntity;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.server.msg.req.location.ExtraEntity;
import io.github.hylexus.jt808.server.msg.req.location.LocationUploadStatus;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgMetadataAware;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

import static io.github.hylexus.jt.annotation.msg.AdditionalField.ROOT_GROUP_MSG_ID;
import static io.github.hylexus.jt.data.MsgDataType.*;

@Data
@Accessors(chain = true)
@Jt808MsgBody(msgType = 0x0200)
public class LocationUploadMsgBody implements RequestMsgBody, RequestMsgMetadataAware {

    @ToString.Exclude
    private RequestMsgMetadata requestMsgMetadata;

    @BasicField(startIndex = 0, dataType = DWORD)
    private int warningFlag;

    @BasicField(startIndex = 4, dataType = DWORD)
    @SplittableField(splitPropertyValueIntoNestedBeanField = "statusInfo")
    private int status;

    private LocationUploadStatus statusInfo = new LocationUploadStatus();

    @SlicedFrom(sourceFieldName = "status", bitIndex = 0)
    private Integer accStatus;

    @SlicedFrom(sourceFieldName = "status", bitIndex = 0)
    private Boolean accStatus1;

    @SlicedFrom(sourceFieldName = "status", startBitIndex = 8, endBitIndex = 9)
    private Integer bit8to9;

    @SlicedFrom(sourceFieldName = "status", bitIndex = 18)
    private Integer bit18;

    @BasicField(startIndex = 8, dataType = DWORD, customerDataTypeConverterClass = LngLatDataTypeConverter.class)
    private Double lat;

    @BasicField(startIndex = 12, dataType = DWORD, customerDataTypeConverterClass = LngLatDataTypeConverter.class)
    private Double lng;

    @BasicField(startIndex = 16, dataType = WORD)
    private short height;

    @BasicField(startIndex = 18, dataType = WORD)
    private short speed;

    @BasicField(startIndex = 20, dataType = WORD)
    private short direction;

    @BasicField(startIndex = 22, dataType = BCD, length = 6)
    private String time;

    @AdditionalField(
            startIndex = 28,
            byteCountMethod = "getExtraInfoLength",
            msgTypeMappings = {
                    @AdditionalField.MsgTypeMapping(groupMsgId = ROOT_GROUP_MSG_ID),
                    @AdditionalField.MsgTypeMapping(groupMsgId = 0xf3, byteCountOfMsgId = 2, isNestedAdditionalField = true),
            }
    )
    private List<AdditionalItemEntity> additionalInfo;

    @ExtraField(
            startIndex = 28,
            byteCountMethod = "getExtraInfoLength"
    )
    private ExtraEntity extraEntity;

    @Override
    public void setRequestMsgMetadata(RequestMsgMetadata metadata) {
        this.requestMsgMetadata = metadata;
    }

    public int getExtraInfoLength() {
        int totalLength = this.requestMsgMetadata.getHeader().getMsgBodyLength();
        return totalLength - 28;
    }

}