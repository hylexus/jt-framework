package io.github.hylexus.jt.jt808.boot2.config;

import io.github.hylexus.jt.jt808.boot.config.configuration.BuiltinJt808AutoConfiguration;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * @author hylexus
 */
@Import(BuiltinJt808AutoConfiguration.class)
@EnableConfigurationProperties({Jt808ServerProps.class})
public class Jt808AutoConfiguration {

}
