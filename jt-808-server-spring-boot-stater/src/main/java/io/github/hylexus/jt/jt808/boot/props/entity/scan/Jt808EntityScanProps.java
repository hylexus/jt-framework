package io.github.hylexus.jt.jt808.boot.props.entity.scan;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Slf4j
@Validated
public class Jt808EntityScanProps {
    private boolean enabled = true;
    private Set<String> basePackages;
    private boolean enableBuiltinEntity = true;
}
