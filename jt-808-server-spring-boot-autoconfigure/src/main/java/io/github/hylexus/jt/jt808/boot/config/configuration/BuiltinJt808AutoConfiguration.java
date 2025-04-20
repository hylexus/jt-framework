package io.github.hylexus.jt.jt808.boot.config.configuration;

import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * @author hylexus
 */
@Import({
        Jt808ServerCommonAutoConfiguration.class,
        Jt808InstructionServerAutoConfiguration.class,
        Jt808AttachmentServerAutoConfiguration.class,
})
@EnableConfigurationProperties({Jt808ServerProps.class})
public class BuiltinJt808AutoConfiguration {

}
