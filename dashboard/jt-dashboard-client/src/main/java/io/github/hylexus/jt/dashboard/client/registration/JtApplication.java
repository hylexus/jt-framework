package io.github.hylexus.jt.dashboard.client.registration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public abstract class JtApplication {
    protected String name;
    protected String type;
    protected String baseUrl;
    protected Map<String, String> metadata;

    public JtApplication(String name, String type, String baseUrl, Map<String, String> metadata) {
        this.name = name;
        this.type = type;
        this.baseUrl = baseUrl;
        this.metadata = metadata;
    }

    public enum Type {
        JT_1078,
        JT_808
    }
}
