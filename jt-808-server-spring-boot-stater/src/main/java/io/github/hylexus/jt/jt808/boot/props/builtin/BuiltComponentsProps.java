package io.github.hylexus.jt.jt808.boot.props.builtin;

import io.github.hylexus.jt.jt808.boot.props.builtin.handler.BuiltinHandlerProps;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author hylexus
 */
@Data
public class BuiltComponentsProps {
    @NestedConfigurationProperty
    private BuiltinHandlerProps requestHandlers = new BuiltinHandlerProps();
}
