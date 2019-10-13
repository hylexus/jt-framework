package io.github.hylexus.jt.command;

/**
 * @author hylexus
 * Created At 2019-10-09 9:19 下午
 */
public interface CommandKey {
    String getTerminalId();

    int getMsgId();

    Integer getFlowId();

    default String getKeyAsString() {
        return getFlowId() == null
                ? String.format("%s_%s", getTerminalId(), getMsgId())
                : String.format("%s_%s_%s", getTerminalId(), getMsgId(), getFlowId());
    }

    default String getWaitingFlag() {
        return "_" + getKeyAsString();
    }
}
