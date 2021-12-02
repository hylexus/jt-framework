package io.github.hylexus.jt.jt808.response;

import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.jt808.response.impl.DefaultJt808CommandKey;

/**
 * Created At 2019-10-09 9:19 下午
 *
 * @author hylexus
 */
public interface Jt808CommandKey {

    String terminalId();

    int terminalReplyMsgId();

    Integer serverFlowId();

    default String getKeyAsString() {
        return serverFlowId() == null
                ? String.format("%s_%s", terminalId(), terminalReplyMsgId())
                : String.format("%s_%s_%s", terminalId(), terminalReplyMsgId(), serverFlowId());
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
