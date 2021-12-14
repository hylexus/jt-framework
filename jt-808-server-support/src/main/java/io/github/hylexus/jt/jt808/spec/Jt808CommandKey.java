package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808CommandKey;

import java.util.Optional;

/**
 * Created At 2019-10-09 9:19 下午
 *
 * @author hylexus
 */
public interface Jt808CommandKey {

    String terminalId();

    int terminalReplyMsgId();

    Optional<Integer> serverFlowId();

    default String getKeyAsString() {
        return serverFlowId().isEmpty()
                ? String.format("%s_%s", terminalId(), terminalReplyMsgId())
                : String.format("%s_%s_%s", terminalId(), terminalReplyMsgId(), serverFlowId().get());
    }

    default String getWaitingFlag() {
        return "_" + getKeyAsString();
    }

    static Jt808CommandKey of(String terminalId, MsgType msgType, Integer flowId) {
        return DefaultJt808CommandKey.of(terminalId, msgType, flowId);
    }

    static Jt808CommandKey of(String terminalId, MsgType msgType) {
        return DefaultJt808CommandKey.of(terminalId, msgType);
    }
}
