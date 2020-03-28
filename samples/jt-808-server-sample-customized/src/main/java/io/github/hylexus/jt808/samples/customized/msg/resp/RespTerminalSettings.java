package io.github.hylexus.jt808.samples.customized.msg.resp;

import io.github.hylexus.jt.annotation.msg.resp.CommandField;
import io.github.hylexus.jt.annotation.msg.resp.Jt808RespMsgBody;
import io.github.hylexus.jt.data.resp.BytesValueWrapper;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import static io.github.hylexus.jt.data.MsgDataType.BYTE;
import static io.github.hylexus.jt.data.MsgDataType.DWORD;

/**
 * @author hylexus
 * Created At 2019-10-16 10:43 下午
 */
@Data
@Accessors(chain = true)
@Jt808RespMsgBody(respMsgId = 0x8103, desc = "设置终端参数")
public class RespTerminalSettings {

    @CommandField(order = 2)
    private List<ParamItem> paramList;

    @CommandField(order = 1, targetMsgDataType = BYTE)
    private int totalParamCount;

    @Data
    @Accessors(chain = true)
    @SuppressWarnings("rawtypes")
    public static class ParamItem {
        @CommandField(order = 1, targetMsgDataType = DWORD)
        private int msgId;

        @CommandField(order = 2, targetMsgDataType = BYTE)
        private int bytesCountOfContentLength;

        @CommandField(order = 3)
        private BytesValueWrapper msgContent;

        public ParamItem(int msgId, BytesValueWrapper msgContent) {
            this.msgId = msgId;
            this.msgContent = msgContent;
            this.bytesCountOfContentLength = msgContent.getAsBytes().length;
        }
    }

}
