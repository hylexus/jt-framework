package io.github.hylexus.jt.jt808.boot.props.builtin;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author hylexus
 */
@Data
public class BuiltComponentsProps {

    /**
     * 内置的请求处理器
     */
    @NestedConfigurationProperty
    private Switchable requestHandlers = new Switchable();

    /**
     * 组件统计
     */
    @NestedConfigurationProperty
    private Switchable componentStatistics = new Switchable();

}
