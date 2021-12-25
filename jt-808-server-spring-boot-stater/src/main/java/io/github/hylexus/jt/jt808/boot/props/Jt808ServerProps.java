package io.github.hylexus.jt.jt808.boot.props;

import io.github.hylexus.jt.jt808.boot.props.builtin.BuiltComponentsProps;
import io.github.hylexus.jt.jt808.boot.props.msg.processor.MsgProcessorProps;
import io.github.hylexus.jt.jt808.boot.props.protocol.ProtocolConfig;
import io.github.hylexus.jt.jt808.boot.props.server.Jt808NettyTcpServerProps;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "jt808")
@Validated
public class Jt808ServerProps {

    @NestedConfigurationProperty
    private BuiltComponentsProps builtComponents = new BuiltComponentsProps();

    @NestedConfigurationProperty
    private ProtocolConfig protocol = new ProtocolConfig();

    @NestedConfigurationProperty
    private Jt808NettyTcpServerProps server = new Jt808NettyTcpServerProps();

    @NestedConfigurationProperty
    private MsgProcessorProps msgProcessor = new MsgProcessorProps();
}
