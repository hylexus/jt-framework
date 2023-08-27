package io.github.hylexus.jt.dashboard.server.model.values;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class AbstractStaticJtApplicationProps {
    private String name;
    private String baseUrl;
    private Map<String, String> metadata;
}
