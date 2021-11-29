package io.github.hylexus.jt.jt808.boot.props.converter.scan;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

/**
 * @author 李仁豪
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Slf4j
@Validated
public class Jt808ConverterScanProps {
    private boolean enabled = true;
    private Set<String> basePackages;
    private boolean registerBuiltinRequestMsgConverters = true;
}
