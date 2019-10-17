package io.github.hylexus.jt808.server.msg.resp;

import io.github.hylexus.jt.annotation.msg.resp.BasicRespField;
import io.github.hylexus.jt.annotation.msg.resp.Jt808RespMsgBody;
import io.github.hylexus.jt.data.resp.DataType;
import lombok.Data;

import java.util.List;

import static io.github.hylexus.jt.data.MsgDataType.*;

/**
 * @author hylexus
 * Created At 2019-10-16 10:43 下午
 */
@Data
@Jt808RespMsgBody(msgType = 0x01)
public class RespTerminalSettings {

    @BasicRespField(order = 1, dataType = BYTE)
    private byte totalParamCount;

    @BasicRespField(order = 2, isNested = true)
    private List<ParamItem> paramList;

    @Data
    public static class ParamItem {
        @BasicRespField(order = 1, dataType = DWORD)
        private int msgId;

        @BasicRespField(order = 2, dataType = BYTE)
        private byte msgLength;

        @BasicRespField(order = 3, dataType = BYTES)
        private byte[] byteValue;

        @BasicRespField(order = 3, dataType = UNKNOWN)
        private Object value;
    }


    class Item {
        private DataType msgId;
        private DataType content;
        private int bytesCountLength;
        private int contentLength;

        public int getContentLength() {
            return contentLength;
        }
    }
}
