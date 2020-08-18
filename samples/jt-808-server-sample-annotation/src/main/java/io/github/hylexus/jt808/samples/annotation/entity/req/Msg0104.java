package io.github.hylexus.jt808.samples.annotation.entity.req;

import io.github.hylexus.jt.annotation.msg.req.Jt808ReqMsgBody;
import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import io.github.hylexus.jt.annotation.msg.req.extra.ExtraField;
import io.github.hylexus.jt.annotation.msg.req.extra.ExtraMsgBody;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgHeaderAware;
import lombok.Data;
import lombok.ToString;

import java.util.List;

import static io.github.hylexus.jt.data.MsgDataType.*;

/**
 * Created At 2020-08-10 12:54
 *
 * @author hylexus
 */
@Data
@Jt808ReqMsgBody(msgType = 0x0104)
public class Msg0104 implements RequestMsgBody, RequestMsgHeaderAware {
    // auto-inject
    @ToString.Exclude
    private RequestMsgHeader requestMsgHeader;

    @BasicField(startIndex = 0, dataType = MsgDataType.WORD)
    private int replyFlowId;

    @BasicField(startIndex = 2, dataType = MsgDataType.BYTE)
    private byte paramCount;

    @ExtraField(
            startIndex = 3,
            byteCountMethod = "getParamBodyBytesCount"
    )
    private ParamList paramList;

    public int getParamBodyBytesCount() {
        return requestMsgHeader.getMsgBodyLength() - 3;
    }

    @Data
    @ExtraMsgBody(
            byteCountOfMsgId = 4, // 消息Id用4个字节表示(DWORD)
            byteCountOfContentLength = 1 // 附加项长度字段用1个字节表示(BYTE)
    )
    public static class ParamList {
        // https://github.com/hylexus/jt-framework/issues/25
        // 这里写成List仅仅为了演示在msgId重复时可以使用List类型
        @ExtraField.NestedFieldMapping(msgId = 0x0001, dataType = LIST, itemDataType = DWORD)
        private List<Integer> field0x0001;

        @ExtraField.NestedFieldMapping(msgId = 0x0003, dataType = DWORD)
        private Integer field0x0003;

        @ExtraField.NestedFieldMapping(msgId = 0x0010, dataType = STRING)
        private String field0x0010;

        @ExtraField.NestedFieldMapping(msgId = 0x0013, dataType = STRING)
        private String field0x0013;

        @ExtraField.NestedFieldMapping(msgId = 0x0017, dataType = STRING)
        private String field0x0017;
    }
}
