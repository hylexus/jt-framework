package io.github.hylexus.jt.demos.common.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;

@Import({
        DemosReactiveAutoConfiguration.class,
})
@ConditionalOnProperty(prefix = "jt-samples.common-config", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DemosCommonAutoConfiguration {
}
