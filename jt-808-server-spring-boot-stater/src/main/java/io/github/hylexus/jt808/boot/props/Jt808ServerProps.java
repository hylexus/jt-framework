package io.github.hylexus.jt808.boot.props;

import io.github.hylexus.jt808.boot.props.converter.scan.Jt808ConverterScanProps;
import io.github.hylexus.jt808.boot.props.entity.scan.Jt808EntityScanProps;
import io.github.hylexus.jt808.boot.props.exception.handler.scan.Jt808ExceptionHandlerScanProps;
import io.github.hylexus.jt808.boot.props.handler.scan.Jt808HandlerScanProps;
import io.github.hylexus.jt808.boot.props.msg.processor.MsgProcessorProps;
import io.github.hylexus.jt808.boot.props.protocol.ProtocolConfig;
import io.github.hylexus.jt808.boot.props.server.Jt808NettyTcpServerProps;
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

    private boolean printComponentStatistics = true;

    @NestedConfigurationProperty
    private ProtocolConfig protocol = new ProtocolConfig();

    @NestedConfigurationProperty
    private Jt808EntityScanProps entityScan = new Jt808EntityScanProps();

    @NestedConfigurationProperty
    private Jt808HandlerScanProps handlerScan = new Jt808HandlerScanProps();

    @NestedConfigurationProperty
    private Jt808ConverterScanProps converterScan = new Jt808ConverterScanProps();

    @NestedConfigurationProperty
    private Jt808ExceptionHandlerScanProps exceptionHandlerScan = new Jt808ExceptionHandlerScanProps();

    @NestedConfigurationProperty
    private Jt808NettyTcpServerProps server = new Jt808NettyTcpServerProps();

    @NestedConfigurationProperty
    private MsgProcessorProps msgProcessor = new MsgProcessorProps();
}
