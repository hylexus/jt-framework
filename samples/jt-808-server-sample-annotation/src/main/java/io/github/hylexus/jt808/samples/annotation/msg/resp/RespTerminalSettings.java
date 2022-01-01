package io.github.hylexus.jt808.samples.annotation.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

import static io.github.hylexus.jt.jt808.support.data.MsgDataType.*;

/**
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x8103, desc = "设置终端参数")
public class RespTerminalSettings {

    // 参数项列表
    @ResponseField(order = 2, dataType = MsgDataType.LIST)
    private List<ParamItem> paramList;

    // 参数总数
    @ResponseField(order = 1, dataType = BYTE)
    private int totalParamCount;

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class ParamItem {
        @ResponseField(order = 1, dataType = DWORD)
        private int msgId;

        @ResponseField(order = 2, dataType = BYTE)
        private int bytesCountOfContentLength;

        @ResponseField(order = 3, dataType = BYTES)
        private ByteBuf msgContent;

        public ParamItem(int msgId, ByteBuf msgContent) {
            this.msgId = msgId;
            this.msgContent = msgContent;
            this.bytesCountOfContentLength = msgContent.readableBytes();
        }
    }

}
