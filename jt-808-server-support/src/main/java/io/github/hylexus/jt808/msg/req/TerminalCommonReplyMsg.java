package io.github.hylexus.jt808.msg.req;


import io.github.hylexus.jt808.msg.AbstractRequestMsg;

/**
 * @author hylexus
 * createdAt 2019/1/4
 **/
public class TerminalCommonReplyMsg extends AbstractRequestMsg {

    private TerminalCommonReplyMsgBody body;

    public TerminalCommonReplyMsg(AbstractRequestMsg other) {
        super(other);
    }

    public TerminalCommonReplyMsgBody getBody() {
        return body;
    }

    public TerminalCommonReplyMsg setBody(TerminalCommonReplyMsgBody body) {
        this.body = body;
        return this;
    }

    public static class TerminalCommonReplyMsgBody {
        private int replyFlowId;
        private int replyMsgId;
        private byte result;

        public int getReplyFlowId() {
            return replyFlowId;
        }

        public TerminalCommonReplyMsgBody setReplyFlowId(int replyFlowId) {
            this.replyFlowId = replyFlowId;
            return this;
        }

        public int getReplyMsgId() {
            return replyMsgId;
        }

        public TerminalCommonReplyMsgBody setReplyMsgId(int replyMsgId) {
            this.replyMsgId = replyMsgId;
            return this;
        }

        public byte getResult() {
            return result;
        }

        public TerminalCommonReplyMsgBody setResult(byte result) {
            this.result = result;
            return this;
        }

        @Override
        public String toString() {
            return "TerminalCommonReplyMsgBody{"
                    + "replyFlowId=" + replyFlowId
                    + ", replyMsgId=" + replyMsgId
                    + ", result=" + result
                    + '}';
        }
    }
}
