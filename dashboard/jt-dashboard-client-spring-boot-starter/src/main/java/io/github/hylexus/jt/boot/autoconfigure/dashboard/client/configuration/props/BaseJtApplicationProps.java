package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public abstract class BaseJtApplicationProps {
    protected boolean enabled = true;
    protected String[] serverUrl;
    protected boolean registerOnce = true;
    @Value("${spring.application.name}")
    protected String name;
    protected String baseUrl;
    protected String source;
    protected Map<String, String> metadata = new LinkedHashMap<>();
}
