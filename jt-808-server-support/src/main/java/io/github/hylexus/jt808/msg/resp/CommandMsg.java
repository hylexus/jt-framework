package io.github.hylexus.jt808.msg.resp;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.utils.Assertions;
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
    private Jt808ProtocolVersion version;

    public CommandMsg() {
    }

    /**
     * @deprecated 使用 {@link #CommandMsg(String, MsgType, Object, Jt808ProtocolVersion)} 代替(明确指定版本)
     */
    @Deprecated
    public CommandMsg(String terminalId, MsgType expectedReplyMsgType, Object body) {
        this(terminalId, expectedReplyMsgType, body, Jt808ProtocolVersion.VERSION_2011);
    }

    public CommandMsg(String terminalId, MsgType expectedReplyMsgType, Object body, Jt808ProtocolVersion version) {
        this.terminalId = terminalId;
        this.expectedReplyMsgType = expectedReplyMsgType;
        this.body = body;
        Assertions.isTrue(version != null && version != Jt808ProtocolVersion.AUTO_DETECTION, "消息下发时应该明确指定消息协议版本");
        this.version = version;
    }

    /**
     * @deprecated 使用 {@link #of(String, MsgType, Object, Jt808ProtocolVersion)} 代替(明确指定版本)
     */
    @Deprecated
    public static CommandMsg of(String terminalId, MsgType expectedReplyMsgType, Object body) {
        return of(terminalId, expectedReplyMsgType, body, Jt808ProtocolVersion.VERSION_2011);
    }

    public static CommandMsg of(String terminalId, MsgType expectedReplyMsgType, Object body, Jt808ProtocolVersion version) {
        return new CommandMsg(terminalId, expectedReplyMsgType, body, version);
    }

    /**
     * @deprecated 使用 {@link #emptyRespMsgBody(String, MsgType, Jt808ProtocolVersion)} 代替(明确指定版本)
     */
    @Deprecated
    public static CommandMsg emptyRespMsgBody(String terminalId, MsgType respMsgType) {
        return emptyRespMsgBody(terminalId, respMsgType, Jt808ProtocolVersion.VERSION_2011);
    }

    public static CommandMsg emptyRespMsgBody(String terminalId, MsgType respMsgType, Jt808ProtocolVersion version) {
        return of(terminalId, respMsgType, null, version);
    }

    public static CommandMsg emptyRespMsgBody(String terminalId, MsgType expectedReplyMsgType, MsgType respMsgType) {
        return of(terminalId, expectedReplyMsgType, new EmptyRespMsgBody(respMsgType), Jt808ProtocolVersion.VERSION_2011);
    }

}
