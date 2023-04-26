package io.github.hylexus.jt.jt808.boot.props.plugin;

import io.github.hylexus.jt.jt808.boot.props.plugin.filter.Jt808RequestFilterProps;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
public class Jt808PluginProps {

    @NestedConfigurationProperty
    private Jt808RequestFilterProps requestFilter = new Jt808RequestFilterProps();

}
