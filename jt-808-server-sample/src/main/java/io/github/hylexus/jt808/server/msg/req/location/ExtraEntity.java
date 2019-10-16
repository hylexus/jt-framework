package io.github.hylexus.jt808.server.msg.req.location;

import io.github.hylexus.jt.annotation.msg.req.extra.ExtraField;
import io.github.hylexus.jt.annotation.msg.req.extra.ExtraMsgBody;
import io.github.hylexus.jt.annotation.msg.req.slice.SlicedFrom;
import lombok.Data;

import static io.github.hylexus.jt.data.MsgDataType.*;

/**
 * @author hylexus
 * Created At 2019-10-01 7:42 下午
 */
@Data
@ExtraMsgBody(byteCountOfMsgId = 1, byteCountOfContentLength = 1)
public class ExtraEntity {

    @ExtraField.NestedFieldMapping(msgId = 0x01, dataType = DWORD)
    private Integer field0x01;

    @SlicedFrom(sourceFieldName = "field0x01", bitIndex = 0)
    private Boolean field0x01BoolValue;

    @ExtraField.NestedFieldMapping(msgId = 0x02, dataType = WORD)
    private Integer field0x02;

    @ExtraField.NestedFieldMapping(msgId = 0x03, dataType = WORD)
    private Short field0x03;

    @ExtraField.NestedFieldMapping(msgId = 0x04, dataType = WORD)
    private Short field0x04;

    @ExtraField.NestedFieldMapping(msgId = 0x25, dataType = DWORD)
    private Integer field0x25;

    @ExtraField.NestedFieldMapping(msgId = 0x2a, dataType = WORD)
    private Short field0x2a;

    @ExtraField.NestedFieldMapping(msgId = 0x2b, dataType = DWORD)
    private Integer field0x2b;

    @ExtraField.NestedFieldMapping(msgId = 0x30, dataType = BYTE)
    private Byte field0x30;

    @ExtraField.NestedFieldMapping(msgId = 0x31, dataType = BYTE)
    private Byte field0x31;

    @ExtraField.NestedFieldMapping(msgId = 0xf3, isNestedExtraField = true)
    private ObdData obdData;
}
