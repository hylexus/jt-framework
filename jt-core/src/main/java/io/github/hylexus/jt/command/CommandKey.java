package io.github.hylexus.jt.command;

/**
 * @author hylexus
 */
public interface CommandKey {
    String getTerminalId();

    int getMsgId();

    Integer getServerFlowId();

    default String getKeyAsString() {
        return getServerFlowId() == null
                ? String.format("%s_%s", getTerminalId(), getMsgId())
                : String.format("%s_%s_%s", getTerminalId(), getMsgId(), getServerFlowId());
    }

    default String getWaitingFlag() {
        return "_" + getKeyAsString();
    }
}
