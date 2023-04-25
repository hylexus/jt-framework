package io.github.hylexus.jt.jt808.spec.builtin.msg.resp;

import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseFieldAlias;
import io.github.hylexus.jt.jt808.support.data.type.bytebuf.ByteBufContainer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Jt808ResponseBody(msgId = 0x8103)
public class BuiltinMsg8103Alias {

    @ResponseFieldAlias.Byte(order = 100)
    private int paramCount;

    @ResponseFieldAlias.List(order = 200)
    private List<ParamItem> paramItemList;

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class ParamItem {

        @ResponseFieldAlias.Dword(order = 100)
        private long msgId;

        @ResponseFieldAlias.Byte(order = 200)
        private int msgLength;

        @ResponseFieldAlias.Bytes(order = 300)
        private ByteBufContainer msgContent;

        public ParamItem(long msgId, ByteBufContainer msgContent) {
            this.msgId = msgId;
            this.msgContent = msgContent;
            this.msgLength = msgContent.length();
        }
    }
}
