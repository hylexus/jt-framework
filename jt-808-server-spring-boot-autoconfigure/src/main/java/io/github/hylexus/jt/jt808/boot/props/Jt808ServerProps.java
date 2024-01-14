package io.github.hylexus.jt.jt808.boot.props;

import io.github.hylexus.jt.jt808.boot.props.attachment.AttachmentServerProps;
import io.github.hylexus.jt.jt808.boot.props.builtin.BuiltComponentsProps;
import io.github.hylexus.jt.jt808.boot.props.builtin.RequestSubPackageStorageProps;
import io.github.hylexus.jt.jt808.boot.props.builtin.ResponseSubPackageStorageProps;
import io.github.hylexus.jt.jt808.boot.props.feature.Jt808FeatureProps;
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

    /**
     * 内置组件配置
     */
    @NestedConfigurationProperty
    private BuiltComponentsProps builtComponents = new BuiltComponentsProps();

    /**
     * 请求消息分包配置
     */
    @NestedConfigurationProperty
    private RequestSubPackageStorageProps requestSubPackageStorage = new RequestSubPackageStorageProps();

    /**
     * 响应消息分包配置
     */
    @NestedConfigurationProperty
    private ResponseSubPackageStorageProps responseSubPackageStorage = new ResponseSubPackageStorageProps();

    @NestedConfigurationProperty
    private ProtocolConfig protocol = new ProtocolConfig();

    @NestedConfigurationProperty
    private Jt808NettyTcpServerProps server = new Jt808NettyTcpServerProps();

    /**
     * 消息处理器线程池配置
     */
    @NestedConfigurationProperty
    private MsgProcessorProps msgProcessor = new MsgProcessorProps();

    @NestedConfigurationProperty
    private Jt808FeatureProps features = new Jt808FeatureProps();

    @NestedConfigurationProperty
    private AttachmentServerProps attachmentServer = new AttachmentServerProps();
}
