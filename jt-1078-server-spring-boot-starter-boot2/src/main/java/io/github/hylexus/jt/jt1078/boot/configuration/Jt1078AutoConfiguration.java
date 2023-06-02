package io.github.hylexus.jt.jt1078.boot.configuration;

import io.github.hylexus.jt.jt1078.boot.configuration.codec.Jt1078CodecAutoConfiguration;
import io.github.hylexus.jt.jt1078.boot.configuration.handler.Jt1078HandlerAutoConfiguration;
import io.github.hylexus.jt.jt1078.boot.configuration.netty.Jt1078NettyAutoConfiguration;
import io.github.hylexus.jt.jt1078.boot.props.Jt1078ServerProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author hylexus
 */
@Configuration
@Import({
        Jt1078CodecAutoConfiguration.class,
        Jt1078HandlerAutoConfiguration.class,
        Jt1078NettyAutoConfiguration.class,
})
@EnableConfigurationProperties({
        Jt1078ServerProps.class,
})
public class Jt1078AutoConfiguration {
}
