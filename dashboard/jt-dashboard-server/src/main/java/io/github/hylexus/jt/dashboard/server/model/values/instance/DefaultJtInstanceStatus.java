package io.github.hylexus.jt.dashboard.server.model.values.instance;

import java.time.LocalDateTime;

public class DefaultJtInstanceStatus extends JtInstanceStatus {
    public static JtInstanceStatus up() {
        return createDefaultInstance(STATUS_UP, LocalDateTime.now());
    }

    public static JtInstanceStatus down() {
        return createDefaultInstance(STATUS_DOWN, LocalDateTime.now());
    }

    public static JtInstanceStatus unknown() {
        return createDefaultInstance(STATUS_UNKNOWN, LocalDateTime.now());
    }

    private static DefaultJtInstanceStatus createDefaultInstance(String statusString, LocalDateTime updatedAt) {
        final DefaultJtInstanceStatus instanceStatus = new DefaultJtInstanceStatus();
        instanceStatus.setStatus(statusString);
        instanceStatus.setUpdatedAt(updatedAt);
        return instanceStatus;
    }
}
