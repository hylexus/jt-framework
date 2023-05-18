package io.github.hylexus.jt.jt808.boot.props.feature;

import io.github.hylexus.jt.jt808.boot.props.feature.filter.Jt808RequestFilterProps;
import io.github.hylexus.jt.jt808.boot.props.feature.printer.Jt808ParamPrinterProps;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
public class Jt808FeatureProps {

    @NestedConfigurationProperty
    private Jt808RequestFilterProps requestFilter = new Jt808RequestFilterProps();

    @NestedConfigurationProperty
    private Jt808ParamPrinterProps programParamPrinter = new Jt808ParamPrinterProps();

}
