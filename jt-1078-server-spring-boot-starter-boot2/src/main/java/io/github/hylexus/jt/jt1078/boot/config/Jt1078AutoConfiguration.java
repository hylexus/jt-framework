package io.github.hylexus.jt.jt1078.boot.config;

import io.github.hylexus.jt.jt1078.boot.config.configuration.Jt1078NettyServerAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author hylexus
 */
@Import({
        Jt1078NettyServerAutoConfiguration.class
})
public class Jt1078AutoConfiguration {
}
