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
 * Created At 2019-10-09 9:22 下午
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Jt808CommandKey implements CommandKey {

    private static final String PREFIX = "jt808:";

    /**
     * 终端ID
     */
    private String terminalId;
    /**
     * 终端对下发消息的回复类型
     */
    private MsgType msgType;
    /**
     * 流水号
     */
    @Nullable
    private Integer flowId;

    private Jt808CommandKey() {
    }

    public static Jt808CommandKey of(String terminalId, MsgType msgType) {
        return of(terminalId, msgType, null);
    }

    /***
     *
     * @deprecated User {@link #of(String, MsgType)} instead.
     */
    @Deprecated
    public static Jt808CommandKey of(MsgType msgType, String terminalId) {
        return of(terminalId, msgType);
    }

    public static Jt808CommandKey of(String terminalId, MsgType msgType, Integer flowId) {
        Objects.requireNonNull(msgType, "msgType is null");
        return new Jt808CommandKey().setMsgType(msgType).setTerminalId(terminalId).setFlowId(flowId);
    }

    /**
     * @deprecated Use {@link #of(String, MsgType, Integer)} instead.
     */
    @Deprecated
    public static Jt808CommandKey of(MsgType msgType, String terminalId, Integer flowId) {
        return of(terminalId, msgType, flowId);
    }

    @Override
    public int getMsgId() {
        return msgType.getMsgId();
    }

    @Override
    public String getTerminalId() {
        return terminalId;
    }

    @Nullable
    @Override
    public Integer getFlowId() {
        return flowId;
    }

    //    @Override
    //    public String getKeyAsString() {
    //        return PREFIX + CommandKey.super.getKeyAsString();
    //    }

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
