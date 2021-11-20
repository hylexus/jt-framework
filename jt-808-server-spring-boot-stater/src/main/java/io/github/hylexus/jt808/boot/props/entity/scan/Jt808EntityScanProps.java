package io.github.hylexus.jt808.boot.props.entity.scan;

import io.github.hylexus.jt808.boot.props.converter.scan.Jt808ConverterScanProps;
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

    /**
     * @deprecated 使用 {@link Jt808ConverterScanProps#isRegisterBuiltinRequestMsgConverters()} 代替
     */
    @Deprecated
    private boolean registerBuiltinRequestMsgConverters = true;
}
