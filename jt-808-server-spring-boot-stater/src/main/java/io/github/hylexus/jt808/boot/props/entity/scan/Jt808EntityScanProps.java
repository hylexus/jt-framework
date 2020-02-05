package io.github.hylexus.jt808.boot.props.entity.scan;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-09-22 7:33 下午
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
    private boolean registerBuiltinRequestMsgConverters = true;
}
