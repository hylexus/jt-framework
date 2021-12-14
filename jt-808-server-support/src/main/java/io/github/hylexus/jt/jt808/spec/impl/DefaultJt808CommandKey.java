package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808CommandKey;
import io.github.hylexus.jt.jt808.spec.MsgType;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hylexus
 */
@ToString
public class DefaultJt808CommandKey implements Jt808CommandKey {

    /**
     * 终端ID
     */
    private final String terminalId;
    /**
     * 终端对下发消息的回复类型
     */
    private final MsgType terminalReplyMsgId;
    /**
     * 流水号
     */
    @Nullable
    private final Integer serverFlowId;

    private DefaultJt808CommandKey(String terminalId, MsgType terminalReplyMsgId, @Nullable Integer serverFlowId) {
        this.terminalId = terminalId;
        this.terminalReplyMsgId = terminalReplyMsgId;
        this.serverFlowId = serverFlowId;
    }

    public static DefaultJt808CommandKey of(String terminalId, MsgType expectedReplyMsgType) {
        return of(terminalId, expectedReplyMsgType, null);
    }

    public static DefaultJt808CommandKey of(String terminalId, MsgType expectedReplyMsgType, Integer flowId) {
        Objects.requireNonNull(expectedReplyMsgType, "expectedReplyMsgType is null");
        return new DefaultJt808CommandKey(terminalId, expectedReplyMsgType, flowId);
    }

    @Override
    public int terminalReplyMsgId() {
        return terminalReplyMsgId.getMsgId();
    }

    @Override
    public String terminalId() {
        return terminalId;
    }

    @Nullable
    public Optional<Integer> serverFlowId() {
        return Optional.ofNullable(serverFlowId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultJt808CommandKey that = (DefaultJt808CommandKey) o;
        return Objects.equals(terminalId, that.terminalId)
               && Objects.equals(terminalReplyMsgId.getMsgId(), that.terminalReplyMsgId.getMsgId())
               && Objects.equals(serverFlowId, that.serverFlowId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terminalId, terminalReplyMsgId(), serverFlowId);
    }
}
