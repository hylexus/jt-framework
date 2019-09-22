package io.github.hylexus.jt808.msg.req;

import io.github.hylexus.jt808.msg.RequestMsgBody;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TerminalCommonReplyMsgBody implements RequestMsgBody {
    private int replyFlowId;
    private int replyMsgId;
    private byte result;
}