package io.github.hylexus.jt808.boot.props.protocol;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.config.JtProtocolConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

/**
 * Created At 2020-07-20 19:49
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Slf4j
@Validated
public class ProtocolConfig {
    private Jt808ProtocolVersion version = Jt808ProtocolVersion.AUTO_DETECTION;
    private int maxFrameLength = JtProtocolConstant.MAX_PACKAGE_LENGTH;
}
