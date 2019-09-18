package io.github.hylexus.jt808.msg.req;


import io.github.hylexus.jt808.msg.AbstractRequestMsg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * createdAt 2019/1/4
 **/
@Getter
@Setter
@Accessors(chain = true)
public class TerminalCommonReplyMsg extends AbstractRequestMsg {

    private TerminalCommonReplyMsgBody body;

    public TerminalCommonReplyMsg(AbstractRequestMsg other) {
        super(other);
    }

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class TerminalCommonReplyMsgBody {
        private int replyFlowId;
        private int replyMsgId;
        private byte result;
    }
}
