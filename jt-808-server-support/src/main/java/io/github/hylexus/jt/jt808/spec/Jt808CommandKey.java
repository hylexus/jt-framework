package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808CommandKey;
import io.github.hylexus.jt.utils.Jdk8Adapter;

import java.util.Optional;

import static io.github.hylexus.jt.utils.HexStringUtils.int2HexString;

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
        return Jdk8Adapter.optionalIsEmpty(serverFlowId())
                ? String.format("%s_%s", terminalId(), int2HexString(terminalReplyMsgId(), 4))
                : String.format("%s_%s_%s", terminalId(), int2HexString(terminalReplyMsgId(), 4), serverFlowId().get());
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
