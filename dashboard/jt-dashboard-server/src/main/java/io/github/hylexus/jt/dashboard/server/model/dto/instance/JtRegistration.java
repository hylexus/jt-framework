package io.github.hylexus.jt.dashboard.server.model.dto.instance;

import io.github.hylexus.jt.dashboard.server.model.values.Updatable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@ToString
public abstract class JtRegistration implements Updatable {
    protected LocalDateTime updatedAt = LocalDateTime.now();
    private String name;
    private String type;
    private String baseUrl;
    private String source;
    private Map<String, String> metadata;
}
