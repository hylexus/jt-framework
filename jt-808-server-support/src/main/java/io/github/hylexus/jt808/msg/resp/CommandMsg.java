package io.github.hylexus.jt808.msg.resp;

import io.github.hylexus.jt.data.msg.MsgType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author hylexus
 * Created At 2020-03-14 9:43 下午
 */
@Getter
@Setter
@ToString(of = {"terminalId", "expectedReplyMsgType"})
public class CommandMsg {

    private String terminalId;
    /**
     * 客户端对该下发指令的回复类型
     */
    private MsgType expectedReplyMsgType;
    private Object body;

    public CommandMsg() {
    }

    public CommandMsg(String terminalId, MsgType expectedReplyMsgType, Object body) {
        this.terminalId = terminalId;
        this.expectedReplyMsgType = expectedReplyMsgType;
        this.body = body;
    }

    public static CommandMsg emptyRespMsgBody(String terminalId, MsgType respMsgType) {
        return emptyRespMsgBody(terminalId, null, respMsgType);
    }

    public static CommandMsg emptyRespMsgBody(String terminalId, MsgType expectedReplyMsgType, MsgType respMsgType) {
        return new CommandMsg(terminalId, expectedReplyMsgType, new EmptyRespMsgBody(respMsgType));
    }

    public static CommandMsg of(String terminalId, MsgType expectedReplyMsgType, Object body) {
        return new CommandMsg(terminalId, expectedReplyMsgType, body);
    }
}
