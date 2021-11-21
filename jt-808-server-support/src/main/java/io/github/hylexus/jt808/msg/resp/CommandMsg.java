package io.github.hylexus.jt808.msg.resp;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.utils.Assertions;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author hylexus
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
    private Jt808ProtocolVersion version;

    public CommandMsg() {
    }

    public CommandMsg(String terminalId, MsgType expectedReplyMsgType, Object body, Jt808ProtocolVersion version) {
        this.terminalId = terminalId;
        this.expectedReplyMsgType = expectedReplyMsgType;
        this.body = body;
        Assertions.isTrue(version != null && version != Jt808ProtocolVersion.AUTO_DETECTION, "消息下发时应该明确指定消息协议版本");
        this.version = version;
    }

    public static CommandMsg of(String terminalId, MsgType expectedReplyMsgType, Object body, Jt808ProtocolVersion version) {
        return body == null
                ? emptyRespMsgBody(terminalId, expectedReplyMsgType, version)
                : new CommandMsg(terminalId, expectedReplyMsgType, body, version);
    }

    public static CommandMsg emptyRespMsgBody(String terminalId, MsgType respMsgType, Jt808ProtocolVersion version) {
        return of(terminalId, respMsgType, new EmptyRespMsgBody(respMsgType), version);
    }

}
