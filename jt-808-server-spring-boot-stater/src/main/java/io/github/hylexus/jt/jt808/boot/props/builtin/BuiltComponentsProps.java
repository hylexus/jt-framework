package io.github.hylexus.jt.jt808.boot.props.builtin;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author hylexus
 */
@Data
public class BuiltComponentsProps {

    @NestedConfigurationProperty
    private Switchable requestHandlers = new Switchable();

    @NestedConfigurationProperty
    private Switchable componentStatistics = new Switchable();


}
