package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.type.byteseq.ByteArrayContainer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x8103)
public class BuiltinMsg8103 {

    @ResponseField(order = 100, dataType = MsgDataType.BYTE)

    private int paramCount;

    @ResponseField(order = 200, dataType = MsgDataType.LIST)
    private List<ParamItem> paramItemList;

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class ParamItem {

        @ResponseField(order = 100, dataType = MsgDataType.DWORD)
        private long msgId;

        @ResponseField(order = 200, dataType = MsgDataType.BYTE)
        private int msgLength;

        @ResponseField(order = 300, dataType = MsgDataType.BYTES)
        private ByteArrayContainer msgContent;

        public ParamItem(long msgId, ByteArrayContainer msgContent) {
            this.msgId = msgId;
            this.msgContent = msgContent;
            this.msgLength = msgContent.length();
        }
    }
}
