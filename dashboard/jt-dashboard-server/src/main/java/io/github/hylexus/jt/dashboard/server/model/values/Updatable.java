package io.github.hylexus.jt.dashboard.server.model.values;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public interface Updatable {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime getUpdatedAt();
}
