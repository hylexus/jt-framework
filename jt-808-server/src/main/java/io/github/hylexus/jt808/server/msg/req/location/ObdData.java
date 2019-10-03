package io.github.hylexus.jt808.server.msg.req.location;

import io.github.hylexus.jt.annotation.msg.ExtraField;
import io.github.hylexus.jt.annotation.msg.ExtraMsgBody;
import lombok.Data;

import static io.github.hylexus.jt.data.MsgDataType.*;

/**
 * @author hylexus
 * Created At 2019-10-01 7:50 下午
 */
@Data
@ExtraMsgBody(byteCountOfMsgId = 2)
public class ObdData {

    @ExtraField.NestedFieldMapping(msgId = 0x0002, dataType = WORD)
    private Short field0x0002;

    @ExtraField.NestedFieldMapping(msgId = 0x0003, dataType = WORD)
    private Short field0x0003;

    @ExtraField.NestedFieldMapping(msgId = 0x0004, dataType = WORD)
    private Short field0x0004;

    @ExtraField.NestedFieldMapping(msgId = 0x0005, dataType = DWORD)
    private Integer field0x0005;

    @ExtraField.NestedFieldMapping(msgId = 0x0006, dataType = WORD)
    private Short field0x0006;

    @ExtraField.NestedFieldMapping(msgId = 0x0007, dataType = WORD)
    private Short field0x0007;

    @ExtraField.NestedFieldMapping(msgId = 0x0008, dataType = BYTE)
    private Byte field0x0008;

    @ExtraField.NestedFieldMapping(msgId = 0x0009, dataType = WORD)
    private Short field0x0009;

    @ExtraField.NestedFieldMapping(msgId = 0x000b, dataType = WORD)
    private Short field0x000b;

    @ExtraField.NestedFieldMapping(msgId = 0x000c, dataType = WORD)
    private Short field0x000c;

    @ExtraField.NestedFieldMapping(msgId = 0x000d, dataType = WORD)
    private Short field0x000d;

    @ExtraField.NestedFieldMapping(msgId = 0x000e, dataType = BYTE)
    private Byte field0x000e;

    @ExtraField.NestedFieldMapping(msgId = 0x000F, dataType = BYTE)
    private Byte field0x000F;

    @ExtraField.NestedFieldMapping(msgId = 0x0050, dataType = STRING)
    private String field0x0050;

    @ExtraField.NestedFieldMapping(msgId = 0x0051, dataType = STRING)
    private String field0x0051;

    @ExtraField.NestedFieldMapping(msgId = 0x0052, dataType = DWORD)
    private Integer field0x0052;

    @ExtraField.NestedFieldMapping(msgId = 0x0100, dataType = WORD)
    private Short field0x0100;

    @ExtraField.NestedFieldMapping(msgId = 0x0101, dataType = DWORD)
    private Integer field0x0101;

    @ExtraField.NestedFieldMapping(msgId = 0x0102, dataType = WORD)
    private Short field0x0102;

    @ExtraField.NestedFieldMapping(msgId = 0x0103, dataType = DWORD)
    private Integer field0x0103;

    @ExtraField.NestedFieldMapping(msgId = 0x0104, dataType = WORD)
    private Short field0x0104;

    @ExtraField.NestedFieldMapping(msgId = 0x0105, dataType = DWORD)
    private Integer field0x0105;

    @ExtraField.NestedFieldMapping(msgId = 0x0106, dataType = WORD)
    private Short field0x0106;

    @ExtraField.NestedFieldMapping(msgId = 0x0107, dataType = DWORD)
    private Integer field0x0107;

    @ExtraField.NestedFieldMapping(msgId = 0x0108, dataType = WORD)
    private Short field0x0108;

    @ExtraField.NestedFieldMapping(msgId = 0x0109, dataType = DWORD)
    private Integer field0x0109;

    @ExtraField.NestedFieldMapping(msgId = 0x010a, dataType = WORD)
    private Short field0x010a;

    @ExtraField.NestedFieldMapping(msgId = 0x010b, dataType = DWORD)
    private Integer field0x010b;

    @ExtraField.NestedFieldMapping(msgId = 0x010c, dataType = WORD)
    private Short field0x010c;

    @ExtraField.NestedFieldMapping(msgId = 0x010d, dataType = WORD)
    private Short field0x010d;

    @ExtraField.NestedFieldMapping(msgId = 0x010e, dataType = WORD)
    private Short field0x010e;

    @ExtraField.NestedFieldMapping(msgId = 0x010f, dataType = WORD)
    private Short field0x010f;

    @ExtraField.NestedFieldMapping(msgId = 0x0110, dataType = WORD)
    private Short field0x0110;

    @ExtraField.NestedFieldMapping(msgId = 0x0111, dataType = WORD)
    private Short field0x0111;

    @ExtraField.NestedFieldMapping(msgId = 0x0112, dataType = WORD)
    private Short field0x0112;

    @ExtraField.NestedFieldMapping(msgId = 0x0113, dataType = WORD)
    private Short field0x0113;

    @ExtraField.NestedFieldMapping(msgId = 0x0114, dataType = WORD)
    private Short field0x0114;

    @ExtraField.NestedFieldMapping(msgId = 0x0115, dataType = WORD)
    private Short field0x0115;

    @ExtraField.NestedFieldMapping(msgId = 0x0116, dataType = WORD)
    private Short field0x0116;
}
