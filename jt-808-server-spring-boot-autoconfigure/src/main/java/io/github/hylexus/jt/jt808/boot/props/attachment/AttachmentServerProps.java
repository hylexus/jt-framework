package io.github.hylexus.jt.jt808.boot.props.attachment;

import io.github.hylexus.jt.jt808.boot.props.msg.processor.MsgProcessorProps;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.validation.constraints.Min;

@Data
@ToString
public class AttachmentServerProps {
    private boolean enabled = false;
    private int port = 6809;

    @Min(value = 0, message = "bossThreadCount >= 0, 0 means that Netty's default logical")
    private int bossThreadCount = 0;

    @Min(value = 0, message = "workerThreadCount >= 0, 0 means that Netty's default logical")
    private int workerThreadCount = 0;

    private int maxFrameLength = 1024 * 65;

    @NestedConfigurationProperty
    private MsgProcessorProps msgProcessor = new MsgProcessorProps();

    @NestedConfigurationProperty
    private IdleStateHandlerProps idleStateHandler = new IdleStateHandlerProps();

}
