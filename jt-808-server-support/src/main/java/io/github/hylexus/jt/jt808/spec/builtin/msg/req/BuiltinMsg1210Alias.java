package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.LIST;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808RequestBody
@BuiltinComponent
public class BuiltinMsg1210Alias {

    // byte[0,7)
    @RequestFieldAlias.Bytes(order = 10, length = 7)
    private String terminalId;

    // byte[7,23)
    @RequestFieldAlias.Bytes(order = 20, length = 16)
    private String alarmIdentifier;

    // byte[23,55)
    @RequestFieldAlias.Bytes(order = 30, length = 32)
    private String alarmNo;

    // byte[55]
    @RequestFieldAlias.Byte(order = 40)
    private short messageType;

    // byte[56]
    @RequestFieldAlias.Byte(order = 50)
    private short attachmentCount;

    // byte[57,...)
    @RequestField(order = 60, dataType = LIST, lengthExpression = "#ctx.msgBodyLength() - 57")
    private List<AttachmentItem> attachmentItemList;

    @Data
    @NoArgsConstructor
    public static class AttachmentItem {
        @RequestFieldAlias.Byte(order = 10)
        private int length;

        @RequestFieldAlias.String(order = 20, lengthExpression = "#this.length")
        private String fileName;

        @RequestFieldAlias.Dword(order = 30)
        private long fileSize;
    }
}
