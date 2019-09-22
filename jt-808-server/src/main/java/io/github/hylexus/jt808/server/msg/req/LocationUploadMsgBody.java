package io.github.hylexus.jt808.server.msg.req;

import io.github.hylexus.jt.annotation.Jt808Field;
import io.github.hylexus.jt.annotation.Jt808MsgBody;
import io.github.hylexus.jt.data.converter.LngLatDataTypeConverter;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import lombok.Data;
import lombok.experimental.Accessors;

import static io.github.hylexus.jt.data.MsgDataType.*;

@Data
@Accessors(chain = true)
@Jt808MsgBody(msgType = 0x0200)
public class LocationUploadMsgBody implements RequestMsgBody {

    @Jt808Field(startIndex = 0, dataType = DWORD)
    private int warningFlag;

    @Jt808Field(startIndex = 4, dataType = DWORD)
    private Integer status;

    @Jt808Field(startIndex = 8, dataType = DWORD, customerDataTypeConverterClass = LngLatDataTypeConverter.class)
    private Double lat;

    @Jt808Field(startIndex = 12, dataType = DWORD, customerDataTypeConverterClass = LngLatDataTypeConverter.class)
    private Double lng;

    @Jt808Field(startIndex = 16, dataType = WORD)
    private int height;

    @Jt808Field(startIndex = 18, dataType = WORD)
    private int speed;

    @Jt808Field(startIndex = 20, dataType = WORD)
    private int direction;

    @Jt808Field(startIndex = 22, dataType = BCD, length = 6)
    private String time;
}