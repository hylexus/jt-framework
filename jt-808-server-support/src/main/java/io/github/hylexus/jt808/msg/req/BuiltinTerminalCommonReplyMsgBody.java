package io.github.hylexus.jt808.msg.req;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@BuiltinComponent
@Accessors(chain = true)
public class BuiltinTerminalCommonReplyMsgBody implements RequestMsgBody {
    private int replyFlowId;
    private int replyMsgId;
    private byte result;
}
