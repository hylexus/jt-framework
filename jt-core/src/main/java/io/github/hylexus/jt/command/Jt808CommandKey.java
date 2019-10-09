package io.github.hylexus.jt.command;

import io.github.hylexus.jt.data.msg.MsgType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * @author hylexus
 * Created At 2019-10-09 9:22 下午
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Jt808CommandKey implements CommandKey {

    private static final String PREFIX = "jt808:";

    private String terminalId;
    private MsgType msgType;
    private Integer flowId;

    private Jt808CommandKey() {
    }

    public static Jt808CommandKey of(MsgType msgType, String terminalId) {
        return of(msgType, terminalId, null);
    }

    public static Jt808CommandKey of(MsgType msgType, String terminalId, Integer flowId) {
        return new Jt808CommandKey().setMsgType(msgType).setTerminalId(terminalId).setFlowId(flowId);
    }

    @Override
    public int getMsgId() {
        return msgType.getMsgId();
    }

    @Override
    public String getTerminalId() {
        return terminalId;
    }

    @Override
    public Integer getFlowId() {
        return flowId;
    }

    @Override
    public String getKeyAsString() {
        return PREFIX + CommandKey.super.getKeyAsString();
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
                && Objects.equals(msgType.getMsgId(), that.msgType.getMsgId())
                && Objects.equals(flowId, that.flowId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terminalId, getMsgId(), flowId);
    }
}
