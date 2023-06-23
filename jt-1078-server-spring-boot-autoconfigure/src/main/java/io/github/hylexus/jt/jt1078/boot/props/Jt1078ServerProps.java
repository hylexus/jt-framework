package io.github.hylexus.jt.jt1078.boot.props;


import io.github.hylexus.jt.jt1078.boot.props.msg.processor.MsgProcessorProps;
import io.github.hylexus.jt.jt1078.boot.props.protocol.ProtocolConfig;
import io.github.hylexus.jt.jt1078.boot.props.server.Jt1078NettyTcpServerProps;
import io.github.hylexus.jt.jt1078.boot.props.subpackage.RequestSubPackageCombinerProps;
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
@ConfigurationProperties(prefix = "jt1078")
@Validated
public class Jt1078ServerProps {

    private boolean enabled = true;

    @NestedConfigurationProperty
    private ProtocolConfig protocol = new ProtocolConfig();

    @NestedConfigurationProperty
    private Jt1078NettyTcpServerProps server = new Jt1078NettyTcpServerProps();

    @NestedConfigurationProperty
    private RequestSubPackageCombinerProps requestSubPackageCombiner = new RequestSubPackageCombinerProps();

    /**
     * 消息处理器线程池配置
     */
    @NestedConfigurationProperty
    private MsgProcessorProps msgProcessor = new MsgProcessorProps();

}
