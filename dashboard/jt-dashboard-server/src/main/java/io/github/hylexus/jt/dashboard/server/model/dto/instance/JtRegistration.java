package io.github.hylexus.jt.dashboard.server.model.dto.instance;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public abstract class JtRegistration {
    private String name;
    private String type;
    private String baseUrl;
    private String source;
    private Map<String, String> metadata;
}
