package io.github.hylexus.jt.dashboard.server.model.values.instance;

import io.github.hylexus.jt.dashboard.server.model.values.Updatable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class JtInstanceStatus implements Updatable {
    public static final String STATUS_UP = "UP";
    public static final String STATUS_DOWN = "DOWN";
    public static final String STATUS_UNKNOWN = "UNKNOWN";
    protected LocalDateTime updatedAt;
    // UP, DOWN, UNKNOWN
    protected String status;
}
