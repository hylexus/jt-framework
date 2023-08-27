package io.github.hylexus.jt.dashboard.client.registration;

import io.github.hylexus.jt.dashboard.client.registration.JtApplication;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Jt808Application extends JtApplication {

    @Builder
    public Jt808Application(String name, String type, String baseUrl, String source, Map<String, String> metadata) {
        super(name, type, baseUrl, metadata);
    }
}
