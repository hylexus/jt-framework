package io.github.hylexus.jt808.boot.props;

import io.github.hylexus.jt808.boot.props.dispatcher.MsgDispatcherProps;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

/**
 * @author hylexus
 * Created At 2019-08-28 11:51
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "jt808")
@Validated
public class Jt808ServerProps {

    @NestedConfigurationProperty
    private Jt808NettyTcpServerProps server = new Jt808NettyTcpServerProps();

    @NestedConfigurationProperty
    private MsgDispatcherProps msgDispatcher = new MsgDispatcherProps();
}
