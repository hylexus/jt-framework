package io.github.hylexus.jt.command;

import io.github.hylexus.jt.data.msg.MsgType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author hylexus
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Jt808CommandKey implements CommandKey {

    /**
     * 终端ID
     */
    private String terminalId;
    /**
     * 终端对下发消息的回复类型
     */
    private MsgType expectedReplyMsgType;
    /**
     * 流水号
     */
    @Nullable
    private Integer serverFlowId;

    private Jt808CommandKey() {
    }

    public static Jt808CommandKey of(String terminalId, MsgType expectedReplyMsgType) {
        return of(terminalId, expectedReplyMsgType, null);
    }

    public static Jt808CommandKey of(String terminalId, MsgType expectedReplyMsgType, Integer flowId) {
        Objects.requireNonNull(expectedReplyMsgType, "expectedReplyMsgType is null");
        return new Jt808CommandKey().setExpectedReplyMsgType(expectedReplyMsgType).setTerminalId(terminalId).setServerFlowId(flowId);
    }

    @Override
    public int getMsgId() {
        return expectedReplyMsgType.getMsgId();
    }

    @Override
    public String getTerminalId() {
        return terminalId;
    }

    @Nullable
    public Integer getServerFlowId() {
        return serverFlowId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Jt808CommandKey that = (Jt808CommandKey) o;
        return Objects.equals(terminalId, that.terminalId)
               && Objects.equals(expectedReplyMsgType.getMsgId(), that.expectedReplyMsgType.getMsgId())
               && Objects.equals(serverFlowId, that.serverFlowId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terminalId, getMsgId(), serverFlowId);
    }
}
