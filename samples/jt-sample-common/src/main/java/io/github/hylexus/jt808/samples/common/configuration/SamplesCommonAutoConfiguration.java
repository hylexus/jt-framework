package io.github.hylexus.jt808.samples.common.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;

@Import({
        SamplesReactiveAutoConfiguration.class,
})
@ConditionalOnProperty(prefix = "jt-samples.common-config", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SamplesCommonAutoConfiguration {
}
