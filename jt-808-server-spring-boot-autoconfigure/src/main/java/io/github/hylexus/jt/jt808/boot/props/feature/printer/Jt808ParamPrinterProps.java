package io.github.hylexus.jt.jt808.boot.props.feature.printer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Jt808ParamPrinterProps {
    /**
     * 如果要启用该配置,必须直接或间接引入{@literal com.fasterxml.jackson.core:jackson-databind} 和 {@literal com.fasterxml.jackson.datatype:jackson-datatype-jsr310}
     */
    private boolean enabled = false;
    /**
     * 格式化输出
     */
    private boolean pretty = true;
}
